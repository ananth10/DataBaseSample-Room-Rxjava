package com.ananth.databasesample.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.ananth.databasesample.di.ContactScope;
import com.ananth.databasesample.repository.local.LocalRepository;
import com.ananth.databasesample.repository.remote.RemoteRepository;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Babu on 10/9/2017.
 */

@ContactScope
public class ViewModelFactory implements ViewModelProvider.Factory {
    @Inject
    LocalRepository localRepository;
    @Inject
    RemoteRepository remoteRepository;
    @Inject @Named("vm")
    CompositeDisposable compositeDisposable;

    @Inject
    public ViewModelFactory(LocalRepository dataSource) {
        localRepository = dataSource;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ContactViewModel.class)) {
            return (T) new ContactViewModel(localRepository,remoteRepository,compositeDisposable);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
