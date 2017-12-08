package com.ananth.databasesample.repository.remote;

import com.ananth.databasesample.model.remote.ContactList;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Babu on 10/12/2017.
 */

public interface RemoteRepository {

    Observable<ContactList> getContactList();
}
