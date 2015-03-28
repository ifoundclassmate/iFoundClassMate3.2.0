package heyheyoheyhey.com.ifoundclassmate3;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import java.util.Calendar;


public class CreateMeetingActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_calendar);

        final User tempUser = getIntent().getParcelableExtra("USER");

        final CalendarView calendarView=(CalendarView) findViewById(R.id.calendarView);
        final Button btnGetSchedule = (Button) findViewById(R.id.btnGetSchedule);
        btnGetSchedule.setText("Create Meeting");
        btnGetSchedule.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Calendar temp = Calendar.getInstance();
                temp.setTimeInMillis(calendarView.getDate());

                Intent intent = new Intent(CreateMeetingActivity.this, DayActivity.class);
                intent.putExtra(DayActivity.SCHEDULE_FUNCTION, 1);
                intent.putExtra(HomeActivity.CalendarFragment.SCHEDULE_DAY, temp.get(Calendar.DAY_OF_MONTH));
                intent.putExtra(HomeActivity.CalendarFragment.SCHEDULE_MONTH, temp.get(Calendar.MONTH));
                intent.putExtra(HomeActivity.CalendarFragment.SCHEDULE_YEAR, temp.get(Calendar.YEAR));
                intent.putExtra(MainActivity.USER_MESSAGE, tempUser);
                startActivityForResult(intent, 1);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_meeting, menu);
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
