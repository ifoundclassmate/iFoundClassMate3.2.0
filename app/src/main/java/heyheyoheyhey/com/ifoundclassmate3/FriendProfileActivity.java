package heyheyoheyhey.com.ifoundclassmate3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class FriendProfileActivity extends ActionBarActivity {
    private String FriendId;
    private String FriendName;
    private ArrayList<String> FriendCourses;
    private TextView showName;
    private TextView showEmail;
    private ListView courseList;
    private TextViewListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        Intent intent = getIntent();
        FriendId = intent.getStringExtra("id");
        FriendName = intent.getStringExtra("name");
        FriendCourses = intent.getStringArrayListExtra("courses");
        showName = (TextView)findViewById(R.id.profileName);
        showEmail = (TextView)findViewById(R.id.profileEmail);
        courseList = (ListView)findViewById(R.id.profileCourses);
        showName.setText(FriendName);
        showEmail.setText(FriendId);
        adapter = new TextViewListAdapter(getApplicationContext(), R.layout.adapter_group_list, FriendCourses);
        adapter.setColor(Color.GRAY);
        courseList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend_profile, menu);
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
