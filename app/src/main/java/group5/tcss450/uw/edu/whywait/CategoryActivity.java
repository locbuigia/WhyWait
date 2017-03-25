package group5.tcss450.uw.edu.whywait;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CategoryActivity extends AppCompatActivity {
    private static final String Key_Search = "type";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

    }
    public void goToMap(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.gym_button:
                intent = new Intent(this, MapsActivity.class);
                intent.putExtra(Key_Search, "gym");
                startActivity(intent);
            case R.id.theater_button:
                intent = new Intent(this, MapsActivity.class);
                intent.putExtra(Key_Search, "movie_theater");
                startActivity(intent);

            case R.id.parks_button:
                intent = new Intent(this, MapsActivity.class);
                intent.putExtra(Key_Search,"park");
                startActivity(intent);

            case R.id.restaurant_button:
                intent = new Intent(this, MapsActivity.class);
                intent.putExtra(Key_Search,"restaurant");
                startActivity(intent);
        }


    }
}
