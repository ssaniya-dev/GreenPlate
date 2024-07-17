package com.example.recipeapp;
import androidx.appcompat.app.AppCompatActivity;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.recipeapp.views.WelcomeActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Chart1Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart1);
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(back -> {
            Intent intent = new Intent(Chart1Activity.this, WelcomeActivity.class);
            startActivity(intent);
        });

        Calendar calendar = Calendar.getInstance();
        AnyChartView dataVisual1 = (AnyChartView) findViewById(R.id.data_visual_1);
        dataVisual1.setProgressBar(findViewById(R.id.progress_bar));

        ArrayList<String> dataKeys = getIntent().getStringArrayListExtra("dataKeys");
        ArrayList<Integer> dataValues = getIntent().getIntegerArrayListExtra("dataValues");

        List<DataEntry> dataList = new ArrayList<>();

        for (int i = 0; i < dataKeys.size(); i++) {
            dataList.add(new ValueDataEntry(dataKeys.get(i), dataValues.get(i)));
        }

        Cartesian cartesian = AnyChart.column();
        Column column = cartesian.column(dataList);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("Daily Caloric Intake");

        //make current month into string
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        String currMonthName = new DateFormatSymbols().getMonths()[currentMonth - 1];
        cartesian.title("Daily Caloric Intake for " + currMonthName);

        cartesian.yScale().minimum(0d);
        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Days");
        cartesian.yAxis(0).title("Calories per day");

        dataVisual1.setChart(cartesian);
    }
}
