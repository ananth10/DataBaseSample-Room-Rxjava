package com.ananth.databasesample.repository.remote;

import com.ananth.databasesample.api.ContactListApi;
import com.ananth.databasesample.model.local.ContactModel;
import com.ananth.databasesample.model.remote.ContactList;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Babu on 10/12/2017.
 */

public class RemoteRepositoryImpl implements RemoteRepository {

    private ContactListApi contactListApi;

    public RemoteRepositoryImpl(ContactListApi contactListApi) {
        this.contactListApi = contactListApi;
    }

    @Override
    public Observable<ContactList> getContactList() {
        return contactListApi.getContactList();
    }
}
