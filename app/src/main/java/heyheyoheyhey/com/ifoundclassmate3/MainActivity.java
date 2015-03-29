package heyheyoheyhey.com.ifoundclassmate3;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import heyheyoheyhey.com.ifoundclassmate3.support.ServerFunction;
import heyheyoheyhey.com.ifoundclassmate3.support.ServerUtils;


public class MainActivity extends ActionBarActivity {
    public final static String EXTRA_MESSAGE = "test";
    public final static String USER_ID_MESSAGE = "USER_ID_MESSAGE";
    public final static String USER_NAME_MESSAGE = "userName";
    public final static String USER_PASSWORD_MESSAGE = "USER_PASSWORD_MESSAGE";
    public final static String USER_REMEMBER_ME_MESSAGE = "USER_REMEMBER_ME_MESSAGE";

    // for home activity
    public final static String USER_MESSAGE = "USER_MESSAGE";

    public final static String SHOULD_GO_HOME_DIRECTLY = "FALSE";



    // metadata constants
    private final static String METADATA_FILE = "metadata.info";
    private final static String AUTO_LOGIN = "AUTO_LOGIN";

    private User user;
    private ProgressBar loader;
    private int loaderProgress = 0;

    private User savedUser;

    private boolean courseReady = false;
    private boolean friendReady = false;
    private boolean groupReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loader = (ProgressBar) findViewById(R.id.mainProgressBar);
        loader.setMax(100);
        loader.setProgress(1);

        Intent i = getIntent();

        getUserData();
    }

    private void getUserData() {
        Context context = getApplicationContext();
        File file = context.getFilesDir();
        if (file.isDirectory()) {
            System.out.println("heyhey dir is " + file.getPath());
            File users[] = file.listFiles();
            if (users != null && users.length != 0) {
                // already user setup
                for (int i = 0; i < users.length; i++) {
                    System.out.println("there is user: " + users[i].getPath());
                }


                int ch;
                StringBuffer fileContent = new StringBuffer("");
                FileInputStream fis;
                try {
                    fis = context.openFileInput( METADATA_FILE );
                    try {
                        while( (ch = fis.read()) != -1)
                            fileContent.append((char)ch);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String data = new String(fileContent);
                    int findAutoLogin = data.indexOf(AUTO_LOGIN + ":") + AUTO_LOGIN.length() + 1;
                    int findAutoLoginEnd = data.indexOf("\n", findAutoLogin);
                    if (findAutoLogin != -1) {
                        String userToLogin = data.substring(findAutoLogin, findAutoLoginEnd);
                        System.out.println("Finding user to login : " + userToLogin);
                        for (int i = 0; i < users.length; i++) {
                            if (users[i].getName().equals(userToLogin)) {
                                // start this user...
                                System.out.println("Starting user " + users[i].getName());
                                savedUser = new User(users[i], getApplicationContext());
                                Intent intent = new Intent(this, LoginActivity.class);
                                intent.putExtra(USER_ID_MESSAGE, savedUser.getId());
                                intent.putExtra(USER_NAME_MESSAGE, savedUser.getUsername());
                                intent.putExtra(USER_PASSWORD_MESSAGE, savedUser.getPassword());
                                intent.putExtra(USER_REMEMBER_ME_MESSAGE, true);
                                startActivityForResult(intent, 1);
                                return;
                            }
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                // users saved on disk, but not "remember me"-ed...
                Intent intent = new Intent(this, LoginActivity.class);
                intent.putExtra(USER_ID_MESSAGE, "");
                intent.putExtra(USER_NAME_MESSAGE, "");
                intent.putExtra(USER_PASSWORD_MESSAGE, "");
                intent.putExtra(USER_REMEMBER_ME_MESSAGE, true);
                startActivityForResult(intent, 1);
            } else {
                // no users saved on disk...
                Intent intent = new Intent(this, LoginActivity.class);
                intent.putExtra(USER_ID_MESSAGE, "");
                intent.putExtra(USER_NAME_MESSAGE, "");
                intent.putExtra(USER_PASSWORD_MESSAGE, "");
                intent.putExtra(USER_REMEMBER_ME_MESSAGE, true);
                startActivityForResult(intent, 1);
            }
        }

    }

    // This starts the home screen
    private void startHome(User user) {
        /*
        try {

            String oldstring = "2015-01-05 00:00:00.0";
            Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(oldstring);
            String oldstring2 = "2015-04-08 00:00:00.0";
            Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(oldstring2);
            boolean[] day = new boolean[7];
            for (int i = 0; i < day.length; i++) {
                day[i] = false;
            }
            day[1] = true;
            day[3] = true;
            CourseItem courseItem = new CourseItem("CS 349", "LEC 001", date1, date2, day, 13, 0, 14, 20);
            user.addScheduleItem(courseItem);
            user.writeToDisk(getApplicationContext());

        } catch (Exception e) {
            System.out.println("ADDING COURSE UNSUCCESSFUL!!");
            e.printStackTrace();
        }
*/
        loader.setProgress(99);

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(USER_MESSAGE, user);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 1) {
            finish();
            return;
        }

        if (requestCode == 1) {

            // Login screen landing: get login details
            String userId = data.getStringExtra(USER_ID_MESSAGE);
            String userName = data.getStringExtra(USER_NAME_MESSAGE);
            String userPassword = data.getStringExtra(USER_PASSWORD_MESSAGE);
            boolean userAutoLogin = data.getBooleanExtra(USER_REMEMBER_ME_MESSAGE, true);
            if (userAutoLogin) {
                String path = METADATA_FILE;
                String toWrite = AUTO_LOGIN + ":" + userId + "\n";
                FileOutputStream outputStream;

                try {
                    outputStream = openFileOutput(path, Context.MODE_PRIVATE);
                    outputStream.write(toWrite.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            System.out.println("userId: " + userId + ", userName: " + userName + ", password: " + userPassword);

            if (ServerUtils.BYPASS_SERVER) {
                // Starting from disk: immediately start home screen
                if (savedUser == null) {
                    savedUser = new User(userId, userName, userPassword);
                }
                savedUser.SetCalendar(getApplicationContext());
                startHome(savedUser);
            } else {
                // not starting from disk... retrieve info from server.
                // TODO: if a user previously has saved to disk, we should upload that to server...
                user = new User(userId, userName, userPassword);
                loaderProgress += 22;
                loader.setProgress(loaderProgress);
                user.SetCalendar(getApplicationContext());
                // Step 1: get courses from server
                ServerFunction getCoursesTask = new ServerFunction(ServerUtils.TASK_RETRIEVE_COURSES);
                getCoursesTask.setUser(user);
                getCoursesTask.setListener(new ServerFunction.ServerTaskListener() {
                    @Override
                    public void onPostExecuteConcluded(boolean result, Object retVal) {
                        if (result) {
                            System.out.println("Server returned courses...");
                            ArrayList<CourseItem> courseItems = (ArrayList<CourseItem>) retVal;
                            if (courseItems.isEmpty()) System.out.println("User has no ccourses");
                            for (CourseItem courseItem : courseItems) {
                                System.out.println("Populating course for user: " + courseItem.getId());
                                user.addScheduleItem(courseItem);
                            }
                            user.writeToDisk(getApplicationContext());
                            onReadyStartHome(1);
                        }
                    }
                });
                getCoursesTask.execute((Void) null);

                // Step 2: get friends from server
                ServerFunction getFriendsTask = new ServerFunction(ServerUtils.TASK_RETRIEVE_FRIENDS);
                getFriendsTask.setUser(user);
                getFriendsTask.setListener(new ServerFunction.ServerTaskListener() {
                    @Override
                    public void onPostExecuteConcluded(boolean result, Object retVal) {
                        if (result) {
                            System.out.println("Server returned friends...");
                            ArrayList<String> friends = (ArrayList<String>) retVal;
                            if (friends.isEmpty()) System.out.println("User has no friends");
                            for (String friend : friends) {
                                System.out.println("Populating friend for user: " + friend);
                                user.addFriend(friend);
                            }
                            //user.writeToDisk(getApplicationContext());
                            onReadyStartHome(2);
                        }
                    }
                });
                getFriendsTask.execute((Void) null);

                // Step 3: get groups from server
                ServerFunction getGroupsTask = new ServerFunction(ServerUtils.TASK_RETRIEVE_GROUPS);
                getGroupsTask.setUser(user);
                getGroupsTask.setListener(new ServerFunction.ServerTaskListener() {
                    @Override
                    public void onPostExecuteConcluded(boolean result, Object retVal) {
                        if (result) {
                            System.out.println("Server returned groups...");
                            ArrayList<Group> groups = (ArrayList<Group>) retVal;
                            if (groups.isEmpty()) System.out.println("User has no groups");
                            for (Group group : groups) {
                                System.out.println("Populating friend for user: " + group.getId());
                                user.addToGroup(group);
                            }
                            //user.writeToDisk(getApplicationContext());
                            onReadyStartHome(3);
                        }
                    }
                });
                getGroupsTask.execute((Void) null);

            }
        }
    }

    private void startHomeFromRegister(String userId,String userName, String userPassword){

        user = new User(userId, userName, userPassword);
        loader.setProgress(50);
        // Step 1: get courses from server
        ServerFunction getCoursesTask = new ServerFunction(ServerUtils.TASK_RETRIEVE_COURSES);
        getCoursesTask.setUser(user);
        getCoursesTask.setListener(new ServerFunction.ServerTaskListener() {
            @Override
            public void onPostExecuteConcluded(boolean result, Object retVal) {
                if (result) {
                    System.out.println("Server returned courses...");
                    ArrayList<CourseItem> courseItems = (ArrayList<CourseItem>) retVal;
                    if (courseItems.isEmpty()) System.out.println("User has no ccourses");
                    for (CourseItem courseItem : courseItems) {
                        System.out.println("Populating course for user: " + courseItem.getId());
                        user.addScheduleItem(courseItem);
                    }
                    user.writeToDisk(getApplicationContext());
                    onReadyStartHome(1);
                }
            }
        });
        getCoursesTask.execute((Void) null);
    }

    private void onReadyStartHome(int ready) {
        switch (ready) {
            case 1:
                // course ready
                courseReady = true;
                loaderProgress += 26;
                loader.setProgress(loaderProgress);
                break;
            case 2:
                // friend ready
                friendReady = true;
                loaderProgress += 22;
                loader.setProgress(loaderProgress);
                break;
            case 3:
                // groups ready
                groupReady = true;
                loaderProgress += 22;
                loader.setProgress(loaderProgress);
                break;
        }
        if (courseReady && friendReady && groupReady) startHome(user);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
