package heyheyoheyhey.com.ifoundclassmate3;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import heyheyoheyhey.com.ifoundclassmate3.support.ProjectUtils;
import heyheyoheyhey.com.ifoundclassmate3.support.ServerUtils;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends ActionBarActivity implements LoaderCallbacks<Cursor> {


    private final static String TEMP_IP = "99.236.119.157";
    private final static int TEMP_PORT = 3455;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private LoadFromDiskSetting mLoadFromDisk = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private CheckBox mRememberMeCheckBox;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent i = getIntent();

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();
        String userEmail = i.getStringExtra(MainActivity.USER_NAME_MESSAGE);
        if (!userEmail.equals("")) {
            mEmailView.setText(userEmail);
            System.out.println("email view set...");

        }

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        String userPassword = i.getStringExtra(MainActivity.USER_PASSWORD_MESSAGE);
        if (!userPassword.equals("")) mPasswordView.setText(userPassword);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        Button mEmailRegisterButton = (Button) findViewById(R.id.email_register_button);
        mEmailRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2=new Intent(LoginActivity.this, registerActivity.class);
                startActivityForResult(i2, 2);

            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

       // mRememberMeCheckBox = (CheckBox) findViewById(R.id.check_remember_me);

        mLoadFromDisk = new LoadFromDiskSetting(this);
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);

            /*
                    System.out.println("goodhere");
            Account account = new Account(email,"ifoundclassmate.com");
            AccountManager am = AccountManager.get(this);
            boolean accountCreated = am.addAccountExplicitly(account, password, null);

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                if (accountCreated) {  //Pass the new account back to the account manager
                    AccountAuthenticatorResponse response = extras.getParcelable(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
                    Bundle result = new Bundle();
                    result.putString(AccountManager.KEY_ACCOUNT_NAME, email);
                    result.putString(AccountManager.KEY_ACCOUNT_TYPE,"ifoundclassmate.com");
                    response.onResult(result);
                }
                finish();
            }
            */


        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@") || !email.contains(" ") || email.contains(".");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4 || !password.contains(" ");
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), LoginSettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private String mId;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
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
                String toServer = "login\n" + mEmail + "\n" + mPassword + "\n";
                outToServer.writeBytes(toServer);
                String authResult = inFromServer.readLine();

                // password is incorrect
                if (authResult.equals("0")) return false;

                    // user already exists or new user
                else if (authResult.equals("1")) {
                    // obtain user id from server
                    mId = inFromServer.readLine();
                    System.out.println("The server given id is: " + mId);


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
            showProgress(false);

            if (success) {

                Intent intent = new Intent();
                intent.putExtra(MainActivity.USER_ID_MESSAGE, mId);
                intent.putExtra(MainActivity.USER_NAME_MESSAGE, mEmail);
                intent.putExtra(MainActivity.USER_PASSWORD_MESSAGE, mPassword);
//                intent.putExtra(MainActivity.USER_REMEMBER_ME_MESSAGE, mRememberMeCheckBox.isChecked());
                setResult(1, intent);
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }


public class LoadFromDiskSetting implements SharedPreferences.OnSharedPreferenceChangeListener {

        private boolean loadFromDisk;

        public LoadFromDiskSetting(Context context) {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

            // register preference change listener
            prefs.registerOnSharedPreferenceChangeListener(this);

            // and set remembered preferences
            loadFromDisk = prefs.getBoolean("load_from_disk", false);
            ServerUtils.BYPASS_SERVER = loadFromDisk;
        }

        // handle updates to preferences
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            if (key.equals("load_from_disk")) {
                loadFromDisk = prefs.getBoolean("load_from_disk", false);
                System.out.println("Change load from disk setting to: " + loadFromDisk);
                ServerUtils.BYPASS_SERVER = loadFromDisk;
            }
            // etc
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 2){
            return;
        }

        if (resultCode != 1) {
            finish();
            return;
        }


        if (requestCode == 1) {
            System.out.println("accepting information from register class");
            String mId = data.getStringExtra(registerActivity.mUSERID);
            String mEmail = data.getStringExtra(registerActivity.mUSERNAME);
            String mPassword = data.getStringExtra(registerActivity.mPASSWORD);
            System.out.println(mId);

            Intent intent = new Intent();
            intent.putExtra(MainActivity.USER_ID_MESSAGE, mId);
            intent.putExtra(MainActivity.USER_NAME_MESSAGE, mEmail);
            intent.putExtra(MainActivity.USER_PASSWORD_MESSAGE, mPassword);
            //                intent.putExtra(MainActivity.USER_REMEMBER_ME_MESSAGE, mRememberMeCheckBox.isChecked());
            setResult(1, intent);
            finish();
        }
    }



}



