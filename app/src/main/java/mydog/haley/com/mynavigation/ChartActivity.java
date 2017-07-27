package mydog.haley.com.mynavigation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;

public class ChartActivity extends AppCompatActivity {

    // 차트 테스트

    private BarChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);


        mChart = (BarChart)findViewById(R.id.chart);


    }
}
