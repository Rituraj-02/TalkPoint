package com.example.talkpoint.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.talkpoint.Adapter.viewPagerAdapter;
import com.example.talkpoint.R;
import com.google.android.material.tabs.TabLayout;


public class NotificationFragment extends Fragment {

    ViewPager viewpager;
    TabLayout tabLayout;

    public NotificationFragment() {
        // Required empty public constructor
    }



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        viewpager = view.findViewById(R.id.viewPager);
        viewpager.setAdapter(new viewPagerAdapter(getFragmentManager()));

        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewpager);
        return view;
    }
}