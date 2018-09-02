package za.ac.tut.travel_guide.review;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import za.ac.tut.travel_guide.DownloadImageTask;
import za.ac.tut.travel_guide.R;
import za.ac.tut.travel_guide.viewDetails;

import static za.ac.tut.travel_guide.IP.getIp;

/**
 * Created by Thulani on 2017/11/15.
 */

public class adapterReview extends BaseAdapter {
    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    //ImageLoader imageLoader;
    HashMap<String, String> resultp = new HashMap<String, String>();

    public adapterReview(Context context, ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
        //imageLoader = new ImageLoader(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // Declare Variables
        TextView Name;
        TextView date;
        TextView comment;
        RatingBar ratingBar;


        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.activity_review_custom, parent, false);
        // Get the position
        resultp = data.get(position);


        Name = (TextView) itemView.findViewById(R.id.fullname);
        date = (TextView) itemView.findViewById(R.id.fulldate);
        comment = (TextView) itemView.findViewById(R.id.reviewComent);
        ratingBar=(RatingBar) itemView.findViewById(R.id.ratingReview);


        // Capture position and set results to the TextViews
        Name.setText(resultp.get("fname")+" "+resultp.get("lname"));
        date.setText(resultp.get("date"));
        comment.setText(resultp.get("comment"));

        ratingBar.setRating((float) Double.parseDouble(resultp.get("rating")));


        // Capture ListView item click

        return itemView;
    }
}
