package com.example.arshit.studentattendanceapp.TimeTable;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.arshit.studentattendanceapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TimeTableMainActivity extends AppCompatActivity implements TabLayout.BaseOnTabSelectedListener {


    View view;

    List<String> sem;

    String DeptName,DeptField,DeptSpec,SemesterItem,session_id,SessionId;

    FirebaseUser currentFirebaseUser;
    TabLayout tabLayout;
    MyPagerAdapter adapterViewPager;
    MondayFragment mondayFragment;
    TuesdayFragment tuesdayFragment;
    WednesdayFragment wednesdayFragment;
    ThursdayFragment thursdayFragment;
    FridayFragment fridayFragment;
    ViewPager vpPager;
    Bundle bundle1,bundle,bundle2,bundle3,bundle4;
    TextView toolbar_title;
    Intent intent;
    Spinner spinner;

    Locale myLocale;
    String currentLanguage = "en", currentLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table_main);

        intialise();
        toolbar();
    }

    private void toolbar() {


        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.user_toolbar);

        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(SemesterItem);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


    }


    private void intialise() {

        intent = getIntent();

        SemesterItem = intent.getStringExtra("SemesterItem");
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;



        session_id= currentFirebaseUser.getUid();
        sem = new ArrayList<String>();



        tabLayout = (TabLayout)findViewById(R.id.tabLayout);

        tabLayout.addTab(tabLayout.newTab().setText("Monday"));
        tabLayout.addTab(tabLayout.newTab().setText("Tuesday"));
        tabLayout.addTab(tabLayout.newTab().setText("WednesDay"));
        tabLayout.addTab(tabLayout.newTab().setText("Thursday"));
        tabLayout.addTab(tabLayout.newTab().setText("Friday"));


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        vpPager = (ViewPager)findViewById(R.id.vpPager);
        tabLayout.setOnTabSelectedListener(TimeTableMainActivity.this);

        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
//        showDay();

        vpPager.setAdapter(adapterViewPager);

        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                vpPager.setCurrentItem(position);

                tabLayout.getTabAt(position).select();


            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }


            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        vpPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    public  class MyPagerAdapter extends FragmentPagerAdapter {

        int tabCount;


        public MyPagerAdapter(FragmentManager fragmentManager, int tabCount) {
            super(fragmentManager);
            this.tabCount= tabCount;
        }

        @Override
        public int getCount() {
            return tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment


                    bundle = new Bundle();
                    bundle.putString("Semester",SemesterItem);
                     mondayFragment = new MondayFragment();
                    mondayFragment.setArguments(bundle);


                    return mondayFragment;

                case 1 : // Fragment # 0 - This will show FirstFragment different title


                    bundle1 = new Bundle();
                    bundle1.putString("Semester",SemesterItem);
                    tuesdayFragment = new TuesdayFragment();

                    tuesdayFragment.setArguments(bundle1);

                    return tuesdayFragment;

                case 2:

                    bundle2 = new Bundle();
                    bundle2.putString("Semester",SemesterItem);

                    wednesdayFragment = new WednesdayFragment();
                    wednesdayFragment.setArguments(bundle2);


                    return wednesdayFragment;

                case 3 : // Fragment # 0 - This will show FirstFragment different title


                    bundle3 = new Bundle();
                    bundle3.putString("Semester",SemesterItem);

                    thursdayFragment = new ThursdayFragment();

                    thursdayFragment.setArguments(bundle3);

                    return thursdayFragment;

                case 4: // Fragment # 0 - This will show FirstFragment


                    bundle4 = new Bundle();
                    bundle4.putString("Semester",SemesterItem);

                  fridayFragment = new FridayFragment();
                    fridayFragment.setArguments(bundle4);


                    return fridayFragment;




                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }



    }

  }
