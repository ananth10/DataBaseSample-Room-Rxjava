package com.ananth.databasesample.repository.local;

import com.ananth.databasesample.model.local.ContactModel;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

/**
 * Created by Babu on 10/12/2017.
 */

public interface LocalRepository {
    /**
     * Get all contacts
     * */
    Flowable<List<ContactModel>> getAllContacts();

    /**
     * Get contact by phone
     * */
    Maybe<ContactModel> getContactByPhone(String phone);

    /**
     * Check contact exist or not
     * */
    ContactModel isContactExit(String phone);

    /**
     * Add a new contact
     * */
    void addContact(ContactModel contact);

    /**
     * Delete a contact
     * */
    void deleteContact(ContactModel contact);

    /**
     * update contact
     * */
    void updateContact(ContactModel contact);
}
