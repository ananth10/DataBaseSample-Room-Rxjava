package com.ananth.databasesample.di;

import com.ananth.databasesample.view.ContactListActivity;
import dagger.Component;
import dagger.Subcomponent;

/**
 * Created by Babu on 10/12/2017.
 */

@ContactScope
@Subcomponent(modules={ContactModule.class})
public interface ContactComponent {
    void inject(ContactListActivity contactList);
}
