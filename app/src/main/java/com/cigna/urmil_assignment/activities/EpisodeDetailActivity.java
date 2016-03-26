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

import org.json.JSONException;
import org.json.JSONObject;

public class EpisodeDetailActivity extends AppCompatActivity implements AsyncResponse {

    private TextView noDataAvailable;
    private CignaAsyncTask asyncHttpPost;
    private Gson gson;
    private Toolbar toolbar;
    private TextView tv_year_value,tv_rated_value,tv_released_value,tv_season_value,tv_episode_value,tv_runtime_value;
    private String imdbID;
    private static String EPISODE_DETAILS_URL = "http://www.omdbapi.com/?i=<imdbID>&plot=short&r=json";
    private EpisodeDetails episodeDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodedetails);

        if(getIntent().getExtras()!=null){
            imdbID = getIntent().getStringExtra("imdbID");
        }

        tv_year_value = (TextView)this.findViewById(R.id.tv_year_value);
        tv_rated_value = (TextView)this.findViewById(R.id.tv_rated_value);
        tv_released_value = (TextView)this.findViewById(R.id.tv_released_value);
        tv_season_value = (TextView)this.findViewById(R.id.tv_season_value);
        tv_episode_value = (TextView)this.findViewById(R.id.tv_episode_value);
        tv_runtime_value = (TextView)this.findViewById(R.id.tv_runtime_value);


        gson = new Gson();
        fetchEpisodesDetails();
    }

    private void fetchEpisodesDetails() {

        asyncHttpPost = new CignaAsyncTask(EpisodeDetailActivity.this);
        asyncHttpPost.delegate =EpisodeDetailActivity.this;
        String detailsURL = EPISODE_DETAILS_URL.replaceAll("<imdbID>",imdbID);
        asyncHttpPost.execute(detailsURL);
    }

    @Override
    public void processFinish(String output) {

        extractEpisodeDetailsFromResponse(output);
    }

    public void extractEpisodeDetailsFromResponse(String output){

        try {
            JSONObject obj = new JSONObject(output);
            episodeDetails = gson.fromJson(output,EpisodeDetails.class);

            setupToolBar();

            tv_year_value.setText(episodeDetails.getYear());
            tv_rated_value.setText(episodeDetails.getRated());
            tv_released_value.setText(episodeDetails.getReleased());
            tv_episode_value.setText(episodeDetails.getEpisode());
            tv_season_value.setText(episodeDetails.getSeason());
            tv_runtime_value.setText(episodeDetails.getRuntime());

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        }else if(id==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setTitle(episodeDetails.getTitle());
        //ab.setDisplayHomeAsUpEnabled(true);
    }


}
