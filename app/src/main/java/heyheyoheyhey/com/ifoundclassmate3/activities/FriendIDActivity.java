package heyheyoheyhey.com.ifoundclassmate3.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBPrivateChatManager;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import heyheyoheyhey.com.ifoundclassmate3.ApplicationSingleton;
import heyheyoheyhey.com.ifoundclassmate3.HomeActivity;
import heyheyoheyhey.com.ifoundclassmate3.R;
import heyheyoheyhey.com.ifoundclassmate3.adapters.DialogsAdapter;

import java.util.ArrayList;
import java.util.List;

import heyheyoheyhey.com.ifoundclassmate3.R;

public class FriendIDActivity extends ActionBarActivity {

    static String friend;
    static int n;
    QBChatService chatService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_id);
    }

    public void Change(View view) {
        EditText friendID = (EditText) findViewById(R.id.Friend);
        friend = friendID.getText().toString();
        //List<Integer> usersIDs = new ArrayList<Integer>();
        //usersIDs.add(Integer.parseInt(friend));
        List<String> usersnames = new ArrayList<String>();
        usersnames.add(friend);
        QBPagedRequestBuilder requestBuilder = new QBPagedRequestBuilder();
        requestBuilder.setPage(1);
        requestBuilder.setPerPage(usersnames.size());

        //
        QBUsers.getUsersByLogins(usersnames, requestBuilder, new QBEntityCallbackImpl<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> users, Bundle params) {


                ((ApplicationSingleton) getApplication()).addDialogsUsers(users);

                QBPrivateChatManager privateChatManager = QBChatService.getInstance().getPrivateChatManager();
                privateChatManager.createDialog(users.get(0).getId(), new QBEntityCallbackImpl<QBDialog>() {
                    @Override
                    public void onSuccess(QBDialog dialog, Bundle args) {

                    }

                    @Override
                    public void onError(List<String> errors) {

                    }
                });
                startActivity(new Intent(FriendIDActivity.this, DialogsActivity.class));
                finish();
            }

            @Override
            public void onError(List<String> errors) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(FriendIDActivity.this);
                dialog.setMessage("get occupants errors: " + errors).create().show();
            }
        });
        /*
        QBUsers.getUsersByIDs(usersIDs, requestBuilder, new QBEntityCallbackImpl<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> users, Bundle params) {


            }

            @Override
            public void onError(List<String> errors) {

            }

        });
        */
    }

    static public int GetID() {
        int n = Integer.parseInt(friend);
        return n;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend_id, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void onBackPressed() {
        Intent intent = new Intent(FriendIDActivity.this, DialogsActivity.class);
        startActivity(intent);
        finish();
    }
}
