package com.sinch.messagingtutorialskeleton;

import android.app.Activity;
import android.os.Bundle;
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
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> userList, ParseException e) {
                if(e == null) {
                    for(int i=0; i<userList.size(); i++ ){

                        names.add(userList.get(1).getUsername().toString());

                    }

                    ListView usersListView = (ListView)findViewById(R.id.usersListView);
                    ArrayAdapter namesArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.user_list_item, names);
                    usersListView.setAdapter(namesArrayAdapter);

                    usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
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

    }

    public void openConversation(ArrayList<String> names, int pos) {
        ParseQuery<ParseUser>  query = ParseUser.getQuery();
        query.whereEqualTo("username", names.get(pos));
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if(e == null){
                    //start the messaging activity

                } else {
                    Toast.makeText(getApplicationContext(), "Error finding that user", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
