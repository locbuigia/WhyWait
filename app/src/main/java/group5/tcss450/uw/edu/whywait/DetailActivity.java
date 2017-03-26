package group5.tcss450.uw.edu.whywait;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;

import java.text.DecimalFormat;

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
        String openStr = getIntent().getSerializableExtra("openNow").toString();
        openTv.setText(openStr);

    }
    
}
