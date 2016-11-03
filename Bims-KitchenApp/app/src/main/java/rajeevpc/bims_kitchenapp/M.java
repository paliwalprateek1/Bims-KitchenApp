package rajeevpc.bims_kitchenapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class M extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m);
    }

    public void Login(View view) {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }

    public void menu(View view) {
        Intent intent = new Intent(this, MenuPage.class);
        String ss ;
        ss = StoreSharedPreferences.getUserEmail(this);
        Toast.makeText(this, ss, Toast.LENGTH_SHORT).show();
        startActivity(intent);
/*
        ProgressDialog pd = new ProgressDialog(MenuPage.this);
        pd.setMessage("loading");
        pd.show();
        */
    }
}
