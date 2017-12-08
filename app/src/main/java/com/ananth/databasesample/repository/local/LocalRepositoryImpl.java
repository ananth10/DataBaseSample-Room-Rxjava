package com.ananth.databasesample.repository.local;

import com.ananth.databasesample.db.ContactDao;
import com.ananth.databasesample.model.local.ContactModel;

import java.util.List;
import java.util.concurrent.Executor;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

/**
 * Created by Babu on 10/9/2017.
 */

public class LocalRepositoryImpl implements LocalRepository {

    private ContactDao mContactDao;
    private Executor executor;

    public LocalRepositoryImpl(ContactDao contactDao, Executor executor) {
        this.mContactDao = contactDao;
        this.executor = executor;
    }

    @Override
    public Flowable<List<ContactModel>> getAllContacts() {
        return mContactDao.getAllContacts();
    }

    @Override
    public Maybe<ContactModel> getContactByPhone(String phone) {
        return mContactDao.getContactByPhone(phone);
    }

    @Override
    public ContactModel isContactExit(String phone) {
        return mContactDao.isContactExit(phone);
    }

    @Override
    public void addContact(final ContactModel contact) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mContactDao.addContact(contact);
            }
        });
    }

    @Override
    public void deleteContact(final ContactModel contact) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mContactDao.deleteContact(contact);
            }
        });
    }

    @Override
    public void updateContact(final ContactModel contact) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mContactDao.deleteContact(contact);
            }
        });
    }
}
