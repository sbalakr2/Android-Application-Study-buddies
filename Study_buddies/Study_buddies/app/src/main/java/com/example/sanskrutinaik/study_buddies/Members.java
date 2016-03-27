package com.example.sanskrutinaik.study_buddies;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class Members extends ActionBarActivity {
    StudyGroupDB sb = new StudyGroupDB(this, null, null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);

        final long groupId = getIntent().getLongExtra("GroupId", 0);
        ArrayList<String> members = sb.getMemebers(groupId);

        ListView memberList = (ListView) findViewById(R.id.member_list);

        memberList.setAdapter(null);
        ArrayAdapter listAdapter = new ArrayAdapter(Members.this,R.layout.detail_member_row,R.id.member_name,members);
        memberList.setAdapter(listAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_members, menu);
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
