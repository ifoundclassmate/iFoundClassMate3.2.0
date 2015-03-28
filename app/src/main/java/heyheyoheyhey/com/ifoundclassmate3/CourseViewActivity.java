package heyheyoheyhey.com.ifoundclassmate3;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

// CourseViewActivity: Generic activity for viewing details of a course.
// PRE: Intent extras:
// * AddCourseActivity.COURSEITEM_MESSAGE: CourseItem
// * AddCourseActivity.COURSE_ACTION_MESSAGE: boolean
public class CourseViewActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view);

        // Get the CourseItem to display
        Intent intent = getIntent();
        final CourseItem courseItem = intent.getParcelableExtra(AddCourseActivity.COURSEITEM_MESSAGE);
        TextView tvCourseId = (TextView) findViewById(R.id.tvCourse);
        tvCourseId.setText(courseItem.getId());

        TextView tvSection = (TextView) findViewById(R.id.tvSection);
        tvSection.setText(courseItem.getSection());

        TextView tvStartTime = (TextView) findViewById(R.id.tvStartTime);
        String startTimeMins;
        if (courseItem.getStartMins() == -1) {
            tvStartTime.setText("N/A");
        } else {
            if (courseItem.getStartMins() < 10) startTimeMins = "0" + courseItem.getStartMins();
            else startTimeMins = Integer.toString(courseItem.getStartMins());
            tvStartTime.setText(Integer.toString(courseItem.getStartHours()) + ":" + startTimeMins);
        }

        TextView tvEndTime = (TextView) findViewById(R.id.tvEndTime);
        String endTimeMins;
        if (courseItem.getEndMins() == -1) {
            tvEndTime.setText("N/A");
        } else {
            if (courseItem.getEndMins() < 10) endTimeMins = "0" + courseItem.getEndMins();
            else endTimeMins = Integer.toString(courseItem.getEndMins());
            tvEndTime.setText(Integer.toString(courseItem.getEndHours()) + ":" + endTimeMins);
        }

        // Location display text
        TextView tvLocation = (TextView) findViewById(R.id.tvLocation);
        if (courseItem.getLocation().isEmpty()) tvLocation.setVisibility(View.INVISIBLE);
        else tvLocation.setText(courseItem.getLocation());

        // Set instructors TextView display text.
        // Note, instructors separated by ";" in CourseItem, need to change separator to newline.
        TextView tvInstructors = (TextView) findViewById(R.id.tvInstructors);
        if (courseItem.getInstructors().isEmpty()) tvInstructors.setVisibility(View.INVISIBLE);
        else {
            String[] instructors = courseItem.getInstructors().split(";");
            String instructorsText = "";
            for (int i = 0; i < instructors.length; i++) {
                instructorsText += instructors[i] + "\n";
            }
            tvInstructors.setText(instructorsText);
        }

        TextView tvTerm = (TextView) findViewById(R.id.tvTerm);
        if (courseItem.getTerm().isEmpty()) tvTerm.setVisibility(View.INVISIBLE);
        else tvTerm.setText(courseItem.getTerm());

        TextView tvTitle = (TextView) findViewById(R.id.tvDescription);
        if (courseItem.getTitle().isEmpty()) tvTitle.setVisibility(View.INVISIBLE);
        else tvTitle.setText(courseItem.getTitle());

        TextView tvDaysOfWeek = (TextView) findViewById(R.id.tvDaysOfWeek);
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
        boolean addOrRemove = intent.getBooleanExtra(AddCourseActivity.COURSE_ACTION_MESSAGE, true);
        Button btnAdd = (Button) findViewById(R.id.btnAddOrRemove);
        if (addOrRemove) btnAdd.setText("Add to My Courses");
        else btnAdd.setText("Remove from My Courses");
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ret = new Intent();
                ret.putExtra(AddCourseActivity.COURSEITEM_MESSAGE, courseItem);
                setResult(1, ret);
                finish();
            }
        });
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
}
