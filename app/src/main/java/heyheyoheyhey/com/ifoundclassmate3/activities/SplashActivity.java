package heyheyoheyhey.com.ifoundclassmate3.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.QBSettings;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smack.SmackException;

import java.util.List;

import heyheyoheyhey.com.ifoundclassmate3.HomeActivity;
import heyheyoheyhey.com.ifoundclassmate3.R;
import heyheyoheyhey.com.ifoundclassmate3.LoginActivity;
import heyheyoheyhey.com.ifoundclassmate3.ApplicationSingleton;

public class SplashActivity extends Activity {

    private static final String APP_ID = "20563";
    private static final String AUTH_KEY = "jh-Z-TYuQf-k33N";
    private static final String AUTH_SECRET = "8HGNRx3V4EVXW2-";

    static final int AUTO_PRESENCE_INTERVAL_IN_SECONDS = 30;

    static String ID = heyheyoheyhey.com.ifoundclassmate3.LoginActivity.getID();
    static String PW = heyheyoheyhey.com.ifoundclassmate3.LoginActivity.getPW();

    private static QBUser user;

    static private QBChatService chatService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        QBSettings.getInstance().fastConfigInit(APP_ID, AUTH_KEY, AUTH_SECRET);
        if (!QBChatService.isInitialized()) {
            QBChatService.init(this);
        }
        chatService = QBChatService.getInstance();

        QBAuth.createSession(new QBEntityCallbackImpl<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle args) {
                //user.setId(session.getUserId());
                //System.out.println(session.getUserId());
                final QBUser user = new QBUser(ID, PW);

                QBUsers.signIn(user, new QBEntityCallbackImpl<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser, Bundle args) {
                        user.setId(qbUser.getId());
                        // success
                        ((ApplicationSingleton) getApplicationContext()).setCurrentUser(user);
                        chatService.login(user, new QBEntityCallbackImpl() {
                            @Override
                            public void onSuccess() {

                                // Start sending presences
                                //
                                try {
                                    chatService.startAutoSendPresence(AUTO_PRESENCE_INTERVAL_IN_SECONDS);
                                } catch (SmackException.NotLoggedInException e) {
                                    e.printStackTrace();
                                }

                                // go to Dialogs screen
                                //
                                Intent intent = new Intent(SplashActivity.this, DialogsActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onError(List errors) {
                                System.out.println(errors);
                                AlertDialog.Builder dialog = new AlertDialog.Builder(SplashActivity.this);
                                dialog.setMessage("chat login errors: " + errors).create().show();
                            }
                        });
                    }

                    @Override
                    public void onError(List<String> errors) {
                        // error
                    }
                });

            }

            @Override
            public void onError(List<String> errors) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(SplashActivity.this);
                dialog.setMessage("create session errors: " + errors).create().show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
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

    static public void logout() {
        chatService.logout(new QBEntityCallbackImpl() {

            @Override
            public void onSuccess() {
                // success
                QBUsers.signOut(new QBEntityCallbackImpl() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(List errors) {

                    }
                });
                chatService.destroy();
            }

            @Override
            public void onError(final List list) {

            }
        });
    }

}
