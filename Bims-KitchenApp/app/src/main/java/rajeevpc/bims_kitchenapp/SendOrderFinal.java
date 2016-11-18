package rajeevpc.bims_kitchenapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SendOrderFinal extends AppCompatActivity {

    private static List<Food> order = new ArrayList<>();
    StoreSharedPreferences storeSharedPreferences = new StoreSharedPreferences();
    Data data = new Data();
    Firebase ref;
    String place="";
    String latitude= "";
    String specialRemarks="";
    String price = "";
    String itemOrderString="";
    String itemOrderStringSend="";
    String itemQuantString="";
    String itemValueString="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_order_final);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Firebase.setAndroidContext(this);
        ref = new Firebase(Server.URL);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        place = extras.getString("place");
        latitude = extras.getString("latitude");
        specialRemarks = extras.getString("specialRemarks");
        price = extras.getString("price");
        itemOrderString = extras.getString("itemOrderString");
        itemOrderStringSend=extras.getString("itemOrderStringSend");
        itemValueString=extras.getString("itemValueString");
        itemQuantString=extras.getString("itemQuantString");


        TextView placeFinal =(TextView) findViewById(R.id.placeFinal);
        TextView name =(TextView) findViewById(R.id.name);

        TextView number =(TextView) findViewById(R.id.number);
        TextView amount =(TextView) findViewById(R.id.amount);
        TextView remarksFinal =(TextView) findViewById(R.id.remarksFinal);
        TextView order = (TextView) findViewById(R.id.order);
        TextView quantiss =(TextView) findViewById(R.id.quantityiss);
        TextView pricess = (TextView) findViewById(R.id.pricess);

        order.setText(itemOrderString);
        quantiss.setText(itemQuantString);
        pricess.setText(itemValueString);

        placeFinal.setText(place);
        name.setText(StoreSharedPreferences.getUserName(this));
        number.setText(StoreSharedPreferences.getUserNumber(this));
        amount.setText(price);
        remarksFinal.setText(specialRemarks);


        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        data.setPlace(place);
        data.setCordinates(latitude);
        data.setRemarks(specialRemarks);
        data.setEmail(StoreSharedPreferences.getUserEmail(this));
        data.setNumber(StoreSharedPreferences.getUserNumber(this));
        data.setName(StoreSharedPreferences.getUserName(this));
        data.setFood(itemOrderStringSend);
        data.setPrice(price);
        data.setOrderStatus("pending");
        data.setDate(date);


    }


    public void sendOrderFinalUltimate(Data data){
        Firebase.setAndroidContext(getApplicationContext());
        ref = new Firebase(Server.URL);
        Firebase newRef = ref.child("Order").push();
        newRef.setValue(data);
        String r = newRef.getKey();
    }

    public void sendOrderFinalMaaKasam(View view) {
        sendOrderFinalUltimate(data);
        Toast.makeText(this, "Ordered", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MenuPage.class);
        startActivity(intent);
        finish();
    }
}

class Data{
    private String food;
    private String price;
    private String place;
    private String remarks;
    private String cordinates;
    private String name;
    private String email;
    private String number;
    private String orderStatus;
    private String date;

    public Data(){}

    public Data(String food, String price, String place, String remarks, String cordinates,
                String name, String email, String number, String orderStatus, String date){
        this.food = food;
        this.price = price;
        this.place = place;
        this.remarks = remarks;
        this.cordinates = cordinates;
        this.name = name;
        this.number = number;
        this.email = email;
        this.orderStatus= orderStatus;
        this.date = date;
    }

    public String getDate(){return date;}

    public void setDate(String date){this.date = date;}
    public String getOrderStatus(){return orderStatus;}

    public void setOrderStatus(String orderStatus){this.orderStatus = orderStatus;}

    public String getCordinates() {
        return cordinates;
    }

    public void setCordinates(String cordinates) {
        this.cordinates = cordinates;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

}
