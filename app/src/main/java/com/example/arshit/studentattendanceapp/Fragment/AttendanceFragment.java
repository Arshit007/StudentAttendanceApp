package com.example.arshit.studentattendanceapp.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.example.arshit.studentattendanceapp.Attendance.DailyAttendanceFragment;
import com.example.arshit.studentattendanceapp.Attendance.TotalSubjectAttendanceFragment;
import com.example.arshit.studentattendanceapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class AttendanceFragment extends Fragment implements TabLayout.BaseOnTabSelectedListener {


    FirebaseAuth mAuth;

    View view;


    TabLayout tabLayout;
    MyPagerAdapter adapterViewPager;
    ViewPager vpPager;


    public AttendanceFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_status_attendance, container, false);


intialise();
        return view;

    }

    private  void intialise(){




     tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);



//        tabLayout.addTab(tabLayout.newTab().setText("Daily Attendance Subject Wise"));
        tabLayout.addTab(tabLayout.newTab().setText("Total Attendance Subject Wise"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        vpPager = (ViewPager) view.findViewById(R.id.vpPager);
        tabLayout.setOnTabSelectedListener(AttendanceFragment.this);
        adapterViewPager = new MyPagerAdapter(getChildFragmentManager(),tabLayout.getTabCount());
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

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;
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
//                case 0:
//
//
//                    DailyAttendanceFragment dailyAttendanceFragment = new DailyAttendanceFragment();
//                    return dailyAttendanceFragment;

                case 0 :
                    TotalSubjectAttendanceFragment totalSubjectAttendanceFragment = new TotalSubjectAttendanceFragment();
                    return totalSubjectAttendanceFragment;


                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }



    }





//        database.child(session_id).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//                final List<String> areas = new ArrayList<String>();
//
//                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
//                    String areaName = areaSnapshot.getValue(String.class);
//                    areas.add(areaName);
//                }
//
//
//                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, areas);
//                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spinner.setAdapter(areasAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//
//
//
//        });
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//        {
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
//            {
//                selectedItem = parent.getItemAtPosition(position).toString();
//            }
//
//            public void onNothingSelected(AdapterView<?> parent)
//            {
//
//            }
//        });
//
//        btnAddTeacher.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(getContext(),StatusAttendanceActivity.class);
//                intent.putExtra("CourseSelected",selectedItem);
//                intent.putExtra("id",selectedItem);
//
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//
//
//
//            }
//        });



    }




//}