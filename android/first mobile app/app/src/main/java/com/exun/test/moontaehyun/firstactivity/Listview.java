package com.exun.test.moontaehyun.firstactivity;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.exun.test.moontaehyun.CalendarActivity;
import com.exun.test.moontaehyun.NavAdapter;
import com.exun.test.moontaehyun.R;
import com.exun.test.moontaehyun.firstactivity.Listview;
import com.exun.test.moontaehyun.firstactivity.Redvelvet2;
import com.exun.test.moontaehyun.firstactivity.Twice2;
import com.exun.test.moontaehyun.secondactivity.Irene;
import com.exun.test.moontaehyun.secondactivity.Sana;
import com.exun.test.moontaehyun.secondactivity.jeongyeon;
import com.exun.test.moontaehyun.secondactivity.joy;
import com.exun.test.moontaehyun.secondactivity.nayeon;
import com.exun.test.moontaehyun.secondactivity.seulgi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.exun.test.moontaehyun.AddNoteActivity.am;

public class Listview extends AppCompatActivity implements ExpandableListView.OnChildClickListener  {

    Toolbar toolbar;
    private DrawerLayout drawer;
    private ExpandableListView drawerList;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    static public Switch sw;
    private TextView selected_item_textview;
    static public ListView listview;
    static public ListView listview1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listview);

        Button b =(Button) findViewById(R.id.button3);
        b.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                }
        );
        listview = (ListView)findViewById(R.id.listview);
        listview1 = (ListView)findViewById(R.id.listview1);
        final ArrayList<String> list = new ArrayList<>();
        final ArrayList<String> list1 = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, list1);
        final ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listview1.setAdapter(adapter);
        listview.setAdapter(adapter2);
        list1.add("사나");
        list1.add("나연");
        list1.add("다현");
        list1.add("쯔위");
        list1.add("모모");
        list1.add("미나");
        list1.add("채영");
        list1.add("정연");
        list1.add("지효");
        list1.add("아이린");
        list1.add("조이");
        list1.add("슬기");
        list1.add("예리");
        list1.add("웬디");
        list.add("지수");
        list.add("리사");
        list.add("로제");
        list.add("제니");

        Collections.sort(list1);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {

                //클릭한 아이템의 문자열을 가져옴
                String selected_item = list.get(position).toString();
                list.remove(position);
                adapter.notifyDataSetChanged();
                list1.add(selected_item);
                Collections.sort(list1);
                adapter2.notifyDataSetChanged();


            }
        });

        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected_item = list1.get(position).toString();
                list1.remove(position);
                Collections.sort(list1);
                adapter2.notifyDataSetChanged();
                list.add(selected_item);
                adapter.notifyDataSetChanged();

            }
        });


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        initDrawer();
    }




    private void initDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerList = (ExpandableListView) findViewById(R.id.left_drawer);

        // preparing list data
        prepareListData();

        drawerList.setAdapter(new NavAdapter(this, listDataHeader, listDataChild));

        drawerList.setOnChildClickListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we don't want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we don't want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();


        // Adding headers
        Resources res = getResources();
        String[] headers = res.getStringArray(R.array.nav_drawer_labels);
        listDataHeader = Arrays.asList(headers);

        //Adding child data

//        //Dynamic method
//        for (int i =0; i<listDataHeader.size(); i++){
//
//            //Save data in array
//            String[] childData = res.getStringArray(R.array.elements_home);
//
//            //Put data in List
//            List<String> listChild;
//            listChild = Arrays.asList(childData);
//
//            //Add to hashMap
//            listDataChild.put(listDataHeader.get(i),listChild);
//        }

        // Static method
        List<String> home, friends, notifs, list;
        String[] shome, sfriends, snotifs, slist;

        shome = res.getStringArray(R.array.elements_home);
        home = Arrays.asList(shome);

        sfriends = res.getStringArray(R.array.elements_friends);
        friends = Arrays.asList(sfriends);

        snotifs = res.getStringArray(R.array.elements_notifs);
        notifs = Arrays.asList(snotifs);

        slist = res.getStringArray(R.array.elements_list);
        list = Arrays.asList(slist);

        // Add to hashMap
        listDataChild.put(listDataHeader.get(0), home); // Header, Child data
        listDataChild.put(listDataHeader.get(1), friends);
        listDataChild.put(listDataHeader.get(2), notifs);
        listDataChild.put(listDataHeader.get(3), list);
    }


    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
                                int groupPosition, int childPosition, long id) {
        if(groupPosition==0&&childPosition==0)
        {
            Intent intent =new Intent(getApplicationContext(), Sana.class);
            startActivity(intent);

        }
        if(groupPosition==0&&childPosition==1)
        {
            Intent intent =new Intent(getApplicationContext(), jeongyeon.class);
            startActivity(intent);

        }
        if(groupPosition==0&&childPosition==2)
        {
            Intent intent =new Intent(getApplicationContext(), nayeon.class);
            startActivity(intent);

        }
        if(groupPosition==1&&childPosition==0)
        {
            Intent intent =new Intent(getApplicationContext(), Irene.class);
            startActivity(intent);

        }
        if(groupPosition==1&&childPosition==1)
        {
            Intent intent =new Intent(getApplicationContext(), joy.class);
            startActivity(intent);

        }
        if(groupPosition==1&&childPosition==2)
        {
            Intent intent =new Intent(getApplicationContext(), seulgi.class);
            startActivity(intent);

        }
        if(groupPosition ==2 && childPosition==0){
            Intent intent =new Intent(getApplicationContext(), CalendarActivity.class);
            startActivity(intent);
        }



        return false;
        /*Toast.makeText(
                getApplicationContext(),
                listDataHeader.get(groupPosition)
                        + " : "
                        + listDataChild.get(
                        listDataHeader.get(groupPosition)).get(
                        childPosition), Toast.LENGTH_SHORT)
                .show();
        return false;*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}