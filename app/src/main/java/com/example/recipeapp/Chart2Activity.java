package com.example.recipeapp;
import androidx.appcompat.app.AppCompatActivity;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;

import com.example.recipeapp.views.WelcomeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
public class Chart2Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart2);

        AnyChartView dataVisual2 = (AnyChartView) findViewById(R.id.data_visual_2);
        dataVisual2.setProgressBar(findViewById(R.id.progress_bar2));

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(back -> {
            Intent intent = new Intent(Chart2Activity.this, WelcomeActivity.class);
            startActivity(intent);
        });

        ArrayList<Integer> dataValues = getIntent().getIntegerArrayListExtra("calorieList");

        Pie pie = AnyChart.pie();
        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(Chart2Activity.this,
                        event.getData().get("x") + ":"
                                + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        List<DataEntry> data = new ArrayList<>();
        int x = 0;
        for (int i = 0; i < dataValues.size(); i++) {
            x++;
            data.add(new ValueDataEntry("Meal " + x, dataValues.get(i)));
        }

        pie.data(data);
        pie.title("Distribution of Calories Consumed per Meal Today");

        pie.labels().position("outside");

        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Meals")
                .padding(0d, 0d, 10d, 0d);

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        dataVisual2.setChart(pie);
    }
}
