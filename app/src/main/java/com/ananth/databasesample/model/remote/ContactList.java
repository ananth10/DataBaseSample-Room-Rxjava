package com.ananth.databasesample.model.remote;

import com.ananth.databasesample.model.local.ContactModel;

import java.util.List;

/**
 * Created by Babu on 10/12/2017.
 */

public class ContactList {
    private List<ContactModel> mContactList;

    public List<ContactModel> getContactList()
    {
        return mContactList;
    }

    public void setContactList(List<ContactModel> list)
    {
        mContactList=list;
    }
}
