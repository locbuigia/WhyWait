package group5.tcss450.uw.edu.whywait;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    /**
     * Key for news.
     */
    private final static String NEWS_KEY = "news";
    /**
     * the key for getting the username.
     */
    private static final String KEY_SEARCH = "name";
    /**
     * AppCombat variable.
     */
    AppCompatActivity mActivity;
    /**
     * Username to keep track in DB
     */
    private String mUsername;
    private ListView listview;
    /**
     * NewsDB Object
     */
    private SearchDB mCourseDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Log.d("Activity started", "History");
        listview = (ListView) findViewById(R.id.History_listView);
        loadFromLocalDB();
    }
    /**
     * Loading content from Db and display a listView
     * NewViewAcrtivity will start when item is clicked.
     */
    private void loadFromLocalDB(){
        if(mCourseDB == null) {
            mCourseDB = new SearchDB(this);
        }
        Object[] SearcjObj = mCourseDB.getBookmarks();
        String[] search = Arrays.copyOf(SearcjObj,SearcjObj.length,String[].class);
        Log.d("Array length", "the length" + search.length);
        Log.d("Item is ",SearcjObj[2].toString());
        final String[] tempSearches = search;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                Arrays.asList(search));
        listview.setAdapter(arrayAdapter);
        mActivity = this;
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent(mActivity, MainActivity.class);
                intent.putExtra(KEY_SEARCH, tempSearches[position]);
                startActivity(intent);
            }
        });
    }
}
