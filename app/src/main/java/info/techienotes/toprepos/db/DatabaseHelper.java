package info.techienotes.toprepos.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import info.techienotes.toprepos.model.Item;
import info.techienotes.toprepos.model.Repo;

/**
 * Created by bharatkulratan
 */

public class DatabaseHelper extends SQLiteOpenHelper{
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "github_trending";

    // Contacts table name
    private static final String TABLE_REPO = "repo";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String NAME = "name";
    private static final String FULL_NAME = "full_name";
    private static final String LANGUAGE = "language";
    private static final String STAR_COUNT = "star_count";
    private static final String HTML_URL = "html_url";
    private static final String AUTHOR = "author";
    private static final String DURATION = "duration";
    private static final String DATE_RANGE = "date_range";
    private static final String DESCRIPTION = "description";
    private static final String FORK_COUNT = "fork_count";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder create_repo_query = new StringBuilder();

        create_repo_query.append("CREATE TABLE " + TABLE_REPO + "(");
        create_repo_query.append(KEY_ID + " INTEGER PRIMARY KEY, ");
        create_repo_query.append(NAME + " TEXT, ");
        create_repo_query.append(FULL_NAME + " TEXT, ");
        create_repo_query.append(DESCRIPTION + " TEXT, ");
        create_repo_query.append(LANGUAGE + " TEXT, ");
        create_repo_query.append(HTML_URL + " TEXT, ");
        create_repo_query.append(AUTHOR + " TEXT, ");
        create_repo_query.append(DURATION + " TEXT, ");
        create_repo_query.append(DATE_RANGE + " TEXT, ");
        create_repo_query.append(FORK_COUNT + " INTEGER, ");
        create_repo_query.append(STAR_COUNT + " INTEGER" + ")");

        String CREATE_REPO_TABLE = create_repo_query.toString();

        db.execSQL(CREATE_REPO_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPO);

        // Create tables again
        onCreate(db);
    }

    // Adding a single repo item
    private void addRepo(SQLiteDatabase db, Item item, String duration, String dateRange) {
        ContentValues values = new ContentValues();
        values.put(NAME, item.getName());
        values.put(FULL_NAME, item.getFullName());
        values.put(LANGUAGE, item.getLanguage());
        values.put(HTML_URL, item.getHtmlUrl());
        values.put(AUTHOR, item.getOwner().getLogin());
        values.put(STAR_COUNT, item.getStargazersCount());
        values.put(DESCRIPTION, item.getDescription());
        values.put(FORK_COUNT, item.getForks());
        values.put(DURATION, duration);
        values.put(DATE_RANGE, dateRange);

        try {
            // Inserting Row
            db.insert(TABLE_REPO, null, values);
        }catch (SQLiteException exception){
            Log.e("DB", exception.getMessage());
        }
    }


    public void addRepoList(List<Item> repoList, String language, String duration, String dateRange){
        SQLiteDatabase db = this.getWritableDatabase();

        deleteExistingEntries(db, language, duration);

        int listSize = repoList.size();
        for (int C=0; C<listSize; C++){
            addRepo(db, repoList.get(C), duration, dateRange);
        }

        db.close(); // Closing database connection
    }

    private int deleteExistingEntries (SQLiteDatabase db, String language, String duration){
        //SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_REPO, LANGUAGE + "=? AND " + DURATION + "=?",
                new String[] { language, duration });
    }

    // Fetching list repo item
    public List<Repo> getRepos(String language, String duration) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_REPO, null, LANGUAGE + "=? AND " + DURATION + "=?",
                new String[] { language, duration }, null, null, null, null);

        List<Repo> repoItems = new ArrayList<>();

        if (cursor == null){
            return repoItems;
        }

        if (cursor.moveToFirst()) {
            do {
                Repo repo = new Repo();

                repo.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                repo.setFullName(cursor.getString(cursor.getColumnIndex(FULL_NAME)));
                repo.setAuthor(cursor.getString(cursor.getColumnIndex(AUTHOR)));
                repo.setLanguage(cursor.getString(cursor.getColumnIndex(LANGUAGE)));
                repo.setStarCount(cursor.getInt(cursor.getColumnIndex(STAR_COUNT)));
                repo.setHtmlUrl(cursor.getString(cursor.getColumnIndex(HTML_URL)));
                repo.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
                repo.setForkCount(cursor.getInt(cursor.getColumnIndex(FORK_COUNT)));

                repoItems.add(repo);
            } while (cursor.moveToNext());
        }

        return repoItems;
    }
}
