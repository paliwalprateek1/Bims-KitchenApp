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
    Data data = new Data();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_order_final);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Intent intent = new Intent();
//        intent.setClass(getApplicationContext(), SendOrderFinal.class);

        String place="";
        String latitude= "";

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        place = extras.getString("place");
        latitude = extras.getString("latitude");
        data.setPlace(place);
        data.setCordinates(latitude);


    }

}

class Data{
    private String food;
    private String price;
    private String place;
    private String remarks;

    public Data(){}

    public Data(String food, String price, String place, String remarks, String cordinates, String name){
        this.food = food;
        this.price = price;
        this.place = place;
        this.remarks = remarks;
        this.cordinates = cordinates;
        this.name = name;
    }

    public String getCordinates() {
        return cordinates;
    }

    public void setCordinates(String cordinates) {
        this.cordinates = cordinates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    private String cordinates;
    private String name;
}
