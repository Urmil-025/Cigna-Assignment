package com.cigna.urmil_assignment.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

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

    private TextView noDataAvailable;
    private CignaAsyncTask asyncHttpPost;
    private Gson gson;
    private Toolbar toolbar;
    private ListView episodelist;
    private String activityName;
    private static String EPISODE_URL = "http://www.omdbapi.com/?t=Game%20of%20Thrones&Season=1";
    private static String EPISODE_DETAILS_URL = "http://www.omdbapi.com/?i=%1s&plot=short&r=json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_list);

        episodelist = (ListView)this.findViewById(R.id.episodelist);
        gson = new Gson();
        fetchEpisodesList();
    }

    private void fetchEpisodesList() {

        asyncHttpPost = new CignaAsyncTask(EpisodeListActivity.this);
        asyncHttpPost.delegate =EpisodeListActivity.this;
        asyncHttpPost.execute(EPISODE_URL); //"http://www.omdbapi.com/?t=Game%20of%20Thrones&Season=1"
    }

    @Override
    public void processFinish(String output) {

        extractEpisodesFromResponse(output);
    }

    public void extractEpisodesFromResponse(String output){
        EpisodeListDetails[] episodeListDetails;

        try {
            JSONObject obj = new JSONObject(output);
            activityName = obj.getString("Title");
            setupToolBar();

            JSONArray m_jArry = obj.getJSONArray("Episodes");

            episodeListDetails = gson.fromJson(m_jArry.toString(),EpisodeListDetails[].class);

            EpisodeListAdapter episodeListAdapter = new EpisodeListAdapter(this,episodeListDetails);

            // assign the list adapter
            episodelist.setAdapter(episodeListAdapter);

//            for (int i = 0; i < m_jArry.length(); i++) {
//                JSONObject jo_inside = m_jArry.getJSONObject(i);
//                episodeListDetails.setTitle(jo_inside.getString("Title"));
//                episodeListDetails.setImdbID(jo_inside.getString("imdbID"));
//                listValues.add(episodeListDetails.getTitle());
//            }

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

    private void setupToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setTitle(activityName);
        ab.setDisplayHomeAsUpEnabled(false);
    }
}
