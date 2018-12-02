package com.exun.test.moontaehyun;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity {
    static public AlarmManager am;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note_activity);
        final CalendarView datePicker = (CalendarView) findViewById(R.id.datePicker);
        Button button = (Button) findViewById(R.id.addNoteButton);
        final EditText noteEditText = (EditText) findViewById(R.id.noteEditText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                MyEventDay myEventDay = new MyEventDay(datePicker.getSelectedDate(),
                        R.drawable.ic_message_black_48dp, noteEditText.getText().toString());


                returnIntent.putExtra(CalendarActivity.RESULT, myEventDay);
                setResult(Activity.RESULT_OK, returnIntent);

                SimpleDateFormat year = new SimpleDateFormat("yyyy", Locale.getDefault());
                String formattedyear = year.format(myEventDay.getCalendar().getTime());
                int complete_year = Integer.parseInt(formattedyear);


                SimpleDateFormat month = new SimpleDateFormat("MM", Locale.getDefault());
                String formattedmonth = month.format(myEventDay.getCalendar().getTime());
                int complete_month = Integer.parseInt(formattedmonth);
                complete_month--;


                SimpleDateFormat day = new SimpleDateFormat("dd", Locale.getDefault());
                String formattedday = day.format(myEventDay.getCalendar().getTime());
                int complete_day = Integer.parseInt(formattedday);



                am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


                Intent intent = new Intent(getApplicationContext(), BroadcastD.class);

                PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

                Calendar calendar = Calendar.getInstance();
                //알람시간 calendar에 set해주기

                calendar.set(complete_year, complete_month, complete_day, 12, 0, 0);

                //알람 예약
                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);


                finish();
            }
        });

        Calendar cal = Calendar.getInstance();
        try{
            datePicker.setDate(cal);
        }catch(OutOfDateRangeException e){
            e.printStackTrace();
        }
    }




}