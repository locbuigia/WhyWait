package group5.tcss450.uw.edu.whywait;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class CategoryActivity extends AppCompatActivity {
    private static final String Key_Search = "type";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
//        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
//
//        // use this setting to improve performance if you know that changes
//        // in content do not change the layout size of the RecyclerView
//        mRecyclerView.setHasFixedSize(true);
//
//        // use a linear layout manager
//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//
//        // specify an adapter (see also next example)
//        mAdapter = new MyAdapter(myDataset);
//        mRecyclerView.setAdapter(mAdapter);

    }
    public void goToMap(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.gym_button:
                intent = new Intent(this, MainActivity.class);
                intent.putExtra(Key_Search, "gym");
                startActivity(intent);
                break;

            case R.id.theater_button:
                intent = new Intent(this, MainActivity.class);
                intent.putExtra(Key_Search, "movie_theater");
                startActivity(intent);
                break;
            case R.id.parks_button:
                intent = new Intent(this, MainActivity.class);
                intent.putExtra(Key_Search,"park");
                startActivity(intent);
                break;
            case R.id.restaurant_button:
                intent = new Intent(this, MainActivity.class);
                intent.putExtra(Key_Search,"restaurant");
                startActivity(intent);
                break;
        }
    }
    /**
     * LogOut when LogOut menu is called.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.Return_home) {
            homeActivity();
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * This method will call the home activity.
     */
    private void homeActivity() {
        finish();
    }
    /**
     * create option menu.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homebutton, menu);
        return true;
    }
}
