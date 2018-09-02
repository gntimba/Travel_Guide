package za.ac.tut.travel_guide.Customer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;
import com.like.OnLikeListener;

import za.ac.tut.travel_guide.R;
import za.ac.tut.travel_guide.backWorker;
import za.ac.tut.travel_guide.util.email;

public class review extends AppCompatActivity {
RatingBar ratingBar;
    TextView status;
    EditText comment;
    float rate;
   // String cust_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ratingBar=(RatingBar)findViewById(R.id.ratingBar);
        status=(TextView)findViewById(R.id.txtReview);
        comment=(EditText)findViewById(R.id.comment);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rate=rating;
                if(rating==1)
                    status.setText("Hated it");
                else if(rating==2)
                    status.setText("Disliked it");
                else if(rating==3)
                    status.setText("It's Ok");
                else if(rating==4)
                    status.setText("Liked it");
                else if(rating==5)
                    status.setText("Loved it");
                else if(rating==0)
                    status.setText("Please Rate the place");

            }
        });


    }
    public void raview(View v){
        if(comment.getText().toString().length() < 2)
        {
            comment.setError("Please enter valid comment");
        }
       // place_id=getIntent().getExtras().getString("place_id");
        //cust_email=getIntent().getExtras().getString("email");
        if(rate==0)
            Toast.makeText(this, "Please Rate the place", Toast.LENGTH_SHORT).show();
        if(comment.getText().toString().length() >= 2&&rate>0){
            int value=(int)rate;
            String type="review";
            //Toast.makeText(this, email.getPlace_id(), Toast.LENGTH_SHORT).show();
            backWorker BackWorker=new backWorker(this);
            BackWorker.execute(type, email.getPlace_id(),comment.getText().toString(),value+"",email.getEmail());

        }
    }
}
