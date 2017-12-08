package com.ananth.databasesample.di;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Babu on 10/12/2017.
 */

@Singleton
@Component(modules={AppModule.class})
public interface AppComponent {
    ContactComponent getContactComponent(ContactModule contactModule);
}
