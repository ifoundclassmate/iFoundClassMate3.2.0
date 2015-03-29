package heyheyoheyhey.com.ifoundclassmate3;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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
    private ArrayList<MeetingItem> retMeetings;
    private Intent retIntent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);

        mGroup = getIntent().getParcelableExtra(HomeActivity.GroupFragment.VIEW_GROUP);
        user = getIntent().getParcelableExtra(HomeActivity.GroupFragment.VIEW_GROUP_USER);

        setViews();
        retMeetings = new ArrayList<>();

    }

    private void setViews() {
        // set views
        TextView lblGroupName = (TextView) findViewById(R.id.lblGroupName);
        lblGroupName.setText("Group name: " + mGroup.getId());

        TextView lblGroupDesc = (TextView) findViewById(R.id.lblGroupDesc);
        lblGroupDesc.setText("Description: " + mGroup.getDescription());

        LinearLayout memberListView = (LinearLayout) findViewById(R.id.memberListView);
        memberListView.removeAllViews();
        ArrayList<String> listItems = new ArrayList<>();
        listItems.addAll(mGroup.getUsers());
        listItems.add(INVITE_USER_LIST_ITEM);
        TextViewListAdapter adapter = new TextViewListAdapter(getApplicationContext(), R.layout.adapter_group_list, listItems);

        for(int i=0;i<adapter.getCount();i++) {
            View v = adapter.getView(i, null, null);
            memberListView.addView(v);
        }

        LinearLayout meetingListView = (LinearLayout) findViewById(R.id.meetingListView);
        meetingListView.removeAllViews();
        meetingListView.setScrollContainer(false);
        final ArrayList<String> meetingListItems = new ArrayList<>();
        for (MeetingItem meeting : mGroup.getMeetings()) {
            meetingListItems.add(meeting.getDisplayString());
        }
        meetingListItems.add(CREATE_MEETING_LIST_ITEM);
        TextViewListAdapter memberAdapter = new TextViewListAdapter(getApplicationContext(), R.layout.adapter_group_list, meetingListItems);
        for(int i=0;i<memberAdapter.getCount();i++) {
            View v = memberAdapter.getView(i, null, null);
            if (i == memberAdapter.getCount() - 1) {
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MeetingOnItemClickListener meetingOnItemClickListener = new MeetingOnItemClickListener();
                        meetingOnItemClickListener.onItemClick(true);
                    }
                });
            }
            meetingListView.addView(v);
        }
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

    public class MeetingOnItemClickListener  {

        public void onItemClick(boolean addMeeting) {
            if (addMeeting) {
                user = getIntent().getParcelableExtra("USER");
                Intent intent = new Intent(GroupViewActivity.this, CreateMeetingActivity.class);
                intent.putExtra("USER", user);
                startActivityForResult(intent, 1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            MeetingItem meetingItem = data.getParcelableExtra("MI");
            if (meetingItem != null) {
                this.mGroup.addMeetingItem(meetingItem);
                retMeetings.add(meetingItem);
                setViews();
                retIntent.putExtra("M", retMeetings);
                retIntent.putExtra("G", mGroup);
                setResult(1, retIntent);
            }
        }
    }

    @Override
    protected void onDestroy() {
//            if (retMeetings.isEmpty()) {
//                super.onDestroy();
//                return;
//            }
//            Intent retIntent = new Intent();
//            retIntent.putExtra("M", retMeetings);
//            retIntent.putExtra("G", mGroup);
//            setResult(1, retIntent);

        super.onDestroy();
    }
}
