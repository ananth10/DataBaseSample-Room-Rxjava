package com.ananth.databasesample.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.ananth.databasesample.api.ContactListApi;
import com.ananth.databasesample.db.AppDatabase;
import com.ananth.databasesample.db.ContactDao;
import com.ananth.databasesample.repository.local.LocalRepository;
import com.ananth.databasesample.repository.local.LocalRepositoryImpl;
import com.ananth.databasesample.repository.remote.RemoteRepository;
import com.ananth.databasesample.repository.remote.RemoteRepositoryImpl;

import java.util.concurrent.Executor;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Babu on 10/12/2017.
 */
@Module
public class ContactModule {
    private Context mContext;

    public ContactModule(Context context) {
        mContext = context;
    }

    @ContactScope
    @Provides
    public ContactDao getContactDao(AppDatabase appDatabase) {
        return appDatabase.getContactDao();
    }

    @ContactScope
    @Provides
    public AppDatabase getAppDatabase() {
        return Room.databaseBuilder(mContext,AppDatabase.class,"contact_db").build();
    }

    @ContactScope
    @Provides
    public LocalRepository getLocalRepo(ContactDao contactDao, Executor executor) {
        return new LocalRepositoryImpl(contactDao, executor);
    }

    @ContactScope
    @Provides
    @Named("activity")
    public CompositeDisposable getCompositeDisposable() {
        return new CompositeDisposable();
    }

    @ContactScope
    @Provides
    @Named("vm")
    public CompositeDisposable getVmCompositeDisposable() {
        return new CompositeDisposable();
    }

    @ContactScope
    @Provides
    public RemoteRepository getRemoteRepo(ContactListApi contactListApi){
        return new RemoteRepositoryImpl(contactListApi);
    }
}
