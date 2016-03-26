package com.cigna.urmil_assignment.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.cigna.urmil_assignment.R;
import com.cigna.urmil_assignment.background.AsyncResponse;
import com.cigna.urmil_assignment.background.CignaAsyncTask;
import com.cigna.urmil_assignment.model.EpisodeDetails;
import com.google.gson.Gson;

public class EpisodeDetailActivity extends AppCompatActivity implements AsyncResponse {

    private Gson gson;
    private static String EPISODE_DETAILS_URL = "http://www.omdbapi.com/?i=<imdbID>&plot=short&r=json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodedetails);

        //Initialize Gson
        gson = new Gson();

        // Get imdbID from the previous List activity
        if (getIntent().getExtras() != null) {
            String imdbID = getIntent().getStringExtra("imdbID");
            fetchEpisodesDetails(imdbID);
        }
    }

    /**
     * Fetch Episode details
     *
     * @param strImdbID
     */
    private void fetchEpisodesDetails(String strImdbID) {

        CignaAsyncTask asyncHttpPost = new CignaAsyncTask(EpisodeDetailActivity.this);
        asyncHttpPost.delegate = EpisodeDetailActivity.this;
        String detailsURL = EPISODE_DETAILS_URL.replaceAll("<imdbID>", strImdbID);
        asyncHttpPost.execute(detailsURL);
    }

    @Override
    public void processFinish(String output) {
        extractEpisodeDetailsFromResponse(output);
    }


    public void extractEpisodeDetailsFromResponse(String output) {

        EpisodeDetails episodeDetails = gson.fromJson(output, EpisodeDetails.class);
        //Initialize Toolbar and set title using the EpisodeDetails
        setupToolBar(episodeDetails);
        //Update UI with the data
        updateUI(episodeDetails);

    }

    public void updateUI(EpisodeDetails episodeDetails){

        //Initialize all UI elements
        TextView tv_year_value = (TextView) this.findViewById(R.id.tv_year_value);
        TextView tv_rated_value = (TextView) this.findViewById(R.id.tv_rated_value);
        TextView tv_released_value = (TextView) this.findViewById(R.id.tv_released_value);
        TextView tv_season_value = (TextView) this.findViewById(R.id.tv_season_value);
        TextView tv_episode_value = (TextView) this.findViewById(R.id.tv_episode_value);
        TextView tv_runtime_value = (TextView) this.findViewById(R.id.tv_runtime_value);

        tv_year_value.setText(episodeDetails.getYear());
        tv_rated_value.setText(episodeDetails.getRated());
        tv_released_value.setText(episodeDetails.getReleased());
        tv_episode_value.setText(episodeDetails.getEpisode());
        tv_season_value.setText(episodeDetails.getSeason());
        tv_runtime_value.setText(episodeDetails.getRuntime());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_episode_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupToolBar(EpisodeDetails episodeDetails) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setTitle(episodeDetails.getTitle());
    }


}
