package heyheyoheyhey.com.ifoundclassmate3;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

import heyheyoheyhey.com.ifoundclassmate3.support.ProjectUtils;

//Represents a day schedule view
public class DayActivity extends ActionBarActivity {

    private static int day;
    private static int month;
    private static int year;
    protected static User user;
    protected static final int DEFAULT_PAGE_INDEX = 10;
    protected static int function;
    protected ArrayList<ScheduleItem> memberScheduleItems;

    public final static String SCHEDULE_FUNCTION = "SCHEDULE_FUNCTION";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());



        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(DEFAULT_PAGE_INDEX);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(mViewPager);
        tabs.setScrollOffset(50);

        Intent intent = getIntent();
        function = intent.getIntExtra(SCHEDULE_FUNCTION, 0);
        day = intent.getIntExtra(HomeActivity.CalendarFragment.SCHEDULE_DAY, 0);
        month = intent.getIntExtra(HomeActivity.CalendarFragment.SCHEDULE_MONTH, 0);
        year = intent.getIntExtra(HomeActivity.CalendarFragment.SCHEDULE_YEAR, 0);
        user = intent.getParcelableExtra(MainActivity.USER_MESSAGE);
        if (function == 1) {
            // group meeting creation
            memberScheduleItems = new ArrayList<>();
            // TODO: Task to get group member's schedule...
        }
        System.out.println("Received date: " + day + month + year);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_day, menu);
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

    public String getDateString() {
        String date;
        String monthString;
        switch(month) {
            case 0:
                monthString = "January";
                break;
            case 1:
                monthString = "Febuary";
                break;
            case 2:
                monthString = "March";
                break;
            case 3:
                monthString = "April";
                break;
            case 4:
                monthString = "May";
                break;
            case 5:
                monthString = "June";
                break;
            case 6:
                monthString = "July";
                break;
            case 7:
                monthString = "August";
                break;
            case 8:
                monthString = "September";
                break;
            case 9:
                monthString = "October";
                break;
            case 10:
                monthString = "November";
                break;
            case 11:
                monthString = "December";
                break;
            default:
                monthString = "WTF";
                break;
        }
        date = monthString + " " + day + ", " + year;

        return date;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            int sectionNumber = position - DEFAULT_PAGE_INDEX + 1;
            return DayScheduleFragment.newInstance(sectionNumber);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 20;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.MONTH, month);
            int sectionNumber = position - DEFAULT_PAGE_INDEX + 1;
            if (sectionNumber != 1) calendar.add(Calendar.DATE, sectionNumber - 1);
            String cMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
            int mDay = calendar.get(Calendar.DATE);
            return (cMonth + " " + mDay).toUpperCase(l);
            //return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DayScheduleFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static String test;
        private static String cDayOfWeek;
        private static String cMonth;
        private static int mDay, mMonth, mYear;
        private static long myTime;

        private static int meetingStartTimeHours, meetingStartTimeMins, meetingEndTimeHours, meetingEndTimeMins;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        /*public DayScheduleFragment(int sectionNumber) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            if (sectionNumber != 1) calendar.add(Calendar.DATE, sectionNumber - 1);
            cDayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.CANADA);
            cMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.CANADA);
            cDate = calendar.getDisplayName(Calendar.DAY_OF_MONTH, Calendar.LONG, Locale.CANADA);
            cYear = calendar.getDisplayName(Calendar.YEAR, Calendar.LONG, Locale.CANADA);
            test = cDayOfWeek + " " + cMonth + " " + cDate + ", " + cYear;
            mDay = calendar.get(Calendar.DATE);
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
        }*/
        public static DayScheduleFragment newInstance(int sectionNumber) {
            DayScheduleFragment fragment = new DayScheduleFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.MONTH, month);
            if (sectionNumber != 1) calendar.add(Calendar.DATE, sectionNumber - 1);
            cDayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
            cMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
            mDay = calendar.get(Calendar.DATE);
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            test = cDayOfWeek + " " + cMonth + " " + mDay + ", " + mYear;
            myTime = calendar.getTime().getTime();
            return fragment;
        }

        public DayScheduleFragment() {
            Bundle bundle = getArguments();
            if (bundle != null) {
                int sectionNumber = bundle.getInt(ARG_SECTION_NUMBER, -1);
                if (sectionNumber != -1) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    calendar.set(Calendar.MONTH, month);
                    if (sectionNumber != 1) calendar.add(Calendar.DATE, sectionNumber - 1);
                    cDayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
                    cMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
                    mDay = calendar.get(Calendar.DATE);
                    mYear = calendar.get(Calendar.YEAR);
                    mMonth = calendar.get(Calendar.MONTH);
                    test = cDayOfWeek + " " + cMonth + " " + mDay + ", " + mYear;
                    myTime = calendar.getTime().getTime();
                }
            }
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle bundle = getArguments();
            if (bundle != null) {
                int sectionNumber = bundle.getInt(ARG_SECTION_NUMBER, -1);
                if (sectionNumber != -1) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    calendar.set(Calendar.MONTH, month);
                    if (sectionNumber != 1) calendar.add(Calendar.DATE, sectionNumber - 1);
                    cDayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
                    cMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
                    mDay = calendar.get(Calendar.DATE);
                    mYear = calendar.get(Calendar.YEAR);
                    mMonth = calendar.get(Calendar.MONTH);
                    test = cDayOfWeek + " " + cMonth + " " + mDay + ", " + mYear;
                    myTime = calendar.getTime().getTime();
                }
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_day, container, false);
            TextView dayText = (TextView) rootView.findViewById(R.id.txtDay);

            dayText.setText(test);

            // TODO: add group schedule to schedule here...


            ArrayList<ScheduleItem.ScheduleTime> scheduleTimes = new ArrayList<>();
            for (ScheduleItem scheduleItem : user.getScheduleItems()) {
                scheduleTimes.addAll(scheduleItem.getScheduleForDay(mDay, mMonth, mYear));
            }
            Collections.sort(scheduleTimes);
            final RelativeLayout relativeLayout = (RelativeLayout) rootView.findViewById(R.id.MondayRL1);
            // final int WIDTH_BETWEEN_HOURS = R.dimen.activity_day_hour_distance;
            final int WIDTH_BETWEEN_HOURS = ProjectUtils.dpToPx(getActivity().getApplicationContext(), 60);
            int dimen = R.dimen.activity_day_hour_distance;
            System.out.println("dimen " + dimen);
            int test = 100; // id buffer
            for (ScheduleItem.ScheduleTime scheduleTime : scheduleTimes) {
                System.out.println("Adding course to schedule: " + scheduleTime.eventName);
                LinearLayout linearLayout = new LinearLayout(getActivity());

                TextView currentScheduleView = render(scheduleTime);
                currentScheduleView.setId(currentScheduleView.getId() + test);
                currentScheduleView.setWidth(400);
                switch (scheduleTime.getType()) {
                    case 1:
                        currentScheduleView.setBackgroundResource(R.drawable.schedule_calendar_block_yellow);
                        break;
                    case 2:
                        currentScheduleView.setBackgroundResource(R.drawable.schedule_course_block_red);
                        break;
                    case 3:
                        currentScheduleView.setBackgroundResource(R.drawable.schedule_group_block_blue);
                        break;
                    default:
                        currentScheduleView.setBackgroundResource(R.drawable.schedule_course_block_red);
                        break;
                }
                currentScheduleView.setTextColor(Color.BLACK);
                currentScheduleView.setHeight(scheduleTime.length * WIDTH_BETWEEN_HOURS / 60);
                //LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                int scale = scheduleTime.startMins;
                int margin = scale * WIDTH_BETWEEN_HOURS / 60;
                //llp.setMargins(0, margin, 0, 0);
                //currentScheduleView.setLayoutParams(llp);
                linearLayout.addView(currentScheduleView);
                test++;
                TextView thisTimeText = (TextView) relativeLayout.getChildAt((int) scheduleTime.startHours - 5);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                lp.addRule(RelativeLayout.ALIGN_TOP, thisTimeText.getId());
                lp.setMargins(0, margin, 0, 0);
                linearLayout.setLayoutParams(lp);
                relativeLayout.addView(linearLayout);
            }
            if (function == 1) {
                // TODO: add steps
                // step 1 - start time
                int step = 1;
                final Button set = (Button) rootView.findViewById(R.id.btnSet);
                final LinearLayout startTime = (LinearLayout) relativeLayout.findViewById(R.id.startTimeDivider);
                final LinearLayout endTime = (LinearLayout) relativeLayout.findViewById(R.id.endTimeDivider);


                relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            final float y = event.getY();
                            relativeLayout.removeView(startTime);
                            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.topMargin = (int) y;
                            startTime.setVisibility(View.VISIBLE);
                            relativeLayout.addView(startTime, layoutParams);
                            set.setVisibility(View.VISIBLE);
                            set.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TextView text = (TextView) startTime.findViewById(R.id.tvStartTimeDivider);
                                    text.setTextColor(Color.GREEN);
                                    final int NUM_HOURS = 18;
                                    boolean found = false;
                                    for (int i = 0; i < NUM_HOURS; i++) {
                                        View view = relativeLayout.getChildAt(i);
                                        System.out.println(view.getTop() + " " + view.getId());
                                        if ((int) y < view.getTop()) {
                                            meetingStartTimeHours = i + 4;
                                            meetingStartTimeMins = (((int) y - 60) % 120) / 2;
                                            found = true;
                                            break;
                                        }
                                    }
                                    if (!found) {
                                        meetingStartTimeHours = NUM_HOURS + 4;
                                        meetingStartTimeMins = (((int) y - 60) % 120) / 2;
                                    }
                                    set.setEnabled(false);

                                    // new listener for end time bar...
                                    relativeLayout.setOnTouchListener(new View.OnTouchListener() {

                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                                final float y = event.getY();
                                                relativeLayout.removeView(endTime);
                                                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                                if ((int) y <= startTime.getTop()) {
                                                    layoutParams.topMargin = startTime.getTop() + 5;
                                                } else {
                                                    layoutParams.topMargin = (int) y;
                                                }
                                                endTime.setVisibility(View.VISIBLE);
                                                relativeLayout.addView(endTime, layoutParams);
                                                set.setEnabled(true);
                                                set.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        TextView text = (TextView) endTime.findViewById(R.id.tvEndTimeDivider);
                                                        text.setTextColor(Color.GREEN);
                                                        final int NUM_HOURS = 18;
                                                        boolean found = false;
                                                        for (int i = 0; i < NUM_HOURS; i++) {
                                                            View view = relativeLayout.getChildAt(i);
                                                            System.out.println(view.getTop() + " " + view.getId());
                                                            if ((int) y < view.getTop()) {
                                                                meetingEndTimeHours = i + 4;
                                                                meetingEndTimeMins = (((int) y - 60) % 120) / 2;
                                                                found = true;
                                                                break;
                                                            }
                                                        }
                                                        if (!found) {
                                                            meetingEndTimeHours = NUM_HOURS + 4;
                                                            meetingEndTimeMins = (((int) y - 60) % 120) / 2;
                                                        }
                                                        set.setEnabled(false);
                                                        Intent intent = new Intent();
                                                        intent.putExtra("SH", meetingStartTimeHours);
                                                        intent.putExtra("SM", meetingStartTimeMins);
                                                        intent.putExtra("EH", meetingEndTimeHours);
                                                        intent.putExtra("EM", meetingEndTimeMins);
                                                        intent.putExtra("T", myTime);
                                                        getActivity().setResult(1, intent);
                                                        getActivity().finish();
                                                    }
                                                });
                                            }
                                            return true;
                                        }
                                    });
                                }
                            });
                        }
                        return true;
                    }
                });
            }

            if (function == 1) {
                // TODO: add steps
                // step 1 - start time
                int step = 1;
                final Button set = (Button) rootView.findViewById(R.id.btnSet);
                final LinearLayout startTime = (LinearLayout) relativeLayout.findViewById(R.id.startTimeDivider);
                final LinearLayout endTime = (LinearLayout) relativeLayout.findViewById(R.id.endTimeDivider);


                relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            final float y = event.getY();
                            relativeLayout.removeView(startTime);
                            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.topMargin = (int) y;
                            startTime.setVisibility(View.VISIBLE);
                            relativeLayout.addView(startTime, layoutParams);
                            set.setVisibility(View.VISIBLE);
                            set.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TextView text = (TextView) startTime.findViewById(R.id.tvStartTimeDivider);
                                    text.setTextColor(Color.GREEN);
                                    final int NUM_HOURS = 18;
                                    boolean found = false;
                                    for (int i = 0; i < NUM_HOURS; i++) {
                                        View view = relativeLayout.getChildAt(i);
                                        System.out.println(view.getTop() + " " + view.getId());
                                        if ((int) y < view.getTop()) {
                                            meetingStartTimeHours = i + 4;
                                            meetingStartTimeMins = (((int) y - 60) % 120) / 2;
                                            found = true;
                                            break;
                                        }
                                    }
                                    if (!found) {
                                        meetingStartTimeHours = NUM_HOURS + 4;
                                        meetingStartTimeMins = (((int) y - 60) % 120) / 2;
                                    }
                                    set.setEnabled(false);

                                    // new listener for end time bar...
                                    relativeLayout.setOnTouchListener(new View.OnTouchListener() {

                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                                final float y = event.getY();
                                                relativeLayout.removeView(endTime);
                                                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                                if ((int) y <= startTime.getTop()) {
                                                    layoutParams.topMargin = startTime.getTop() + 5;
                                                } else {
                                                    layoutParams.topMargin = (int) y;
                                                }
                                                endTime.setVisibility(View.VISIBLE);
                                                relativeLayout.addView(endTime, layoutParams);
                                                set.setEnabled(true);
                                                set.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        TextView text = (TextView) endTime.findViewById(R.id.tvEndTimeDivider);
                                                        text.setTextColor(Color.GREEN);
                                                        final int NUM_HOURS = 18;
                                                        boolean found = false;
                                                        for (int i = 0; i < NUM_HOURS; i++) {
                                                            View view = relativeLayout.getChildAt(i);
                                                            System.out.println(view.getTop() + " " + view.getId());
                                                            if ((int) y < view.getTop()) {
                                                                meetingEndTimeHours = i + 4;
                                                                meetingEndTimeMins = (((int) y - 60) % 120) / 2;
                                                                found = true;
                                                                break;
                                                            }
                                                        }
                                                        if (!found) {
                                                            meetingEndTimeHours = NUM_HOURS + 4;
                                                            meetingEndTimeMins = (((int) y - 60) % 120) / 2;
                                                        }
                                                        set.setEnabled(false);
                                                        Intent intent = new Intent();
                                                        intent.putExtra("SH", meetingStartTimeHours);
                                                        intent.putExtra("SM", meetingStartTimeMins);
                                                        intent.putExtra("EH", meetingEndTimeHours);
                                                        intent.putExtra("EM", meetingEndTimeMins);
                                                        intent.putExtra("T", myTime);
                                                        getActivity().setResult(1, intent);
                                                        getActivity().finish();
                                                    }
                                                });
                                            }
                                            return true;
                                        }
                                    });
                                }
                            });
                        }
                        return true;
                    }
                });
            }

            return rootView;
        }

        private TextView render(ScheduleItem.ScheduleTime scheduleTime) {
            TextView retVal = new TextView(getActivity().getApplicationContext());
            String textViewText = "";
            if (scheduleTime.eventName != null) textViewText += scheduleTime.eventName + "\n";
            if (scheduleTime.description != null) textViewText += scheduleTime.description + "\n";
            int startTimeHours = scheduleTime.startHours;
            String startTimeMins;
            if (scheduleTime.startMins < 10) startTimeMins = "0" + Integer.toString(scheduleTime.startMins);
            else startTimeMins = Integer.toString(scheduleTime.startMins);
            int endTimeHours = scheduleTime.endHours;
            String endTimeMins;
            if (scheduleTime.endMins < 10) endTimeMins = "0" + Integer.toString(scheduleTime.endMins);
            else endTimeMins = Integer.toString(scheduleTime.endMins);

            String startTimeString;
            String endTimeString;
            if (startTimeHours == 12) {
                startTimeString = startTimeHours + ":" + startTimeMins + " pm";
            } else if (startTimeHours > 12) {
                startTimeHours -= 12;
                startTimeString = startTimeHours + ":" + startTimeMins + " pm";
            }
            else startTimeString = startTimeHours + ":" + startTimeMins + " am";

            if (endTimeHours == 12) {
                endTimeString = endTimeHours + ":" + endTimeMins + " pm";
            } else if (endTimeHours > 12) {
                endTimeHours -= 12;
                endTimeString = endTimeHours + ":" + endTimeMins + " pm";
            }
            else endTimeString = endTimeHours + ":" + endTimeMins + " am";

            retVal.setText(textViewText + startTimeString + " - " + endTimeString);
            return retVal;
        }

    }

}
