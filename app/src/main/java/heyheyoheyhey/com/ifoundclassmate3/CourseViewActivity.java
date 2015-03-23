package heyheyoheyhey.com.ifoundclassmate3;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;

import heyheyoheyhey.com.ifoundclassmate3.support.ServerFunction;
import heyheyoheyhey.com.ifoundclassmate3.support.ServerUtils;

// CourseViewActivity: Generic activity for viewing details of a course.
// PRE: Intent extras:
// * AddCourseActivity.COURSEITEM_MESSAGE: CourseItem
// * AddCourseActivity.COURSE_ACTION_MESSAGE: boolean
public class CourseViewActivity extends ActionBarActivity {

    protected static CourseItem courseItem;
    protected static boolean addOrRemove;

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view);

        // Get the CourseItem to display
        Intent intent = getIntent();
        courseItem = intent.getParcelableExtra(AddCourseActivity.COURSEITEM_MESSAGE);
        addOrRemove = intent.getBooleanExtra(AddCourseActivity.COURSE_ACTION_MESSAGE, true);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.courseViewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.courseViewTabs);
        tabs.setViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_course_view, menu);
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

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position) {
                case 0:
                    return CourseViewFragment.newInstance(position);
                case 1:
                    return UsersRegisteredFragment.newInstance(position);
            }
            return CourseViewFragment.newInstance(position);

        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return courseItem.getId() + " Details";
                case 1:
                    return "Users Registered";
            }
            return "Users Registered";
        }
    }

    public static class CourseViewFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        public static CourseViewFragment newInstance(int sectionNumber) {
            CourseViewFragment fragment = new CourseViewFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_course_view, container, false);
            TextView tvCourseId = (TextView) rootView.findViewById(R.id.tvCourse);
            tvCourseId.setText(courseItem.getId());

            TextView tvSection = (TextView) rootView.findViewById(R.id.tvSection);
            tvSection.setText(courseItem.getSection());

            TextView tvStartTime = (TextView) rootView.findViewById(R.id.tvStartTime);
            String startTimeMins;
            if (courseItem.getStartMins() == -1) {
                tvStartTime.setText("N/A");
            } else {
                if (courseItem.getStartMins() < 10) startTimeMins = "0" + courseItem.getStartMins();
                else startTimeMins = Integer.toString(courseItem.getStartMins());
                tvStartTime.setText(Integer.toString(courseItem.getStartHours()) + ":" + startTimeMins);
            }

            TextView tvEndTime = (TextView) rootView.findViewById(R.id.tvEndTime);
            String endTimeMins;
            if (courseItem.getEndMins() == -1) {
                tvEndTime.setText("N/A");
            } else {
                if (courseItem.getEndMins() < 10) endTimeMins = "0" + courseItem.getEndMins();
                else endTimeMins = Integer.toString(courseItem.getEndMins());
                tvEndTime.setText(Integer.toString(courseItem.getEndHours()) + ":" + endTimeMins);
            }

            // Location display text
            TextView tvLocation = (TextView) rootView.findViewById(R.id.tvLocation);
            if (courseItem.getLocation().isEmpty()) tvLocation.setVisibility(View.INVISIBLE);
            else tvLocation.setText(courseItem.getLocation());

            // Set instructors TextView display text.
            // Note, instructors separated by ";" in CourseItem, need to change separator to newline.
            TextView tvInstructors = (TextView) rootView.findViewById(R.id.tvInstructors);
            if (courseItem.getInstructors().isEmpty()) tvInstructors.setVisibility(View.INVISIBLE);
            else {
                String[] instructors = courseItem.getInstructors().split(";");
                String instructorsText = "";
                for (int i = 0; i < instructors.length; i++) {
                    instructorsText += instructors[i] + "\n";
                }
                tvInstructors.setText(instructorsText);
            }

            TextView tvTerm = (TextView) rootView.findViewById(R.id.tvTerm);
            if (courseItem.getTerm().isEmpty()) tvTerm.setVisibility(View.INVISIBLE);
            else tvTerm.setText(courseItem.getTerm());

            TextView tvTitle = (TextView) rootView.findViewById(R.id.tvDescription);
            if (courseItem.getTitle().isEmpty()) tvTitle.setVisibility(View.INVISIBLE);
            else tvTitle.setText(courseItem.getTitle());

            TextView tvDaysOfWeek = (TextView) rootView.findViewById(R.id.tvDaysOfWeek);
            String daysOfWeekText = "";
            boolean[] daysOfWeek = courseItem.getDaysOfWeek();
            int numDays = 0;
            for (int i = 0; i < daysOfWeek.length; i++) {
                if (daysOfWeek[i]) numDays ++;
            }
            final String[] daysOfWeekString = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
            for (int i = 0; i < daysOfWeek.length; i++) {
                if (daysOfWeek[i]) {
                    daysOfWeekText += daysOfWeekString[i];
                    numDays --;
                    if (numDays > 1) daysOfWeekText += ", ";
                    else if (numDays == 1) daysOfWeekText += " and ";
                }
            }
            tvDaysOfWeek.setText(daysOfWeekText);

            // Action Button text should reflect whether the user is adding or removing course.
            Button btnAdd = (Button) rootView.findViewById(R.id.btnAddOrRemove);
            if (addOrRemove) btnAdd.setText("Add to My Courses");
            else btnAdd.setText("Remove from My Courses");
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ret = new Intent();
                    ret.putExtra(AddCourseActivity.COURSEITEM_MESSAGE, courseItem);
                    getActivity().setResult(1, ret);
                    getActivity().finish();
                }
            });
            return rootView;
        }
    }

    public static class UsersRegisteredFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        private ListView listView;
        private TextViewListAdapter adapter;
        private ArrayList<String> listItems = new ArrayList<>();

        public static UsersRegisteredFragment newInstance(int sectionNumber) {
            UsersRegisteredFragment fragment = new UsersRegisteredFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_users_registered, container, false);

            // populate the users registered in the course.
            listView = (ListView) rootView.findViewById(R.id.userEnrolledListView);
            this.adapter = new TextViewListAdapter(getActivity().getApplicationContext(), R.layout.adapter_group_list, listItems);
            this.adapter.setColor(Color.BLUE);
            listView.setAdapter(adapter);

            TextView title = (TextView) rootView.findViewById(R.id.tvUserEnrolled);
            title.setText("Users Registered in " + courseItem.getId() + ": ");

            ServerFunction getUsersTask = new ServerFunction(ServerUtils.TASK_RETRIEVE_USERS_ENROLLED_IN_COURSE);
            getUsersTask.setCourseItem(courseItem);
            getUsersTask.setListener(new ServerFunction.ServerTaskListener() {
                @Override
                public void onPostExecuteConcluded(boolean result, Object retVal) {
                    if (result) {
                        System.out.println("Server returned users...");
                        listItems.addAll((ArrayList<String>) retVal);
                        adapter.notifyDataSetChanged();
                        ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.userEnrolledProgressBar);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            });
            getUsersTask.execute((Void) null);
            return rootView;
        }
    }

}
