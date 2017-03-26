package group5.tcss450.uw.edu.whywait;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.jjoe64.graphview.GraphView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private final int mCapacity = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        RelativeLayout detail = (RelativeLayout) findViewById(R.id.detailLayout);
        detail.setVerticalScrollBarEnabled(true);

        TextView mNameTv = (TextView) findViewById(R.id.nameTextView);
        String name = getIntent().getSerializableExtra("name").toString();
        mNameTv.setText(name);

        TextView mVicinityTv = (TextView) findViewById(R.id.vicinityTextView);
        String vicinity = getIntent().getSerializableExtra("vicinity").toString();
        mVicinityTv.setText(vicinity);

        TextView ratingTv = (TextView) findViewById(R.id.ratingTextView);
        String rating = getIntent().getSerializableExtra("rating").toString();
        ratingTv.setText(rating);

        TextView openTv = (TextView) findViewById(R.id.openNowTextView);
        String openStr = "Not Available";
        if (getIntent().getSerializableExtra("openNow") != null) {
            openStr = getIntent().getSerializableExtra("openNow").toString();
        }
        openTv.setText(openStr);



        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1f, 0)); //10
        entries.add(new BarEntry(2f, 1)); //11
        entries.add(new BarEntry(3f, 2)); //12
        entries.add(new BarEntry(8f, 3)); //13
        entries.add(new BarEntry(6f, 4)); //14
        entries.add(new BarEntry(4f, 5)); //15
        entries.add(new BarEntry(3f, 6)); //16
        entries.add(new BarEntry(6f, 7)); //17
        entries.add(new BarEntry(8f, 8)); //18
        entries.add(new BarEntry(9f, 9)); //19
        entries.add(new BarEntry(8f, 10)); //20
        entries.add(new BarEntry(6f, 11)); //21
        entries.add(new BarEntry(4f, 12)); //22

        BarDataSet dataset = new BarDataSet(entries, "How full a place is");

        ArrayList<String> labels = new ArrayList<>();
        labels.add("10");
        labels.add("11");
        labels.add("12");
        labels.add("13");
        labels.add("14");
        labels.add("15");
        labels.add("16");
        labels.add("17");
        labels.add("18");
        labels.add("19");
        labels.add("20");
        labels.add("21");
        labels.add("22");

        BarChart chart = (BarChart) findViewById(R.id.barchart);

        BarData data = new BarData(labels, dataset);
        chart.setData(data);
        chart.animateY(2000);
        chart.setDescription("");
    }
    
}
