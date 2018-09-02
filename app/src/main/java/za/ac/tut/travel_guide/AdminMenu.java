package za.ac.tut.travel_guide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AdminMenu extends AppCompatActivity {
Button button;
    TextView admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);
        button=(Button)findViewById(R.id.addP);
        admin=(TextView)findViewById(R.id.admins);
        admin.setText(getIntent().getExtras().getString("email"));

    }
    public void add(View v){
        Intent intent= new Intent(AdminMenu.this,addPlace.class);
        intent.putExtra("email",admin.getText());
        startActivity(intent);
    }
}
