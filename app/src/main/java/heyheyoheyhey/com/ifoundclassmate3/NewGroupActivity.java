package heyheyoheyhey.com.ifoundclassmate3;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class NewGroupActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        Button btnCreate = (Button) findViewById(R.id.btnCreate);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        final EditText txtId = (EditText) findViewById(R.id.txtId);
        final EditText txtDescription = (EditText) findViewById(R.id.txtDescription);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String id = txtId.getText().toString();
                String desc = txtDescription.getText().toString();
                Group group = new Group(id, desc);


                Intent intent = new Intent();
                intent.putExtra(HomeActivity.GroupFragment.NEW_GROUP, group);
                setResult(1, intent);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(0, intent);
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_group, menu);
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
