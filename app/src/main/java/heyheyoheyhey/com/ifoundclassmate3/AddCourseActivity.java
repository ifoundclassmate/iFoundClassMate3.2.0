package heyheyoheyhey.com.ifoundclassmate3;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import heyheyoheyhey.com.ifoundclassmate3.support.ProjectUtils;


public class AddCourseActivity extends ActionBarActivity  {

    public final static String COURSEITEM_MESSAGE = "COURSEITEM_MESSAGE";
    public final static String COURSE_ACTION_MESSAGE = "COURSE_ACTION_MESSAGE";

    protected String mCurrentSubject;
    protected String mCurrentCourse;
    protected ArrayList<String> courseListItems = new ArrayList<>();
    protected TextViewListAdapter courseAdapter;
    protected ArrayList<String> sectionListItems = new ArrayList<>();
    protected TextViewListAdapter sectionAdapter;
    protected ArrayList<CourseItem> courseItems;
    protected ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        courseItems = new ArrayList<>();

        // initiate subject spinner
        Spinner spinner = (Spinner) findViewById(R.id.courseSubjectSpinner);
        ArrayAdapter<CharSequence> subjectAdapter = ArrayAdapter.createFromResource(this, R.array.subjects, android.R.layout.simple_spinner_item);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(subjectAdapter);
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());



        courseAdapter = new TextViewListAdapter(getApplicationContext(), R.layout.adapter_group_list, courseListItems);

        ListView courses = (ListView) findViewById(R.id.addCourseListView);
        this.courseAdapter.setColor(Color.GREEN);
        courses.setAdapter(courseAdapter);
        courses.setOnItemClickListener(new CourseOnItemClickListener());

        sectionAdapter = new TextViewListAdapter(getApplicationContext(), R.layout.adapter_group_list, sectionListItems);
        ListView sections = (ListView) findViewById(R.id.sectionListView);
        this.sectionAdapter.setColor(Color.BLUE);
        sections.setAdapter(sectionAdapter);
        sections.setOnItemClickListener(new SectionOnItemClickListener());

        loader = (ProgressBar) findViewById(R.id.addCourseProgressBar);
        loader.setVisibility(View.INVISIBLE);

    }

    protected void setViewsEnabled(boolean enable) {
        Spinner spinner = (Spinner) findViewById(R.id.courseSubjectSpinner);
        ListView courses = (ListView) findViewById(R.id.addCourseListView);
        ListView sections = (ListView) findViewById(R.id.sectionListView);

        spinner.setEnabled(enable);
        courses.setEnabled(enable);
        sections.setEnabled(enable);
        if (enable) loader.setVisibility(View.INVISIBLE);
        else loader.setVisibility(View.VISIBLE);
    }

    protected void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 1) {
                CourseItem toAdd = data.getParcelableExtra(COURSEITEM_MESSAGE);
                Intent intent = new Intent();
                intent.putExtra(HomeActivity.COURSE_ITEM, toAdd);
                setResult(1, intent);
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_course, menu);
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

    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        protected PopulateCourseTask populateCourseTask;

        public void onItemSelected(AdapterView<?> parent, View view, int pos,
                                   long id) {
            courseListItems.clear();
            sectionListItems.clear();
            courseItems.clear();
            sectionAdapter.notifyDataSetChanged();
            setViewsEnabled(false);

            String subject = (String) parent.getItemAtPosition(pos);
            mCurrentSubject = new String(subject);
            System.out.println(subject);

            populateCourseTask = new PopulateCourseTask(subject);
            populateCourseTask.execute((Void) null);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }

        public class PopulateCourseTask extends AsyncTask<Void, Void, Boolean> {

            private final String mSubject;

            PopulateCourseTask(String subject) {
                this.mSubject = subject;
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                URL url;
                HttpURLConnection connection = null;
                InputStream is = null;
                String catalog_number;
                try{
                    url = new URL("https://api.uwaterloo.ca/v2/courses/" + mSubject + ".json?key=d580b4f96aa6be2d53e8eb2aeb576c8e");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    is = connection.getInputStream();
                    BufferedReader theReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String str = "";
                    String reply;
                    while ((reply = theReader.readLine()) != null){
                        str += reply;
                    }
                    JSONObject obj = new JSONObject(str);
                    for(int i = 0; i < obj.getJSONArray("data").length(); i++){
                        JSONObject res = obj.getJSONArray("data").getJSONObject(i);
                        catalog_number = (String) res.get("catalog_number");
                        System.out.println(catalog_number);
                        courseListItems.add(catalog_number);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }

                return true;
            }

            @Override
            protected void onPostExecute(final Boolean success) {
                populateCourseTask = null;
                setViewsEnabled(true);
                if (success) {
                    courseAdapter.notifyDataSetChanged();
                } else {
                    System.out.println("ERROR RETRIEVING API...");
                }
            }

            @Override
            protected void onCancelled() {
                populateCourseTask = null;
                setViewsEnabled(true);
            }
        }

    }

    public class CourseOnItemClickListener implements AdapterView.OnItemClickListener {

        protected PopulateSectionTask populateSectionTask;

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            view.setSelected(true);
            sectionListItems.clear();
            setViewsEnabled(false);
            String course = (String) parent.getItemAtPosition(position);
            mCurrentCourse = new String(course);

            populateSectionTask = new PopulateSectionTask(mCurrentSubject, course);
            populateSectionTask.execute();
        }

        public class PopulateSectionTask extends AsyncTask<Void, Void, Boolean> {

            private String mSubject;
            private String mCatalogue;

            public PopulateSectionTask(String subject, String catalogue) {
                mSubject = subject;
                mCatalogue = catalogue;
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                URL url;
                HttpURLConnection connection = null;
                InputStream is = null;
                String section;
                courseItems = new ArrayList<>();
                try{
                    url = new URL("https://api.uwaterloo.ca/v2/courses/" + mSubject + "/" + mCatalogue + "/schedule.json?key=d580b4f96aa6be2d53e8eb2aeb576c8e");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    is = connection.getInputStream();
                    BufferedReader theReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String str = "";
                    String reply;
                    while ((reply = theReader.readLine()) != null){
                        str += reply;
                    }
                    JSONObject obj = new JSONObject(str);
                    for(int i = 0; i < obj.getJSONArray("data").length(); i++){
                        JSONObject res = obj.getJSONArray("data").getJSONObject(i);
                        section = (String) res.get("section");
                        System.out.println(section);
                        sectionListItems.add(section);

                        JSONObject schedule = res.getJSONArray("classes").getJSONObject(0);
                        System.out.println(schedule.toString());
                        JSONObject dates = schedule.getJSONObject("date");
                        int startTimeHours = -1;
                        int startTimeMins = -1;
                        if (!dates.isNull("start_time")) {
                            String startTime = (String) dates.get("start_time");
                            String[] test = startTime.split(":");
                            startTimeHours = Integer.parseInt(test[0]);
                            startTimeMins = Integer.parseInt(test[1]);
                        }

                        int endTimeHours = -1;
                        int endTimeMins = -1;
                        if (!dates.isNull("end_time")) {
                            String endTime = (String) dates.get("end_time");
                            String[] endSplit = endTime.split(":");
                            endTimeHours = Integer.parseInt(endSplit[0]);
                            endTimeMins = Integer.parseInt(endSplit[1]);
                        }

                        Date start = new Date();
                        Date end = new Date();
                        Calendar calendarStart = Calendar.getInstance();
                        Calendar calendarEnd = Calendar.getInstance();
                        Calendar calendarNow = Calendar.getInstance();
                        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
                        calendarEnd.set(Calendar.HOUR_OF_DAY, 0);

                        if (!dates.isNull("start_date")) {
                            String[] startDates = ((String) dates.get("start_date")).split("/");
                            calendarStart.set(Calendar.MONTH, Integer.parseInt(startDates[0]));
                            calendarStart.set(Calendar.DAY_OF_MONTH, Integer.parseInt(startDates[1]));
                        } else {
                            if (calendarNow.get(Calendar.MONTH) <= 3) {
                                //TODO: hardcoded start/end
                                calendarStart.set(Calendar.MONTH, 0);
                                calendarStart.set(Calendar.DAY_OF_MONTH, 1);
                            }
                        }
                        start = calendarStart.getTime();
                        if (!dates.isNull("end_date")) {
                            String[] endDates = ((String) dates.get("end_date")).split("/");
                            calendarEnd.set(Calendar.MONTH, Integer.parseInt(endDates[0]));
                            calendarEnd.set(Calendar.DAY_OF_MONTH, Integer.parseInt(endDates[1]));
                        } else {
                            if (calendarNow.get(Calendar.MONTH) <= 3) {
                                //TODO: hardcoded start/end
                                calendarEnd.set(Calendar.MONTH, 3);
                                calendarEnd.set(Calendar.DAY_OF_MONTH, 10);
                            }
                        }
                        end = calendarEnd.getTime();

                        boolean daysOfWeek[] = new boolean[7];
                        if (!dates.isNull("weekdays")) {
                            String weekdays = (String) dates.get("weekdays");
                            if (weekdays.contains("M")) daysOfWeek[1] = true;
                            if (weekdays.contains("T") && (weekdays.indexOf("T") != weekdays.indexOf("Th")))
                                daysOfWeek[2] = true;
                            if (weekdays.contains("W")) daysOfWeek[3] = true;
                            if (weekdays.contains("Th")) daysOfWeek[4] = true;
                            if (weekdays.contains("F")) daysOfWeek[5] = true;
                            if (weekdays.contains("S")) daysOfWeek[6] = true;
                        }
                        CourseItem courseItem = new CourseItem(mSubject + " " + mCatalogue, section, start, end, daysOfWeek, startTimeHours, startTimeMins, endTimeHours, endTimeMins);
                        System.out.println("Course: " + courseItem.getId());

                        if (!res.isNull("title")) {
                            String cTitle = (String) res.get("title");
                            courseItem.setTitle(cTitle);
                        }

                        if (!schedule.isNull("location")) {
                            JSONObject location = schedule.getJSONObject("location");
                            if (!location.isNull("building") && !location.isNull("room")) {
                                String cLocation = ((String) location.get("building")) + " " + ((String) location.get("room"));
                                courseItem.setLocation(cLocation);
                            }
                        }

                        if (!schedule.isNull("instructors")) {
                            JSONArray instrArr = schedule.getJSONArray("instructors");
                            String cInstructors = "";
                            for (int j = 0; j < instrArr.length(); j++) {
                                if (instrArr.isNull(j)) break;
                                else {
                                    String[] nameSplit = ((String) instrArr.get(j)).split(",");
                                    cInstructors += nameSplit[0] + ", " + nameSplit[1] + ";";
                                    System.out.println("oheyhey;".split(";").length);
                                }
                            }
                            courseItem.setInstructors(cInstructors);
                        }

                        if (!res.isNull("term")) {
                            int term = res.getInt("term");
                            String cTerm = ProjectUtils.termIdToName(term);
                            courseItem.setTerm(cTerm);
                        }
                        courseItems.add(courseItem);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }

                return true;
            }

            @Override
            protected void onPostExecute(final Boolean success) {
                populateSectionTask = null;
                setViewsEnabled(true);
                if (success) {
                    sectionAdapter.notifyDataSetChanged();
                    if (sectionListItems.isEmpty()) {
                        showMessage("No sections found for " + mSubject + " " + mCatalogue);
                    }
                } else {
                    System.out.println("ERROR RETRIEVING SCHEDULE API...");
                }
            }

            @Override
            protected void onCancelled() {
                populateSectionTask = null;
                setViewsEnabled(true);
            }
        }
    }

    public class SectionOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // start course view...
            // Note: assume that # of courseItems = # of sectionListItems.
            final boolean addCourse = true;
            Intent intent = new Intent(getApplicationContext(), CourseViewActivity.class);
            intent.putExtra(COURSEITEM_MESSAGE, courseItems.get(position));
            intent.putExtra(COURSE_ACTION_MESSAGE, addCourse);
            startActivityForResult(intent, 1);
        }


    }


}
