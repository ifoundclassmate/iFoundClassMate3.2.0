package heyheyoheyhey.com.ifoundclassmate3;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.fitness.request.m;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;

import heyheyoheyhey.com.ifoundclassmate3.support.ServerUtils;

/**
 * Created by yunfei on 3/5/2015.
 */
public class registerActivity extends ActionBarActivity {

    private final static String TEMP_IP = "99.236.119.157";
    private final static int TEMP_PORT = 3455;
    private UserRegisterTask mAuthTask = null;


    public static String mPASSWORD = "";
    public static String mUSERNAME = "";
    public static String mUSERID = "";
    public static String mShouldGoHomeDirectly = "FALSE";


    private EditText mPasswordView;
    private EditText mUsernameView;
    private EditText mEmailView;
    private EditText mConfirmedPasswordView;
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent i = getIntent();

        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mUsernameView = (EditText) findViewById(R.id.username);
        mConfirmedPasswordView = (EditText) findViewById(R.id.confirmPassword);

        Button mEmailRegisterButton = (Button) findViewById(R.id.registerButton);
        mEmailRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

    }

    private void attemptRegister(){

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String confirmPassword = mConfirmedPasswordView.getText().toString();
        String username = mUsernameView.getText().toString();

        boolean allCorrect = true;

        if(!confirmPassword.equals(password)){
            mConfirmedPasswordView.setError("password doesn't match the confirmed password");
            allCorrect = false;
            mConfirmedPasswordView.requestFocus();
        }

        if(email.equals("")){
            mEmailView.setError("email cannot be empty");
            allCorrect = false;
            mEmailView.requestFocus();

        }

        if(password.equals("")){
            mPasswordView.setError("password cannot be empty");
            allCorrect = false;
            mPasswordView.requestFocus();
        }

        if(username.equals("")){
            mUsernameView.setError("username cannot be empty");
            allCorrect = false;
            mUsernameView.requestFocus();
        }

        //contact server
        if(!allCorrect){

        }else{
            mAuthTask = new UserRegisterTask(username,email, password);
            mAuthTask.execute((Void) null);
        }

    }

    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final String mUsername;
        private String mId;

        UserRegisterTask(String username, String email, String password) {
            mEmail = email;
            mPassword = password;
            mUsername = username;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            System.out.println(ServerUtils.BYPASS_SERVER);
            if (ServerUtils.BYPASS_SERVER) {
                mId = "100";
                return true;
            }

            try {
                // connect to server
                Socket clientSocket = new Socket(TEMP_IP, TEMP_PORT);
                System.out.println("here3");
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String toServer = "ac" + "\n" + mUsername + "\n" + mEmail + "\n" + mPassword + "\n" ;
                outToServer.writeBytes(toServer);
                String authResult = inFromServer.readLine();

                // user already exists
                if (authResult.equals("0")) return false;

                    // new user
                else if (authResult.equals("1")) {
                    // obtain user id from server
                    mId = inFromServer.readLine();
                    mUSERNAME = mUsername;
                    mPASSWORD = mPassword;
                    mUSERID = mId;

                    System.out.println("The server given id is: " + mId);
                    System.out.println("My username is: " + mUSERNAME);

                }
                clientSocket.close();
            } catch (ConnectException e) {
                System.out.println("Not found server...");

                return false;
            } catch (IOException e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                mShouldGoHomeDirectly = "TRUE";
                Intent intent = new Intent(registerActivity.this, LoginActivity.class);
                intent.putExtra(registerActivity.mUSERID, mUSERID);
                intent.putExtra(registerActivity.mUSERNAME, mUSERNAME);
                intent.putExtra(registerActivity.mPASSWORD, mPASSWORD);
                setResult(1,intent);
                finish();
            } else {
                mUsernameView.setError("user already exists");
                mUsernameView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;

        }
    }


}
