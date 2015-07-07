package com.sinch.messagingtutorialskeleton;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.messagingtutorialskeleton.R;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;


/**
 * Created by mtsalis31 on 02-Jul-15.
 */
public class ListUsersActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        String currentUserId = ParseUser.getCurrentUser().getObjectId();
        final ArrayList names = new ArrayList<String>();

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        //don't include yourself
        query.whereNotEqualTo("objectId", currentUserId);
        //display available users in listview.xml
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> userList, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < userList.size(); i++) {

                        names.add(userList.get(i).getUsername().toString());

                    }

                    ListView usersListView = (ListView) findViewById(R.id.usersListView);
                    ArrayAdapter namesArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.user_list_item, names);
                    usersListView.setAdapter(namesArrayAdapter);

                    usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                            openConversation(names, i);
                        }
                    });
                } else {

                    Toast.makeText(getApplicationContext(), "Error loading user list", Toast.LENGTH_LONG).show();
                }
            }
        });

        // progress dialog spinner
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        //broadcast receiver to listen for the broadcast from MessageService
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Boolean success = intent.getBooleanExtra("success", false);
                progressDialog.dismiss();

                //show a toast message if the Sinch service failed to start
                if (!success) {
                    Toast.makeText(getApplicationContext(), "Messaging service failed to start",
                            Toast.LENGTH_LONG).show();
                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,
                new IntentFilter("com.sinch.messagingtutorialskeleton.ListUsersActivity"));


    }

    public void openConversation(ArrayList<String> names, int pos) {
        ParseQuery<ParseUser>  query = ParseUser.getQuery();
        query.whereEqualTo("username", names.get(pos));
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> user, ParseException e) {
                if(e == null){
                    //start the messaging activity
                    Intent intent = new Intent(getApplicationContext(), MessagingActivity.class);
                    intent.putExtra("RECIPIENT_ID", user.get(0).getObjectId());
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Error finding that user",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}
