package com.sinch.messagingtutorialskeleton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.*;
import android.widget.Toast;

import com.example.messagingtutorialskeleton.R;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button loginButton;
        Button signUpButton;
        final EditText usernameField;
        final EditText passwordField;

        // Initialize and connect the buttons with new variables
        loginButton = (Button) findViewById(R.id.loginButton);
        signUpButton = (Button) findViewById(R.id.signupButton);
        usernameField = (EditText) findViewById(R.id.loginUsername);
        passwordField = (EditText) findViewById(R.id.loginPassword);

        // Initialize ListUsersActivity
        final Intent intent = new Intent(getApplicationContext(), ListUsersActivity.class);
        // Initialize Sinch Service
        final Intent serviceIntent = new Intent(getApplicationContext(), MessageService.class);


        //See if users are already log in
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            //start sinch service
            startService(serviceIntent);
            //start next activity
            startActivity(intent);
        }



        // Set an onClickListener on login button
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();
                // Check if the submitted user already exist
                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    public void done(ParseUser user, com.parse.ParseException e) {
                        if (user != null) {
                            //start sinch service
                            startService(serviceIntent);
                            //start next activity
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "There was an error logging in.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

            // Set an onClickListener on signup button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();
                ParseUser user = new ParseUser();
                user.setUsername(username);
                user.setPassword(password);
                user.signUpInBackground(new SignUpCallback() {
                    public void done(com.parse.ParseException e) {
                        if (e == null) {
                            //start sinch service
                            startService(serviceIntent);
                            //start next activity
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "There was an error signing up."
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


    }

    public void onDestroy() {
        stopService(new Intent(this, MessageService.class));
        super.onDestroy();
    }

}
