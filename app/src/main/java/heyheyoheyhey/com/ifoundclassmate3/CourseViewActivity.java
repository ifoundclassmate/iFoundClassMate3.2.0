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


public class CourseViewActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view);

        final CourseItem courseItem = getIntent().getParcelableExtra(AddCourseActivity.COURSEITEM_MESSAGE);
        TextView tvCourseId = (TextView) findViewById(R.id.tvCourse);
        tvCourseId.setText(courseItem.getId());

        TextView tvSection = (TextView) findViewById(R.id.tvSection);
        tvSection.setText(courseItem.getSection());

        TextView tvStartTime = (TextView) findViewById(R.id.tvStartTime);
        String startTimeMins;
        if (courseItem.getStartMins() < 10) startTimeMins = "0" + courseItem.getStartMins();
        else startTimeMins = Integer.toString(courseItem.getStartMins());
        tvStartTime.setText(Integer.toString(courseItem.getStartHours()) + ":" + startTimeMins);

        TextView tvEndTime = (TextView) findViewById(R.id.tvEndTime);
        String endTimeMins;
        if (courseItem.getEndMins() < 10) endTimeMins = "0" + courseItem.getEndMins();
        else endTimeMins = Integer.toString(courseItem.getEndMins());
        tvEndTime.setText(Integer.toString(courseItem.getEndHours()) + ":" + endTimeMins);

        Button btnAdd = (Button) findViewById(R.id.btnAddOrRemove);
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
