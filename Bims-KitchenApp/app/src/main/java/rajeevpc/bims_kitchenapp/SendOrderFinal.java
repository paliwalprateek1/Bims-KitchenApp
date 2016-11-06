package rajeevpc.bims_kitchenapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SendOrderFinal extends AppCompatActivity {

    private static List<Food> order = new ArrayList<>();
    StoreSharedPreferences storeSharedPreferences = new StoreSharedPreferences();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_order_final);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Intent intent = new Intent();
//        intent.setClass(getApplicationContext(), SendOrderFinal.class);

        String s="";
//        Bundle extras = new Bundle();
//        extras.putString("place",s);
//        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
//        intent.putExtras(extras);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        s = extras.getString("place");
        Toast.makeText(this, s+"daa", Toast.LENGTH_SHORT).show();


    }

}
