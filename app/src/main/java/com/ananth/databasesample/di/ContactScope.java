package com.ananth.databasesample.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Babu on 10/12/2017.
 */

@Scope
@Retention(value= RetentionPolicy.RUNTIME)
public @interface ContactScope {
}
