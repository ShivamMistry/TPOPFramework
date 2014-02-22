package com.tpop;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * User: sm1334
 * Date: 21/02/14
 * Time: 14:51
 */
public class TPOPPractical {

    private static final Properties TPOP_PROPERTIES;
    private static final File OUTPUT_FILE;
    private List<TPOPTask> tasks = new LinkedList<>();
    private ExecutorService executor;
    private static File[] practicals;


    static {
        TPOP_PROPERTIES = new Properties();
        try {
            TPOP_PROPERTIES.load(new FileReader("tpop.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        OUTPUT_FILE = new File(TPOP_PROPERTIES.getProperty("tpop.output_location"),
                TPOP_PROPERTIES.getProperty("tpop.package_name").replace(".", File.separator));
        practicals = loadPracticals();
    }

    private static File[] loadPracticals() {
        final File practicalDir = new File(TPOP_PROPERTIES.getProperty("tpop.output_location"),
                TPOP_PROPERTIES.getProperty("tpop.package_name").replace(".", "/"));
        return practicalDir.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.isDirectory() && pathname.getName().startsWith("practical");
            }
        });
    }

    public static void main(String[] args) {
        System.out.println("Implemented practicals: ");
        final HashMap<Integer, File> practicalMap = new HashMap<>();
        for (final File f : practicals) {
            final String practicalNum = f.getName().substring("practical".length());
            System.out.println("\t Practical " +
                    practicalNum);
            try {
                practicalMap.put(Integer.parseInt(practicalNum), f);
            } catch (NumberFormatException e) {
                System.err.println("Error in practical package naming.");
                System.err.printf("\"%s\" is not a valid practical number. \n", practicalNum);
                return;
            }
        }
        Scanner scanner = new Scanner(System.in);
        int n = 0;
        while (n == 0) {
            System.out.print("Enter practical would you like to run: ");
            try {
                n = scanner.nextInt();
                if (!practicalMap.containsKey(n)) {
                    n = 0;
                    System.err.println("That practical hasn't been implemented yet.");
                }
            } catch (InputMismatchException e) {
                System.err.println("That was not a valid practical!");
                scanner.next();
            }
        }
        try {
            new TPOPPractical().run(n);
        } catch (ReflectiveOperationException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public TPOPPractical() {
        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }


    public void run(int practical) throws ReflectiveOperationException, FileNotFoundException {
        System.out.println("Loading exercises from practical " + practical);
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
            TPOPTaskMetadata metadata = task.getAnnotation(TPOPTaskMetadata.class);
            if (metadata != null)
                System.out.printf("\tTask Name: %s, Description: %s, Version: %.2f\n",
                        metadata.name(), metadata.description(), metadata.version());
            TPOPTask taskInstance = (TPOPTask) task.newInstance();
            tasks.add(taskInstance);
        }
        System.out.println("Loaded " + tasks.size() + " exercises");
        for (TPOPTask tpop : tasks) {
            executor.submit(tpop);
        }
        executor.shutdown();
        try {
            executor.awaitTermination(10000L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ignored) {
        }
        if (!executor.isTerminated()) {
            System.err.println("Looks like your tasks are too slow. Make them more efficient.");
            List<Runnable> runnable = executor.shutdownNow();
            for (Runnable r : runnable) {
                TPOPTaskMetadata metadata = r.getClass().getAnnotation(TPOPTaskMetadata.class);
                if (metadata != null) {
                    System.out.printf("\tTask was still running:\t\nName: %s, " +
                            "Description: %s, Version: %f, Practical: %s\n",
                            metadata.name(), metadata.description(),
                            metadata.version(), metadata.practical());
                } else {
                    System.out.println("\tYou didn't include metadata for this task");
                    System.out.println("\tAttempting to inspect task:");
                    System.out.printf("\tStill running: %s\n", r.getClass().getName());
                }
            }
        }
    }


}
