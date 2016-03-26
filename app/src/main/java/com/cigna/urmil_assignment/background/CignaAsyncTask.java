package com.cigna.urmil_assignment.background;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import com.cigna.urmil_assignment.utils.JSONParser;

import org.json.JSONObject;

public class CignaAsyncTask extends AsyncTask<String, String, String> {
    private ProgressDialog pDialog;
    public AsyncResponse delegate;
    private Context mContext;

    public CignaAsyncTask(Context ctx) {
        mContext = ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pDialog = ProgressDialog.show(mContext, "Episodes", "Please wait...");
    }

    @Override
    protected String doInBackground(String... args) {
        JSONParser jParser = new JSONParser();
        // Getting JSON from URL
        JSONObject json = jParser.getJSONFromUrl(args[0]);
        return json.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        pDialog.dismiss();
        delegate.processFinish(result);
    }
}
