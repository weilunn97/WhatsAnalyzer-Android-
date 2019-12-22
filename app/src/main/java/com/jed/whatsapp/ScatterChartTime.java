package com.jed.whatsapp;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ScatterChartTime extends DemoBase implements OnSeekBarChangeListener {

    private ScatterChart chart;
    private SeekBar seekBarX;
    private TextView tvX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphing);
        try {
            getSupportActionBar().hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        // Horizontal seek bar
        tvX = findViewById(R.id.tvXMax);
        seekBarX = findViewById(R.id.seekBar1);
        seekBarX.setOnSeekBarChangeListener(this);

        // Reply timings chart
        chart = findViewById(R.id.chart1);

        // No description text
        chart.getDescription().setEnabled(false);

        // Enable touch gestures
        chart.setTouchEnabled(true);
        chart.setDragDecelerationFrictionCoef(0.9f);

        // Enable scaling, dragging, and zooming
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setMaxVisibleValueCount(200);
        chart.setPinchZoom(true);

        // set an alternative background color
        chart.setBackgroundColor(Color.WHITE);

        // add data
        seekBarX.setProgress(100);

        // X-Axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setTextSize(14f);
        xAxis.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.senderOne));
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(true);
        xAxis.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.senderOne));
        xAxis.setCenterAxisLabels(true);
        xAxis.setGridColor(ContextCompat.getColor(getApplicationContext(), R.color.senderOne));


        // Generate lookup dictionary for MONTH
        final HashMap<Integer, String> monthMap = new HashMap<>();
        monthMap.put(1, "Jan");
        monthMap.put(2, "Feb");
        monthMap.put(3, "Mar");
        monthMap.put(4, "Apr");
        monthMap.put(5, "May");
        monthMap.put(6, "Jun");
        monthMap.put(7, "Jul");
        monthMap.put(8, "Aug");
        monthMap.put(9, "Sep");
        monthMap.put(10, "Oct");
        monthMap.put(11, "Nov");
        monthMap.put(12, "Dec");

        // Custom date formatter for X-Axis labels
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                Date returnDate = new Date((long) value);
                return monthMap.get(returnDate.getMonth()) + " " + (returnDate.getYear() - 100);
            }
        });

        // Y-Axis
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.senderOne));
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawGridLines(true);
        leftAxis.setTextSize(14f);
        leftAxis.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.senderOne));
        leftAxis.setAxisMinimum(0);
        leftAxis.setSpaceTop(5f);
        leftAxis.setSpaceBottom(5f);
        leftAxis.setGridColor(ContextCompat.getColor(getApplicationContext(), R.color.senderOne));

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        Legend legend = chart.getLegend();
        legend.setEnabled(true);
        legend.setTextSize(16f);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setFormSize(16f);
        legend.setWordWrapEnabled(true);

    }

    private void setData(int count, float range) {

        // Generate list of reply timings
        ArrayList<Entry> replyTimingsOne = new ArrayList<>();
        try {
            for (int i = 0; i < ReplyTiming.getSenderOneTimeStamp().size(); i++) {
                replyTimingsOne.add(new Entry(ReplyTiming.getSenderOneTimeStamp().get(i).getTime(),
                        ReplyTiming.getSenderOneReplyTimeInMinutes().get(i)));
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        ArrayList<Entry> replyTimingsTwo = new ArrayList<>();
        try {
            for (int i = 0; i < ReplyTiming.getSenderTwoTimeStamp().size(); i++) {
                replyTimingsTwo.add(new Entry(ReplyTiming.getSenderTwoTimeStamp().get(i).getTime(),
                        ReplyTiming.getSenderTwoReplyTimeInMinutes().get(i)));
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        // create a dataset and give it a type
        ScatterDataSet set1 = new ScatterDataSet(replyTimingsOne, ReplyTiming.getSenderList().get(0));
        ScatterDataSet set2 = new ScatterDataSet(replyTimingsTwo, ReplyTiming.getSenderList().get(1));

        set1.setAxisDependency(AxisDependency.LEFT);
        set1.setColor(ContextCompat.getColor(getApplicationContext(), R.color.senderOne));
        set1.setDrawValues(false);
        set1.setColor(ContextCompat.getColor(getApplicationContext(), R.color.senderOne));

        set2.setAxisDependency(AxisDependency.LEFT);
        set2.setColor(ContextCompat.getColor(getApplicationContext(), R.color.senderTwo));
        set2.setValueTextColor(ContextCompat.getColor(getApplicationContext(), R.color.senderTwo));
        set2.setDrawValues(false);
        set2.setColor(ContextCompat.getColor(getApplicationContext(), R.color.senderTwo));

        // create a data object with the data sets
        ScatterData data = new ScatterData();
        data.addDataSet(set1);
        data.addDataSet(set2);

        // set data
        chart.setData(data);
        set1.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        set2.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        set1.setScatterShapeSize(16f);
        set2.setScatterShapeSize(16f);

        // change axis color
        chart.getAxisLeft().setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.senderOne));
        chart.getAxisLeft().setAxisLineColor(ContextCompat.getColor(getApplicationContext(), R.color.senderOne));
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        tvX.setText(String.valueOf(seekBarX.getProgress()));

        setData(seekBarX.getProgress(), 50);

        // redraw
        chart.invalidate();
    }

    @Override
    protected void saveToGallery() {
        saveToGallery(chart, "ScatterChartTime");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}