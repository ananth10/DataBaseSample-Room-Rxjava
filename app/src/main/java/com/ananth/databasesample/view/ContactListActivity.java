package com.ananth.databasesample.view;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ananth.databasesample.R;
import com.ananth.databasesample.adapter.ContactAdapter;
import com.ananth.databasesample.application.MyApplication;
import com.ananth.databasesample.db.AppDatabase;
import com.ananth.databasesample.di.ContactModule;
import com.ananth.databasesample.model.local.ContactModel;
import com.ananth.databasesample.viewmodel.ContactViewModel;
import com.ananth.databasesample.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ContactListActivity extends LifecycleActivity {
    @Inject
    @Named("activity")
    CompositeDisposable compositeDisposable;

    @Inject
    ViewModelFactory viewModelFactory;
    private RecyclerView mList;
    //    DBHelper myDb;
    ArrayList<HashMap<String, String>> mContactList = new ArrayList<>();
    private ContactAdapter mAdapter;
    private ContactViewModel mViewModel;
    private LinearLayout mNoResult;
    private TextView mCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList = (RecyclerView) findViewById(R.id.recyclerView);
        mNoResult = (LinearLayout) findViewById(R.id.no_result_lay);
        mCreate = (TextView) findViewById(R.id.create_contact);
        mList.setVisibility(View.VISIBLE);
        mNoResult.setVisibility(View.GONE);



        MyApplication.getComponent(getApplicationContext()).getContactComponent(new ContactModule(getApplicationContext())).inject(this);

        //instantiate view model
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(ContactViewModel.class);
        //call retrofit service to get latest data and update database
        //runs in the background thread
//        mViewModel.getContactList();
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(ContactListActivity.this, CreateContact.class);
                io.putExtra("type", "create");
                startActivity(io);
            }
        });


        /**
         * Set the adapter for recyclerview
         * */
        mAdapter = new ContactAdapter(ContactListActivity.this, new ArrayList<ContactModel>());
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.setAdapter(mAdapter);


        handleViewState();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Subscribe to the emissions of the user name from the view model.
        // Update the user name text view, at every onNext emission.
        // In case of error, log the exception.
        compositeDisposable.add(mViewModel.getContactList()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ContactModel>>() {
                    @Override
                    public void accept(List<ContactModel> contact) throws Exception {
                        if (contact != null) {
                            mAdapter.addContact(contact);
                            handleViewState();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("MainActivity", "exception getting coupons");
                    }
                }));

    }

    private void handleViewState() {
        if (mList.getAdapter().getItemCount() > 0) {
            mList.setVisibility(View.VISIBLE);
            mNoResult.setVisibility(View.GONE);
        } else {
            mList.setVisibility(View.GONE);
            mNoResult.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
//        AppDatabase.destroyInstance();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.create) {
            Intent io = new Intent(ContactListActivity.this, CreateContact.class);
            io.putExtra("type", "create");
            startActivity(io);
            return true;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_contact, menu);
        return true;
    }

}
