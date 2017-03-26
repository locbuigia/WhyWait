package group5.tcss450.uw.edu.whywait;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jnbui on 3/26/2017.
 */

public class SearchDB implements Serializable {
    /**
     * Database version.
     */
    public static final int DB_VERSION = 1;
    /**
     * DB name.
     */
    private final String DB_NAME;
    /**
     * table.
     */
    private final String Search_TABLE;
    /**
     * Name of column.
     */
    private final String[] COLUMN_NAMES;
    private Context mContext;
    /**
     * helper variable
     */
    private NewsDBHelper mNewsDBHelper;
    /**
     * SQL variable
     */
    private SQLiteDatabase mSQLiteDatabase;

    /**
     * public constructor.
     * @param context
     */
    public SearchDB(Context context){
        COLUMN_NAMES = context.getResources().getStringArray(R.array.DB_COLUMN_NAMES);
        DB_NAME = context.getString(R.string.DB_NAME);
        Search_TABLE = context.getString(R.string.TABLE_NAME);
        mContext = context;

        mNewsDBHelper = new NewsDBHelper(
                context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mNewsDBHelper.getWritableDatabase();
    }

    public boolean insertSearches(String name){
        if (!checkExist(name)) {
            ContentValues contextValues = new ContentValues();
            contextValues.put(COLUMN_NAMES[0], name);
            long rowId = mSQLiteDatabase.insert(Search_TABLE, null, contextValues);
        Log.d("save data", name);
            return rowId != -1;
        } else {
            return false;
        }
    }

    public boolean checkExist(String name){
        String query = "SELECT name FROM Bookmarks WHERE name=?";
        Cursor c = mSQLiteDatabase.rawQuery(query, new String[]{name});
        if (c.moveToFirst()){
            return true;
        }else {
            return false;
        }
    }
    public Object[] getBookmarks(){

        Cursor c = mSQLiteDatabase.query(
                Search_TABLE,
                COLUMN_NAMES,
                null, null, null, null, null
        );
        c.moveToFirst();
        List<String> list = new ArrayList<>();
        for (int i = 0 ; i < c.getCount(); i++){
            list.add(c.getString(0));
            c.moveToNext();
        }
       // return (list.toArray(new News[list.size()]));
        Log.d("Item to saved DB", list.get(2));
        return list.toArray();
    }

    /**
     * Inner helper class to create and upgrade SQL.
     */
    class NewsDBHelper extends SQLiteOpenHelper {
        /**
         * Create new table in DB
         */
        private final String CREATE_NEWS_SQL;
        /**
         * Drop table of exist.
         */
        private final String DROP_NEWS_SQL;

        /**
         * public constructor to add or drop table in DB.
         * @param context
         * @param name
         * @param factory
         * @param version
         */
        public NewsDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
            super(context, name, factory, version);
            CREATE_NEWS_SQL = context.getString(R.string.CREATE_NEWS_SQL);
            DROP_NEWS_SQL = context.getString(R.string.DROP_NEWS_SQL);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_NEWS_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_NEWS_SQL);
            onCreate(db);
        }
    }
}
