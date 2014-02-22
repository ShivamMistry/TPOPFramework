package com.tpop;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * User: sm1334
 * Date: 21/02/14
 * Time: 15:30
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TPOPTaskMetadata {
    String name();

    String description() default "";

    double version() default 1.00d;

    String practical();
}
