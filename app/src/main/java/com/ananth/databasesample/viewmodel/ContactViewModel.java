package com.ananth.databasesample.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.ananth.databasesample.db.AppDatabase;
import com.ananth.databasesample.model.local.ContactModel;
import com.ananth.databasesample.repository.local.LocalRepository;
import com.ananth.databasesample.repository.remote.RemoteRepository;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Babu on 10/4/2017.
 */

public class ContactViewModel extends ViewModel {

    private LocalRepository localRepository;

    private RemoteRepository remoteRepository;

    private CompositeDisposable compositeDisposable;

    private ContactModel mContactModel;

    private String mPhone="";

    private AppDatabase mAppDatabase;

    public ContactViewModel(LocalRepository localRepo,RemoteRepository remoteRepository,CompositeDisposable compositeDisposable) {

        this.localRepository=localRepo;
        remoteRepository=remoteRepository;
        compositeDisposable=compositeDisposable;

    }

    /**
     *  get All contacts
     * */
    public Flowable<List<ContactModel>> getContactList() {
        return localRepository.getAllContacts();
    }

    /**
     * get a single contact
     * */
//    public LiveData<ContactModel> getSingleContact() {
//        return mSingleContact;
//    }

    public void deleteContact(ContactModel contact) {
        new DeleteContact(mAppDatabase).execute(contact);
    }

    private static class DeleteContact extends AsyncTask<ContactModel, Void, Void> {
        AppDatabase mAppDatabase;

        DeleteContact(AppDatabase appDatabase) {
            mAppDatabase = appDatabase;
        }

        @Override
        protected Void doInBackground(ContactModel... params) {
            mAppDatabase.getContactDao().deleteContact(params[0]);
            return null;
        }
    }
}
