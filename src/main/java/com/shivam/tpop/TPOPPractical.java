package com.shivam.tpop;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: sm1334
 * Date: 21/02/14
 * Time: 14:51
 * To change this template use File | Settings | File Templates.
 */
public class TPOPPractical {

    private static final Properties TPOP_PROPERTIES;
    private static final File OUTPUT_FILE;
    private List<TPOPTask> tasks = new ArrayList<>();
    private ClassLoader cl = this.getClass().getClassLoader();
    private ExecutorService executor;


    static {
        TPOP_PROPERTIES = new Properties();
        try {
            TPOP_PROPERTIES.load(new FileReader("tpop.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        OUTPUT_FILE = new File(TPOP_PROPERTIES.getProperty("tpop.output_location"),
                TPOP_PROPERTIES.getProperty("tpop.package_name").replace(".", File.separator));
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter practical would you like to run: ");
        int n = 0;
        while (n == 0) {
            try {
                n = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("That was not a valid number!");
                scanner.next();
            }
        }
        try {
            new TPOPPractical().run(n);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public TPOPPractical() {
        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }


    public void run(int practical) throws ClassNotFoundException, ReflectiveOperationException, FileNotFoundException {
        System.out.println("Loading files from practical " + practical);
        File dir = new File(OUTPUT_FILE, "practical" + practical);
        if (!dir.exists()) {
            throw new FileNotFoundException(dir.getAbsolutePath());
        }
        for (final File f : dir.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".class") && !pathname.getName().contains("$");
            }
        })) {
            Class<?> task = Class.forName(
                    TPOP_PROPERTIES.getProperty("tpop.package_name") + ".practical" + practical + "."
                            + f.getName().replace(".class", ""));
            if (task.getAnnotations().length > 0) {
                TPOPTaskMetadata metadata = task.getAnnotation(TPOPTaskMetadata.class);
                System.out.printf("\tTask Name: %s, Description: %s, Version: %l\n",
                        metadata.name(), metadata.description(), metadata.version());
            }
            TPOPTask taskInstance = (TPOPTask) task.newInstance();
            tasks.add(taskInstance);
        }
        System.out.println("Loaded " + tasks.size() + " tasks");
        for (TPOPTask tpop : tasks) {
            executor.submit(tpop);
        }
        try {
            executor.awaitTermination(1000L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();

    }


}
