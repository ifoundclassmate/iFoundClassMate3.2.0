package heyheyoheyhey.com.ifoundclassmate3;

import heyheyoheyhey.com.ifoundclassmate3.support.ServerFunction;
import heyheyoheyhey.com.ifoundclassmate3.support.ServerUtils;
import heyheyoheyhey.com.ifoundclassmate3.util.*;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.content.Intent;

import java.util.ArrayList;


public class AddFriendActivity extends ActionBarActivity {
    private User user;
    private EditText searchEmail;
    private Button searchBtn;
    private ListView friendList;
    private TextViewListAdapter adapter;
    private ArrayList<String> friends;
    private String InputEmail;
    private Boolean FoundFriend;
    private int iCount;
    protected static final int STOP = 0x10000;
    protected static final int NEXT = 0x10001;
    private boolean debug = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        Intent intent = getIntent();
        user = intent.getParcelableExtra("User");
        Parcelable[] originallist = intent.getParcelableArrayExtra("friendlist");
        searchEmail = (EditText)findViewById(R.id.searchFriendText);
        searchBtn = (Button)findViewById(R.id.searchFriendBtn);
        friendList = (ListView)findViewById(R.id.searchFriendList);
        getFriendList(originallist);
        this.adapter = new TextViewListAdapter(getApplicationContext(), R.layout.adapter_group_list, friends);
        adapter.setColor(Color.GRAY);
        friendList.setAdapter(this.adapter);
        friendList.setOnItemClickListener(new friendItemListener());


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputEmail = searchEmail.getText().toString();
                //TODO CALL THE FUNCTION
                /*
                ServerFunction addFriendTask = new ServerFunction(ServerUtils.TASK_ADD_FRIEND);
                addFriendTask.setUser(user);
                addFriendTask.setFriendToAdd(searchEmail.getText().toString());
                addFriendTask.execute((Void) null );
                //TODO CALL DAN , DO WE NEED TO WAIT FOR EXEC OF ADDFRIENDTASK?
                //TODO CALL DAN , TEST IF ADD SUCCEED
                ServerFunction getFriendsTask = new ServerFunction(ServerUtils.TASK_RETRIEVE_FRIENDS);
                getFriendsTask.setUser(user);
                getFriendsTask.setListener(new ServerFunction.ServerTaskListener() {
                    @Override
                    public void onPostExecuteConcluded(boolean result, Object retVal) {
                        if (result) {
                            System.out.println("FRIEND ACTIVITY: Server returned friends...");
                            ArrayList<String> friends = (ArrayList<String>) retVal;
                            if (friends.isEmpty()) System.out.println("User has no friends");
                            for (String friend : friends) {
                                System.out.println("FRIEND ACTIVITY: Populating friend for user: " + friend);
                                user.addFriend(friend);
                            }
                        }
                    }
                });
                getFriendsTask.execute((Void) null);
                */

                friends.add(0, InputEmail);
                adapter.notifyDataSetChanged();
            }
        });
    }
    public class friendItemListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("FRIEND ITEM id", id+"");
            Log.d("FRIEND ITEM POS", position+"");
            String cur_id = friends.get(position);
            String cur_name = "Name";
            //TODO CALL DAN, WE NEED THE FUCNTION TO GET THE COURSES AND NAME FOR A FRIEND
            ArrayList<String> cur_courses = new ArrayList<String>();
            cur_courses.add("CS336");
            cur_courses.add("STAT295");
            Intent mintent = new Intent(AddFriendActivity.this, FriendProfileActivity.class);
            mintent.putExtra("User",user);
            mintent.putExtra("id",cur_id);
            mintent.putExtra("name",cur_name);
            mintent.putStringArrayListExtra("courses",cur_courses);
            startActivity(mintent);
        }
    }
    private void getFriendList(Parcelable[] originallist){
        friends = new ArrayList<String>();
        int sizes = 10;
        Friend[] tmp = new Friend[sizes];
        if(!debug) {
             System.arraycopy(originallist, 0, tmp, 0, originallist.length);
        }
        else{
            for(int i = 0; i < tmp.length; i++)
                tmp[i] = new Friend("Tommy "+i);
        }
        for(int i = 0; i < tmp.length; i++)
            friends.add(tmp[i].getId());

        if(friends.size() == 0)
            friendList.setEnabled(false);
        else
            friendList.setEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_friend, menu);
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
