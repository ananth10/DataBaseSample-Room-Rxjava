package com.ananth.databasesample.di;

import com.ananth.databasesample.api.ContactListApi;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Babu on 10/12/2017.
 */

@Module
public class AppModule {
    public static final String BASE_URL = "http://www.zoftino.com/api/";

    @Singleton
    @Provides
    public Executor getExecutor() {
        return Executors.newFixedThreadPool(2);
    }

    @Singleton
    @Provides
    public Retrofit getRemoteServer() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    public ContactListApi getContactList(Retrofit retrofit) {
        return retrofit.create(ContactListApi.class);
    }
}
