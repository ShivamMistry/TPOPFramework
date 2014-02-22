package com.tpop.practical.practical13;

import com.tpop.TPOPTask;
import com.tpop.TPOPTaskMetadata;

/**
 * User: sm1334
 * Date: 21/02/14
 * Time: 16:12
 */
@TPOPTaskMetadata(name = "Exercise 1",
        description = "Pick one or two questions from questions 1 to 4 of practical 2 and rewrite them in Java",
        version = 1.00D,
        practical = "Practical 13")
public class Task1 implements TPOPTask {

    public void run() {
        while (true) {
            try {
                Thread.sleep(100L);
                //current thread sleeps for 100L
            } catch (InterruptedException e) {
                System.out.println("Interrupted :(");
                //we were interrupted
                return; //returns
            }
        }

    }
}
