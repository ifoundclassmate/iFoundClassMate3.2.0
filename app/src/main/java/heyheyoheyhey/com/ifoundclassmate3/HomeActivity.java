package heyheyoheyhey.com.ifoundclassmate3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import heyheyoheyhey.com.ifoundclassmate3.activities.SplashActivity;
import heyheyoheyhey.com.ifoundclassmate3.support.ProjectUtils;
import heyheyoheyhey.com.ifoundclassmate3.support.ServerFunction;
import heyheyoheyhey.com.ifoundclassmate3.support.ServerUtils;
import heyheyoheyhey.com.ifoundclassmate3.util.Friend;


public class HomeActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {


    public static final String COURSE_ITEM = "COURSE_ITEM";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    protected static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get user
        Intent i = getIntent();
        user = i.getParcelableExtra(MainActivity.USER_MESSAGE);


        setContentView(R.layout.activity_home);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch(position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, CalendarFragment.newInstance(position + 1))
                        .commit();
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, CourseFragment.newInstance(position + 1))
                        .commit();
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, GroupFragment.newInstance(position + 1))
                        .commit();
                break;
            case 3:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, ClassmateFragment.newInstance(position + 1))
                        .commit();
                break;

        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.schedule_section);
                break;
            case 2:
                mTitle = getString(R.string.fragment_course_section_title);
                break;
            case 3:
                mTitle = getString(R.string.group_section);
                break;
            case 4:
                mTitle = getString(R.string.fragment_classmate_section_title);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.home, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

    public static class CalendarFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static final String SCHEDULE_DAY = "SCHEDULE_DAY";
        public static final String SCHEDULE_MONTH = "SCHEDULE_MONTH";
        public static final String SCHEDULE_YEAR = "SCHEDULE_YEAR";
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static CalendarFragment newInstance(int sectionNumber) {
            CalendarFragment fragment = new CalendarFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public CalendarFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
            final CalendarView calendarView=(CalendarView) rootView.findViewById(R.id.calendarView);
            final Button btnGetSchedule = (Button) rootView.findViewById(R.id.btnGetSchedule);
            btnGetSchedule.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    Calendar temp = Calendar.getInstance();
                    temp.setTimeInMillis(calendarView.getDate());

                    Intent intent = new Intent(getActivity(), DayActivity.class);
                    intent.putExtra(CalendarFragment.SCHEDULE_DAY, temp.get(Calendar.DAY_OF_MONTH));
                    intent.putExtra(CalendarFragment.SCHEDULE_MONTH, temp.get(Calendar.MONTH));
                    intent.putExtra(CalendarFragment.SCHEDULE_YEAR, temp.get(Calendar.YEAR));
                    intent.putExtra(MainActivity.USER_MESSAGE, user);
                    startActivity(intent);
                }
            });
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((HomeActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public static class GroupFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static final String NEW_GROUP = "NEW_GROUP";

        public static final String VIEW_GROUP = "VIEW_GROUP";
        public static final String VIEW_GROUP_USER = "USER";
        private View myView;
        private ArrayList<String> listItems;
        TextViewListAdapter adapter;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static GroupFragment newInstance(int sectionNumber) {
            GroupFragment fragment = new GroupFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public GroupFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_group, container, false);
            this.myView = rootView;
            this.listItems = new ArrayList<>();
            for (Group group : user.getGroups()) listItems.add(group.getId());
            this.adapter = new TextViewListAdapter(getActivity().getApplicationContext(), R.layout.adapter_group_list, listItems);
            final ListView listView = (ListView) rootView.findViewById(R.id.grpListView);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new GroupOnItemClickListener());

            // add button handler
            Button btnNewGroup = (Button) rootView.findViewById(R.id.btnNewGroup);

            btnNewGroup.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO: go to new group activity...
                    Intent intent = new Intent(getActivity(), NewGroupActivity.class);
                    Group newGroup = null;
                    intent.putExtra(NEW_GROUP, newGroup);
                    startActivityForResult(intent, 2);
                }
            });

            updateView();

            return rootView;
        }

        private void updateView() {
            // disables button/listview if no groups found
            final ListView listView = (ListView) myView.findViewById(R.id.grpListView);
            ArrayList<Group> groups = user.getGroups();
            if (groups.isEmpty()) {
                listView.setEnabled(false);
            } else {
                listView.setEnabled(true);
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == 1) {
                // new group added: add group id to list
                Group newGroup = data.getParcelableExtra(NEW_GROUP);

                // TODO: this might change
                newGroup.addUser(user.getUsername());
                user.addToGroup(newGroup);

                if (!ServerUtils.BYPASS_SERVER) {
                    ServerFunction createGroupTask = new ServerFunction(ServerUtils.TASK_CREATE_GROUP);
                    createGroupTask.setGroup(newGroup);
                    createGroupTask.execute((Void) null);

                    ServerFunction addUserToGroupTask = new ServerFunction(ServerUtils.TASK_ADD_USER_TO_GROUP);
                    addUserToGroupTask.setFriendToAdd(user.getUsername());
                    addUserToGroupTask.setGroup(newGroup);
                    addUserToGroupTask.execute((Void) null);
                }

                listItems.add(newGroup.getId());
                adapter.notifyDataSetChanged();
                updateView();
            }

        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((HomeActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        public class GroupOnItemClickListener implements AdapterView.OnItemClickListener {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: go to view group activity...
                String groupId = (String) parent.getItemAtPosition(position);
                for (Group group : user.getGroups()) {
                    if (groupId.equals(group.getId())) {
                        Intent intent = new Intent(getActivity(), GroupViewActivity.class);
                        intent.putExtra(VIEW_GROUP, group);
                        intent.putExtra(VIEW_GROUP_USER, user);
                        startActivity(intent);
                    }
                }
            }
        }
    }

    // classmate
    public static class ClassmateFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private View myView;
        private ArrayList<String> listItems;
        TextViewListAdapter adapter;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ClassmateFragment newInstance(int sectionNumber) {
            ClassmateFragment fragment = new ClassmateFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public ClassmateFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_classmate, container, false);

            // TODO: for now, just populate the listView with friend names, in the future
            // friends should be tied to a course.
            this.listItems = new ArrayList<>();
            for (String friend : user.getFriends()) listItems.add(friend);
            this.adapter = new TextViewListAdapter(getActivity().getApplicationContext(), R.layout.adapter_group_list, listItems);
            final ListView listView = (ListView) rootView.findViewById(R.id.classmateListView);
            listView.setAdapter(adapter);

            Button btnFindClassmate = (Button) rootView.findViewById(R.id.btnFindClassmate);

            btnFindClassmate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO: open find classmate dialog box
                                        ArrayList<Friend> friendlist;
                                        friendlist = new ArrayList<Friend>();
                                        for (String friend : user.getFriends()) friendlist.add(new Friend(friend));
                                        Intent intent = new Intent(getActivity(), AddFriendActivity.class);
                                        intent.putParcelableArrayListExtra("friendlist", friendlist);
                                        intent.putExtra("User",user);
                                        startActivity(intent);
                }
            });

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((HomeActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public static class CourseFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private static final int ADD_COURSE_REQUEST_CODE = 3;
        private static final int REMOVE_COURSE_REQUEST_CODE = 4;
        private ArrayList<String> listItems;
        private TextViewListAdapter adapter;
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static CourseFragment newInstance(int sectionNumber) {
            CourseFragment fragment = new CourseFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public CourseFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_user_courses, container, false);

            // set up list
            ListView courses = (ListView) rootView.findViewById(R.id.courseListView);
            this.listItems = new ArrayList<>();
            for (ScheduleItem scheduleItem : user.getScheduleItems()) {
                if (scheduleItem instanceof CourseItem) {
                    listItems.add(((CourseItem) scheduleItem).getId());
                }
            }

            this.adapter = new TextViewListAdapter(getActivity().getApplicationContext(), R.layout.adapter_group_list, listItems);
            this.adapter.setColor(Color.BLUE);
            courses.setAdapter(adapter);
            courses.setOnItemClickListener(new CourseOnItemClickListener());

            // set up button
            Button btnAddCourse = (Button) rootView.findViewById(R.id.btnAddCourse);
            btnAddCourse.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO: go to new group activity...
                    Intent intent = new Intent(getActivity(), AddCourseActivity.class);
                    startActivityForResult(intent, ADD_COURSE_REQUEST_CODE);
                }
            });
            return rootView;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == ADD_COURSE_REQUEST_CODE) {
                if (resultCode == 1) {
                    // new group added: add group id to list
                    CourseItem courseItem = data.getParcelableExtra(COURSE_ITEM);

                    // update to server
                    if (!ServerUtils.BYPASS_SERVER) {
                        ServerFunction addCourseTask = new ServerFunction(ServerUtils.TASK_ADD_COURSE);
                        addCourseTask.setUser(user);
                        addCourseTask.setCourseItem(courseItem);
                        addCourseTask.execute((Void) null);
                    }

                    // update user locally (on memory/disk)
                    user.addScheduleItem(courseItem);
                    user.writeToDisk(getActivity().getApplicationContext());
                    listItems.add(courseItem.getId());
                    adapter.notifyDataSetChanged();
                }
            } else if (requestCode == REMOVE_COURSE_REQUEST_CODE) {
                if (resultCode == 1) {
                    // course to be removed
                    CourseItem courseItem = data.getParcelableExtra(AddCourseActivity.COURSEITEM_MESSAGE);

                    // update to server
                    if (!ServerUtils.BYPASS_SERVER) {
                        ServerFunction deleteCourseTask = new ServerFunction(ServerUtils.TASK_DELETE_COURSE);
                        deleteCourseTask.setUser(user);
                        deleteCourseTask.setCourseItem(courseItem);
                        deleteCourseTask.execute((Void) null);
                    }

                    // update user locally (on memory/disk)
                    user.removeScheduleItem(courseItem);
                    user.writeToDisk(getActivity().getApplicationContext());
                    listItems.remove(courseItem.getId());
                    adapter.notifyDataSetChanged();
                }
            }


        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((HomeActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        public class CourseOnItemClickListener implements AdapterView.OnItemClickListener {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // start course view...
                final boolean deleteCourse = false;
                Intent intent = new Intent(getActivity(), CourseViewActivity.class);
                for (ScheduleItem scheduleItem : user.getScheduleItems()) {
                    if (scheduleItem instanceof CourseItem && ((CourseItem) scheduleItem).getId().equals(parent.getItemAtPosition(position))) {
                        intent.putExtra(AddCourseActivity.COURSEITEM_MESSAGE, (CourseItem) scheduleItem);
                        intent.putExtra(AddCourseActivity.COURSE_ACTION_MESSAGE, deleteCourse);
                        startActivityForResult(intent, REMOVE_COURSE_REQUEST_CODE);
                    }
                }
            }
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((HomeActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
    public void Change(View view) {
        startActivity(new Intent(HomeActivity.this, SplashActivity.class));
        finish();
    }

}
