package com.cigna.urmil_assignment.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.cigna.urmil_assignment.R;
import com.cigna.urmil_assignment.adapters.EpisodeListAdapter;
import com.cigna.urmil_assignment.background.AsyncResponse;
import com.cigna.urmil_assignment.background.CignaAsyncTask;
import com.cigna.urmil_assignment.model.EpisodeListDetails;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EpisodeListActivity extends AppCompatActivity implements AsyncResponse {

    private Gson gson;
    private ListView episodelist;
    private static String EPISODE_URL = "http://www.omdbapi.com/?t=Game%20of%20Thrones&Season=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_list);

        episodelist = (ListView)this.findViewById(R.id.episodelist);
        gson = new Gson();
        fetchEpisodesList();
    }

    private void fetchEpisodesList() {
        CignaAsyncTask asyncHttpPost = new CignaAsyncTask(EpisodeListActivity.this);
        asyncHttpPost.delegate =EpisodeListActivity.this;
        asyncHttpPost.execute(EPISODE_URL);
    }

    @Override
    public void processFinish(String output) {
        extractEpisodesFromResponse(output);
    }

    public void extractEpisodesFromResponse(String output){
        try {
            JSONObject obj = new JSONObject(output);
            String activityName = obj.getString("Title");
            setupToolBar(activityName);

            JSONArray m_jArry = obj.getJSONArray("Episodes");
            EpisodeListDetails[] episodeListDetails = gson.fromJson(m_jArry.toString(), EpisodeListDetails[].class);
            EpisodeListAdapter episodeListAdapter = new EpisodeListAdapter(this,episodeListDetails);
            // assign the list adapter
            episodelist.setAdapter(episodeListAdapter);

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
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupToolBar(String activityName) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setTitle(activityName);
        ab.setDisplayHomeAsUpEnabled(false);
    }
}
