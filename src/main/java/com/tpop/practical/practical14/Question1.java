package com.tpop.practical.practical14;

import com.tpop.TPOPTask;
import com.tpop.TPOPTaskMetadata;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: sm1334
 * Date: 28/02/14
 * Time: 14:49
 * To change this template use File | Settings | File Templates.
 */
@TPOPTaskMetadata(name = "Question 1",
        description = "Vertex class",
        version = 1.00D,
        practical = "Practical 14")
public class Question1 implements TPOPTask {


    public void run() {
        City london = new City("London", "UK", "Greater London", "GMT", 51.507222D, -0.1275D);
        //System.out.println(london);
    }
}
