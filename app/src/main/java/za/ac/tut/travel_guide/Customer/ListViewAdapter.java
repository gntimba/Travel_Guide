package za.ac.tut.travel_guide.Customer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import za.ac.tut.travel_guide.DownloadImageTask;
import za.ac.tut.travel_guide.R;
import za.ac.tut.travel_guide.viewDetails;

import static za.ac.tut.travel_guide.IP.getIp;


public class ListViewAdapter extends BaseAdapter {

	// Declare Variables
	Context context;
	LayoutInflater inflater;
	ArrayList<HashMap<String, String>> data;
	private static final String baseUrlForImage =getIp()+ "places/";
	double currLO,currLa;
	//ImageLoader imageLoader;
	HashMap<String, String> resultp = new HashMap<String, String>();

	public ListViewAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
		this.context = context;
		data = arraylist;
		this.currLa=currLa;
		this.currLO=currLO;
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
		TextView Street;
		 TextView Suburb;
		 TextView Distance = null;
	 		TextView code;
		 ImageView imageP;
		final TextView price;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View itemView = inflater.inflate(R.layout.custom_user_main, parent, false);
		// Get the position
		resultp = data.get(position);


		Name = (TextView) itemView.findViewById(R.id.PlaceNames);
		Street = (TextView) itemView.findViewById(R.id.stradd);
		imageP = (ImageView) itemView.findViewById(R.id.ima);
		Suburb = (TextView) itemView.findViewById(R.id.subP);
		code = (TextView) itemView.findViewById(R.id.codeP);
		Distance=(TextView)itemView.findViewById(R.id.distanceP) ;
		price=(TextView)itemView.findViewById(R.id.price);

		// Capture position and set results to the TextViews
		Name.setText(resultp.get("name"));
		String[] spilt =(resultp.get("address")).split("#");
		Street.setText(spilt[0]);
		Suburb.setText(spilt[1]);
		code.setText(spilt[2]);
		price.setText(resultp.get("price"));

		Distance.setText(resultp.get("distance")+" km");
		final String urlForImage = baseUrlForImage + resultp.get("imagename");

		new DownloadImageTask(imageP).execute(urlForImage);
		// Capture ListView item click
		itemView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Get the position
				resultp = data.get(position);
				Intent intent = new Intent(context,viewDetails.class);
				// Pass all data rank
				intent.putExtra("name", resultp.get("name"));
				//intent.putExtra("email",resultp.get("email"));
				// Pass all data country
				intent.putExtra("address", resultp.get("address"));
				// Pass all data population
				intent.putExtra("latitude",resultp.get("latitude"));
				intent.putExtra("longitude",resultp.get("longitude"));
				intent.putExtra("distance",resultp.get("distance"));
				// Pass all data flag
				intent.putExtra("imageurl", urlForImage);
				intent.putExtra("price", resultp.get("price"));
				intent.putExtra("place_id",resultp.get("place_id"));
				// Start SingleItemView Class
				context.startActivity(intent);

			}
		});
		return itemView;
	}
}
