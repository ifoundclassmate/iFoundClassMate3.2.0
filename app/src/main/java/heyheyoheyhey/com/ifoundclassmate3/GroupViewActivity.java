package heyheyoheyhey.com.ifoundclassmate3;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class GroupViewActivity extends ActionBarActivity {
    Group mGroup;
    final static String INVITE_USER_LIST_ITEM = " + Click here to INVITE Others! ";
    final static String CREATE_MEETING_LIST_ITEM = " + Click here to CREATE Meeting!";

    protected User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);

        mGroup = getIntent().getParcelableExtra(HomeActivity.GroupFragment.VIEW_GROUP);

        // set views
        TextView lblGroupName = (TextView) findViewById(R.id.lblGroupName);
        lblGroupName.setText(mGroup.getId());

        TextView lblGroupDesc = (TextView) findViewById(R.id.lblGroupDesc);
        lblGroupDesc.setText(mGroup.getDescription());

        ListView memberListView = (ListView) findViewById(R.id.memberListView);
        ArrayList<String> listItems = new ArrayList<>();
        listItems.addAll(mGroup.getUsers());
        listItems.add(INVITE_USER_LIST_ITEM);
        TextViewListAdapter adapter = new TextViewListAdapter(getApplicationContext(), R.layout.adapter_group_list, listItems);
        memberListView.setAdapter(adapter);

        ListView meetingListView = (ListView) findViewById(R.id.meetingListView);
        ArrayList<String> meetingListItems = new ArrayList<>();
        for (MeetingItem meeting : mGroup.getMeetings()) {
            meetingListItems.add(meeting.getDisplayString());
        }
        meetingListItems.add(CREATE_MEETING_LIST_ITEM);
        TextViewListAdapter memberAdapter = new TextViewListAdapter(getApplicationContext(), R.layout.adapter_group_list, meetingListItems);
        meetingListView.setAdapter(memberAdapter);
        meetingListView.setOnItemClickListener(new MeetingOnItemClickListener());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_view, menu);
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
    public class MeetingOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (parent.getItemAtPosition(position).equals(CREATE_MEETING_LIST_ITEM)) {
                user = getIntent().getParcelableExtra("USER");
                Intent intent = new Intent(GroupViewActivity.this, CreateMeetingActivity.class);
                intent.putExtra("USER", user);
                startActivityForResult(intent, 1);
            }
        }
    }
}
