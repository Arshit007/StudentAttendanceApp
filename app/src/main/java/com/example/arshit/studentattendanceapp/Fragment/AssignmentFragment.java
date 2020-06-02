package com.example.arshit.studentattendanceapp.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.arshit.studentattendanceapp.Assignment.ShowAssignmentFragment;
import com.example.arshit.studentattendanceapp.Assignment.SubmitAssignmentFragment;
import com.example.arshit.studentattendanceapp.R;
import com.example.arshit.studentattendanceapp.TimeTable.FridayFragment;
import com.example.arshit.studentattendanceapp.TimeTable.MondayFragment;
import com.example.arshit.studentattendanceapp.TimeTable.ThursdayFragment;
import com.example.arshit.studentattendanceapp.TimeTable.TimeTableMainActivity;
import com.example.arshit.studentattendanceapp.TimeTable.TuesdayFragment;
import com.example.arshit.studentattendanceapp.TimeTable.WednesdayFragment;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;


public class AssignmentFragment extends Fragment implements TabLayout.BaseOnTabSelectedListener {

    FirebaseUser currentFirebaseUser;
    TabLayout tabLayout;
    MyPagerAdapter adapterViewPager;
    MondayFragment mondayFragment;
    TuesdayFragment tuesdayFragment;
    List<String> sem;

    String DeptName,DeptField,DeptSpec,SemesterItem,session_id,SessionId;

    WednesdayFragment wednesdayFragment;
    ThursdayFragment thursdayFragment;
    FridayFragment fridayFragment;
    ViewPager vpPager;
    Bundle bundle1,bundle,bundle2,bundle3,bundle4;
    TextView toolbar_title;
    Intent intent;
    View view;

    public AssignmentFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_assignment, container, false);

         intialise();
        return view;

    }

    private void intialise() {

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;



        session_id= currentFirebaseUser.getUid();
        sem = new ArrayList<String>();



        tabLayout = (TabLayout)view.findViewById(R.id.tabLayout1);

        tabLayout.addTab(tabLayout.newTab().setText("Show Assignment"));
        tabLayout.addTab(tabLayout.newTab().setText("Submit Assignment"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        vpPager = (ViewPager)view.findViewById(R.id.vpPager1);
        tabLayout.setOnTabSelectedListener(AssignmentFragment.this);

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




    public class MyPagerAdapter extends FragmentPagerAdapter {

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

                 ShowAssignmentFragment showAssignment = new ShowAssignmentFragment();

                    return showAssignment;

                case 1 : // Fragment # 0 - This will show FirstFragment different title



                    SubmitAssignmentFragment submitAssignment = new SubmitAssignmentFragment();

                    return submitAssignment;



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