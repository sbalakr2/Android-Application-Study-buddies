package com.example.sanskrutinaik.study_buddies;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.apache.http.impl.cookie.DateParseException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


public class Schedule extends ActionBarActivity {

    EmailNotifier notifier = new EmailNotifier();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        final long groupId = getIntent().getLongExtra("GroupId",0);
        Button reserve = (Button) findViewById(R.id.reserve);
        Button schedule = (Button) findViewById(R.id.scheduleButton);
        final StudyGroupDB db =  new StudyGroupDB(this, null, null,1);
        DatePicker date = (DatePicker) findViewById(R.id.scheduleAMeetDate);

        date.setMinDate(System.currentTimeMillis() - 1000);

        reserve.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(
                        Schedule.this,
                        WebView.class);
                startActivity(intent);
            }
        });

        schedule.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatePicker date = (DatePicker) findViewById(R.id.scheduleAMeetDate);
                int day = date.getDayOfMonth();
                int month = date.getMonth();
                int year = date.getYear();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year,month,day);
                final String meetingDate = calendar.getTime().toString();
                ArrayList<String> members = db.getMemebers(groupId);
                for (int i = 0; i < members.size(); i++)
                {
                    String member = members.get(i);
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("username", member);
                    query.getFirstInBackground(new GetCallback<ParseUser>() {
                        public void done(ParseUser user, ParseException e) {
                            if (e == null) {
                                String email = user.getEmail().toString();
                                MimeMessage message = null;
                                try {
                                    message = notifier.prepareMessage(email,"A Meeting is Scheduled for Study Group "+ db.getGroupObject(groupId).getGroupName() +" on " + meetingDate,"Meeting Scheduled");
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                } catch (MessagingException e1) {
                                    e1.printStackTrace();
                                }
                                new EmailNotifier().execute(message);
                            }

                        }
                    });

                }

                Toast.makeText(getApplicationContext(),
                        "Meeting Scheduled", Toast.LENGTH_LONG)
                        .show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
