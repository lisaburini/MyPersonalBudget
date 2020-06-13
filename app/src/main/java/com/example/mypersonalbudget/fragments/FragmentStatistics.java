package com.example.mypersonalbudget.fragments;

import android.os.Bundle;
import android.view.MotionEvent;

import androidx.fragment.app.Fragment;

import com.example.mypersonalbudget.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

public class FragmentStatistics extends Fragment implements OnChartGestureListener, OnChartValueSelectedListener {

    private static final String TAG = "FragmentStatistics";

    private LineChart GraficoBarre;

    @Override
    public void OnCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_statistics);

        GraficoBarre = (LineChart) findViewById(R.id.GrafBarre);

        GraficoBarre.setOnChartListener(FragmentStatistics.this);
        GraficoBarre.setOnClickListener
    }



}
