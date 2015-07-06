package com.sinch.messagingtutorialskeleton;

import android.app.Activity;
import android.os.Bundle;

import com.parse.Parse;

/**
 * Created by mtsalis31 on 06-Jul-15.
 */
public class MyApplication extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "GNsyBu7UWQ8qRkj5Xg0LhAydJ4QIVFafD5s0EOz0", "2YAQJ9GmIKtg2YuxB5UhcjiRHSjr6CE7pS3cyfYw");

    }
}
