package info.techienotes.toprepos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.techienotes.toprepos.adapter.ChooseLanguageAdapter;
import info.techienotes.toprepos.adapter.RepoItemAdapter;
import info.techienotes.toprepos.db.DatabaseHelper;
import info.techienotes.toprepos.interfaces.ItemClicked;
import info.techienotes.toprepos.model.Repo;
import info.techienotes.toprepos.model.RepoItem;
import info.techienotes.toprepos.networking.NetworkService;
import info.techienotes.toprepos.utils.Constants;
import info.techienotes.toprepos.utils.TextViewRegular;
import info.techienotes.toprepos.utils.TypefaceSpan;
import info.techienotes.toprepos.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ItemClicked {

    private RepoItemAdapter mAdapter;
    private List<Repo> dataList;
    private ProgressBar progressbar;
    private TextView tvNoResults;
    private RecyclerView dataListView;

    private DatabaseHelper dbHelper;
    private SharedPreferences prefs;
    private AutoCompleteTextView languageAutoCompleteView;

    private Tracker gaTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        gaTracker = ((GithubApp) getApplication()).getDefaultTracker();
        gaTracker.setScreenName("HomeActivity");
        gaTracker.send(new HitBuilders.ScreenViewBuilder().build());

        prefs = getSharedPreferences(Constants.USER_PREFS, 0);

        dbHelper = ((GithubApp) getApplication()).getDbHelper();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SpannableString s = new SpannableString(getString(R.string.trending_repos));
        s.setSpan(new TypefaceSpan(this, "OpenSans-Bold.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        getSupportActionBar().setTitle(s);
        getSupportActionBar().setSubtitle(getString(R.string.today));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        drawer.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                return false;
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        tvNoResults = (TextView) findViewById(R.id.tv_no_results);

        languageAutoCompleteView = (AutoCompleteTextView) findViewById(R.id.auto_complete_language);

        // replace with custom adapter and limit to 5 items in dropdown
        ChooseLanguageAdapter languageAdapter = new ChooseLanguageAdapter(this);


        languageAutoCompleteView.setThreshold(1);
        languageAutoCompleteView.setAdapter(languageAdapter);


        RelativeLayout editIcon = (RelativeLayout) toolbar.findViewById(R.id.lay_change);
        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (languageAutoCompleteView.getVisibility()) {
                    case View.VISIBLE:
                        hideInputBox();
                        break;

                    case View.GONE:
                        showInputBox();
                        break;

                    default:
                        break;
                }
            }
        });

        final TextView selectedLang = (TextView) toolbar.findViewById(R.id.selected_lang);
        selectedLang.setText(prefs.getString(Constants.LANGUAGE, "Java"));

        // implement focus change listener.
        // if not in focus and entered text is value of language present in array. then make a call

        languageAutoCompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideInputBox();
                //now put the values into prefs
                String selectedLanguage = ((TextViewRegular) view).getText().toString();

                selectedLang.setText(selectedLanguage);

                prefs.edit().putString(Constants.LANGUAGE, selectedLanguage).commit();

                // make network call whenever user updates the language or duration
                // selectedLanguage should be retrieved from prefs
                makeNetworkCall();

                gaTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("input")
                        .setAction("language")
                        .setLabel(selectedLanguage)
                        .build());

            }
        });

        dataListView = (RecyclerView) findViewById(R.id.data_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this);
        dataListView.setLayoutManager(linearLayoutManager);
        dataListView.setItemAnimator(new DefaultItemAnimator());

        dataList = new ArrayList<>();

        mAdapter = new RepoItemAdapter(HomeActivity.this, dataList);
        dataListView.setAdapter(mAdapter);

        // select the previous duration values
        highlightDuration();

        // fetch values from network
        makeNetworkCall();
    }

    private void refreshList(String language, String duration) {

        // fetch the list from db
        new FetchRepoItemTask(language, duration).execute();
    }

    private void hideInputBox() {
        //hide the autocomplete itself
        languageAutoCompleteView.setVisibility(View.GONE);

        //close the soft keyboard first
        Utils.hideKeyboard(HomeActivity.this);
    }

    private void showInputBox() {
        //make autocomplete visible again
        languageAutoCompleteView.setVisibility(View.VISIBLE);

        //opens the keyboard if autocomplete is visible
        Utils.showKeyboard(HomeActivity.this, languageAutoCompleteView);
    }


    private void highlightDuration() {
        String searchedDuration = prefs.getString(Constants.DURATION, Constants.MONTH);
        switch (searchedDuration) {
            case Constants.TODAY:
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setSubtitle(getString(R.string.today));
                }
                break;
            case Constants.WEEK:
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setSubtitle(getString(R.string.last_week));
                }
                break;

            case Constants.MONTH:
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setSubtitle(getString(R.string.last_month));
                }
                break;

            case Constants.YEAR:
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setSubtitle(getString(R.string.last_year));
                }
                break;

            default:
                break;

        }
    }

    private void makeNetworkCall() {
        // read from prefs to find out the value of language and duration
        String searchedLanguage = prefs.getString(Constants.LANGUAGE, "Java");
        String searchedDuration = prefs.getString(Constants.DURATION, Constants.MONTH);
        makeNetworkCall(searchedLanguage, searchedDuration);
    }

    private void makeNetworkCall(final String selectedLanguage, final String duration) {
        Map<String, String> params = new HashMap<>();
//        q=language:" + selectedLanguage + " created:\"2017-02-26 .. 2017-03-26\"";

        String lowerLimit;
        String todayDate = Utils.getCreatedDateFromPeriod(Constants.TODAY);

        //create the time-period from duration
        switch (duration) {
            case Constants.TODAY:
                lowerLimit = todayDate;
                break;

            case Constants.WEEK:
                lowerLimit = Utils.getCreatedDateFromPeriod(Constants.WEEK);
                break;

            case Constants.MONTH:
                lowerLimit = Utils.getCreatedDateFromPeriod(Constants.MONTH);
                break;

            case Constants.YEAR:
                lowerLimit = Utils.getCreatedDateFromPeriod(Constants.YEAR);
                break;

            default:
                lowerLimit = todayDate;
                break;
        }

        final String dateRange = "\"" + lowerLimit + " .. " + todayDate + "\"";

        // error message
        // the lower bound of the range (2017-03-26 .. 2017-02-26) is greater than the upper bound
        params.put("q", "language:" + selectedLanguage + " created:" + dateRange);

        Call<RepoItem> repoItemCall = NetworkService.fetchRepos(params);

        progressbar.setVisibility(View.VISIBLE);

        repoItemCall.enqueue(new Callback<RepoItem>() {
            @Override
            public void onResponse(Call<RepoItem> call, Response<RepoItem> response) {

                progressbar.setVisibility(View.GONE);

                RepoItem repoItem = response.body();

                // save the list into db
                dbHelper.addRepoList(repoItem.getItems(), selectedLanguage, duration, dateRange);

                // fetch items from db to display
                // fire an async task

                refreshList(selectedLanguage, duration);
            }

            @Override
            public void onFailure(Call<RepoItem> call, Throwable t) {
                progressbar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        String timePeriod = "";

        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.today:
                timePeriod = getString(R.string.today);
                prefs.edit().putString(Constants.DURATION, Constants.TODAY).commit();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setSubtitle(timePeriod);
                }

                // make network call with changed duration
                // duration values already written in prefs
                makeNetworkCall();
                break;

            case R.id.last_week:
                timePeriod = getString(R.string.last_week);
                prefs.edit().putString(Constants.DURATION, Constants.WEEK).commit();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setSubtitle(timePeriod);
                }

                // make network call with changed duration
                // duration values already written in prefs
                makeNetworkCall();
                break;

            case R.id.last_month:
                timePeriod = getString(R.string.last_month);
                prefs.edit().putString(Constants.DURATION, Constants.MONTH).commit();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setSubtitle(timePeriod);
                }

                // make network call with changed duration
                // duration values already written in prefs
                makeNetworkCall();
                break;

            case R.id.last_year:
                timePeriod = getString(R.string.last_year);
                prefs.edit().putString(Constants.DURATION, Constants.YEAR).commit();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setSubtitle(timePeriod);
                }

                // make network call with changed duration
                // duration values already written in prefs
                makeNetworkCall();
                break;


            case R.id.nav_share:
                // TODO: 30/9/17 correct the share intent
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                final String playStoreUrl = "https://play.google.com/store/apps/details?id=" + getPackageName();
                sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_apk_msg) + playStoreUrl);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));

                gaTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("share")
                        .setAction("share")
                        .setLabel("share")
                        .build());

                break;

            //  case R.id.nav_feedback:
            // TODO: 30/9/17
            // will be implemented in future release
            // open a dialog with input of email(optional) and content
            //
            //      break;

            default:
                break;
        }

        gaTracker.send(new HitBuilders.EventBuilder()
                .setCategory("input")
                .setAction("duration")
                .setLabel(timePeriod)
                .build());


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onItemClicked(int position) {
        Repo clickedItem = dataList.get(position);

        // extract the link and open webview
        Intent webActivity = new Intent(HomeActivity.this, BrowserActivity.class);

        webActivity.putExtra(Constants.URL, clickedItem.getHtmlUrl());
        webActivity.putExtra(Constants.TITLE, clickedItem.getFullName());

        startActivity(webActivity);
    }

    // Fetch repo items from db to populate the list
    private class FetchRepoItemTask extends AsyncTask<Void, Void, List<Repo>> {

        private final String language;
        private final String duration;

        FetchRepoItemTask(String language, String duration) {
            this.language = language;
            this.duration = duration;

        }

        @Override
        protected List<Repo> doInBackground(Void... params) {
            return dbHelper.getRepos(language, duration);
        }

        @Override
        protected void onPostExecute(List<Repo> repoList) {
            super.onPostExecute(repoList);

            // if there are no results to show,
            // show the default empty view
            if (repoList.isEmpty()) {
                tvNoResults.setVisibility(View.VISIBLE);
                dataListView.setVisibility(View.GONE);
            } else {
                tvNoResults.setVisibility(View.GONE);
                dataListView.setVisibility(View.VISIBLE);

                dataList.clear();
                dataList.addAll(repoList);

                mAdapter.notifyDataSetChanged();
            }
        }
    }

}
