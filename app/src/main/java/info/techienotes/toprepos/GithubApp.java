package info.techienotes.toprepos;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import net.danlew.android.joda.JodaTimeAndroid;

import info.techienotes.toprepos.db.DatabaseHelper;


public class GithubApp extends Application
        implements Application.ActivityLifecycleCallbacks {

    private String TAG = this.getClass().getSimpleName();
    private DatabaseHelper dbHelper;


    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(GithubApp.this);

        dbHelper = new DatabaseHelper(this);
        JodaTimeAndroid.init(this);

        sAnalytics = GoogleAnalytics.getInstance(this);
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(getString(R.string.ga_trackingId));
        }

        return sTracker;
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

    @Override
    public void onActivityStarted(Activity activity) {}

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

    @Override
    public void onActivityDestroyed(Activity activity) {}


    public DatabaseHelper getDbHelper() {
        return dbHelper;
    }
}

