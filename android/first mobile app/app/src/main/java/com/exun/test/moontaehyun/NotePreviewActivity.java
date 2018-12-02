package com.exun.test.moontaehyun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.applandeo.materialcalendarview.EventDay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotePreviewActivity extends AppCompatActivity {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_preview_activity);
        Intent intent = getIntent();
        TextView note = (TextView) findViewById(R.id.note);
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (intent != null) {
            Object event = intent.getParcelableExtra(CalendarActivity.EVENT);
            if(event instanceof MyEventDay){
                MyEventDay myEventDay = (MyEventDay)event;
                getSupportActionBar().setTitle(getFormattedDate(myEventDay.getCalendar().getTime()));
                note.setText(myEventDay.getNote());
                return;
            }
            if(event instanceof EventDay){
                EventDay eventDay = (EventDay)event;
                getSupportActionBar().setTitle(getFormattedDate(eventDay.getCalendar().getTime()));
            }
        }
    }
    public static String getFormattedDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy", Locale.getDefault());
        return simpleDateFormat.format(date);
    }
}