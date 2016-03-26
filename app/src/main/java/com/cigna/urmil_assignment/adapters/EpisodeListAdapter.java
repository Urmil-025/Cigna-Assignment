package com.cigna.urmil_assignment.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cigna.urmil_assignment.R;
import com.cigna.urmil_assignment.activities.EpisodeDetailActivity;
import com.cigna.urmil_assignment.model.EpisodeListDetails;

import java.util.ArrayList;
import java.util.Arrays;

public class EpisodeListAdapter extends BaseAdapter implements View.OnClickListener {

    /***********
     * Declare Used Variables
     *********/
    private Context mContext;
    private ArrayList<EpisodeListDetails> data;
    private static LayoutInflater inflater = null;
    public Resources res;
    EpisodeListDetails episodeDetails = null;
    int i = 0;

    /*************
     * EpisodeListAdapter Constructor
     *****************/
    public EpisodeListAdapter(Context mContext, EpisodeListDetails[] episodeDetails) {

        /********** Take passed values **********/
        this.mContext = mContext;
        this.data = new ArrayList<EpisodeListDetails>(Arrays.asList(episodeDetails));

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = (LayoutInflater) mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /********
     * What is the size of Passed Arraylist Size
     ************/
    public int getCount() {

        if (data.size() <= 0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /*********
     * Create a holder Class to contain inflated xml file elements
     *********/
    public static class ViewHolder {
        public TextView title;
    }

    /******
     * Depends upon data size called for each row , Create each ListView row
     *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.row_layout, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.title = (TextView) vi.findViewById(R.id.tv_listItem);

            /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);

        } else {
            holder = (ViewHolder) vi.getTag();
        }

        /***** Get each Model object from Arraylist ********/
        episodeDetails = (EpisodeListDetails) data.get(position);

        /************  Set Model values in Holder elements ***********/
        holder.title.setText(episodeDetails.getTitle());

        /******** Set on Click Listener for LayoutInflater for each row *******/
        holder.title.setTag(episodeDetails);
        holder.title.setOnClickListener(this);

        return vi;
    }

    @Override
    public void onClick(View view) {
        EpisodeListDetails episodeListDetails = (EpisodeListDetails) view.getTag();

        Log.v("Adapter",episodeListDetails.getImdbID());

        Intent i = new Intent(mContext, EpisodeDetailActivity.class);
        // sending data to new activity
        i.putExtra("imdbID", episodeListDetails.getImdbID());
        mContext.startActivity(i);


    }
}