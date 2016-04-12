package com.example.xieminjie.clientapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;

public class RecordDetail extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private Toolbar toolbar;
    private SearchView searchView;
    private String query;
    private ArrayList<Record> arrayList;
    private IOStorageHandler ioStorageHandler;
    private Hashtable hashtable;
    private Record record;
    private int index;
    private BarChart barChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);
        toolbar = (Toolbar)findViewById(R.id.navbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        searchView = (SearchView)findViewById(R.id.record_SearchView);
        searchView.setOnQueryTextListener(this);
        barChart = (BarChart)findViewById(R.id.chart);
        Intent intent = getIntent();
        query = intent.getStringExtra("searchName");
        index = getIndex(query);
        createChart(index, barChart, query);

    }
    private int getIndex(String query){
        Hashtable<String,String> hashtable = new Hashtable();
        hashtable.put("problem","0");
        hashtable.put("ill","1");
        hashtable.put("palpitations","2");
        hashtable.put("weightGain","3");
        hashtable.put("highBloodPressure","4");
        hashtable.put("muscleWeakness", "5");
        hashtable.put("sweating", "6");
        hashtable.put("flushing", "7");
        hashtable.put("headache", "8");
        hashtable.put("chestPain", "9");
        hashtable.put("backPain", "10");
        hashtable.put("bruising", "11");
        hashtable.put("fatigue", "12");
        hashtable.put("panic", "13");
        hashtable.put("sadness","14");
        hashtable.put("recordDate", "15");
        if (hashtable.containsKey(query)){
            index = Integer.parseInt(hashtable.get(query).toString());
        }else{
            index = 0;
        }
        return index;
    }
    private void createChart(int index,BarChart barChart,String query){
        ArrayList<String> dateArrayList;
        ArrayList<String> dataArrayList;
        ArrayList<String> dateArray;
        ArrayList<String> dataArray;

        dateArrayList = ioStorageHandler.readData("record.csv", 15, getApplicationContext());
        dataArrayList = ioStorageHandler.readData("record.csv", index, getApplicationContext());
        dateArray = dateProcessing(dateArrayList);
        dataArray = dateProcessing(dataArrayList);
        BarData data = new BarData(getXAxisValues(dateArray),getDataSet(dataArray,query));
        barChart.setData(data);
        barChart.setDescription("My Chart");
        barChart.animateXY(2000, 2000);
        barChart.invalidate();

    }
    private ArrayList<String> dateProcessing(ArrayList a){
        ArrayList<String> array = new ArrayList<>();
        if(a.size()<=5){
            for(int i=0;i<5;i++){
                array.add(a.get(i).toString());
            }
        }else{
            for(int i = a.size()-1;i>=a.size()-5;i--){
                array.add(a.get(i).toString());
            }
        }
        return array;
    }
    private ArrayList<IBarDataSet> getDataSet(ArrayList dataArray,String query) {
        ArrayList<IBarDataSet> dataSets;
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        for (int i=0;i<dataArray.size();i++){
            BarEntry v = new BarEntry(Integer.valueOf(dataArray.get(i).toString()),i);
            valueSet1.add(v);
        }
        BarDataSet barDataSet = new BarDataSet(valueSet1,query);
        barDataSet.setColor(Color.rgb(0, 155, 0));
        dataSets = new ArrayList<>();
        dataSets.add(barDataSet);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues(ArrayList dateArray) {
        ArrayList<String> xAxis = new ArrayList<>();
        for(int i=0;i<dateArray.size();i++){
            xAxis.add(dateArray.get(i).toString());
        }
        return xAxis;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_record_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if(id==android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        barChart.clear();
        index = getIndex(s);
        createChart(index, barChart,s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}
