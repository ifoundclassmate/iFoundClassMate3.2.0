package heyheyoheyhey.com.ifoundclassmate3;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.os.Handler;
import android.os.Message;


public class AddFriendActivity extends ActionBarActivity {

    private EditText searchEmail;
    private Button searchBtn;
    private ProgressBar proBar;
    private String InputEmail;
    private Boolean FoundFriend;
    private int iCount;
    protected static final int STOP = 0x10000;
    protected static final int NEXT = 0x10001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        searchEmail = (EditText)findViewById(R.id.searchFriendText);
        searchBtn = (Button)findViewById(R.id.searchFriendBtn);
        proBar = (ProgressBar)findViewById(R.id.searchFriendprobar);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputEmail = searchEmail.getText().toString();
                //TODO CALL THE FUNCTION
                //PROGRESS BAR STARTS WORKING
                Thread mThread = new Thread(new Runnable() {

                    public void run() {

                        for(int i=0 ; i < 20; i++){
                            try{
                                iCount = (i + 1) * 5;
                                Thread.sleep(1000);
                                if(i == 19){
                                    Message msg = new Message();
                                    msg.what = STOP;
                                    mHandler.sendMessage(msg);
                                    break;
                                }else{
                                    Message msg = new Message();
                                    msg.what = NEXT;
                                    mHandler.sendMessage(msg);
                                }
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
                mThread.start();
            }
        });
    }
    public Handler mHandler = new Handler(){

        public void handleMessage(Message msg){
            switch (msg.what) {
                case STOP:
                    proBar.setProgress(100);
                    Thread.currentThread().interrupt();
                    break;
                case NEXT:
                    if(!Thread.currentThread().isInterrupted()){
                        proBar.setProgress(iCount);
                    }
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_friend, menu);
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
