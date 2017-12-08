package com.ananth.databasesample.api;

import com.ananth.databasesample.model.remote.ContactList;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by Babu on 10/12/2017.
 */

public interface ContactListApi {
    @GET("contactlist/")
    Observable<ContactList> getContactList();
}
