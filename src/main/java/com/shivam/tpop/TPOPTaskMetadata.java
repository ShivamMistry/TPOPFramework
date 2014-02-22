package com.shivam.tpop;

/**
 * Created with IntelliJ IDEA.
 * User: sm1334
 * Date: 21/02/14
 * Time: 15:30
 * To change this template use File | Settings | File Templates.
 */
public @interface TPOPTaskMetadata {
    String name();
    String description() default "";
    double version() default 1.00d;
}
