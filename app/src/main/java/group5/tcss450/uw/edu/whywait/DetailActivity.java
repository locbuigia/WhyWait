package group5.tcss450.uw.edu.whywait;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailActivity extends AppCompatActivity {
    private final int mCapacity = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
//        mCapacity = getIntent().getIntExtra(Capacity);
    }
    
}
