package rajeevpc.bims_kitchenapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProceedOrder extends AppCompatActivity {

    private static List<FoodQuantity> order = new ArrayList<>();
    private static List<FoodQuantity> orderV = new ArrayList<>();
    private static List<FoodQuantity> orderN = new ArrayList<>();
    private static List<FoodQuantity> orderB = new ArrayList<>();

    StoreSharedPreferences storeSharedPreferences = new StoreSharedPreferences();
    private RecyclerView recyclerView;
    private ProceedFoodAdapter mAdapter;
    private String latitude, longitude, address;
    int status = 1;
    EditText specialRemarks;
    String itemOrderString="", itemQuantString="", itemValueString="", itemOrderStringSend="";
    String fOrder="", valueP="";
    int value=0;


    @Override
    public void onBackPressed() {
        order.clear();
        storeSharedPreferences.removeAllQuant(getApplicationContext());
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceed_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        order = storeSharedPreferences.loadFoodQuantity(getApplicationContext());
//        orderV = storeSharedPreferences.loadFoodVegQuantity(getApplicationContext());
//
//        orderN = storeSharedPreferences.loadFoodNonQuantity(getApplicationContext());
//
//        orderB = storeSharedPreferences.loadFoodBevQuantity(getApplicationContext());
//
//        if(orderB!=null)
//            order.addAll(orderB);
//        if(orderN!=null)
//            order.addAll(orderN);
//        if(orderV!=null)
//            order.addAll(orderV);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_proceed_order);
        mAdapter = new ProceedFoodAdapter(order);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                FoodQuantity food = order.get(position);
                storeSharedPreferences.removeFavoriteQuantity(getApplicationContext(), food);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        specialRemarks = (EditText) findViewById(R.id.specialRemarks);
    }
    Place place;

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100){
            status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        }
        if (requestCode == 199) {
            if(data!=null) {
                place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getAddress());
                address = place.getAddress().toString();
                latitude = place.getLatLng().toString();

                int size = order.size();

                if (itemValueString.length() == 0) {
                    for (int i = 0; i < size; i++) {
                        int a = Integer.parseInt(order.get(i).getQuantity());
                        int b = Integer.parseInt(order.get(i).getPrice());
                        String ss = order.get(i).getFood() + "\n";
                        String sss = "       X   " + order.get(i).getQuantity() + "    =        " + "\n";
                        String ssss = order.get(i).getPrice() + "\n";
                        itemOrderStringSend = ss + sss + itemOrderStringSend;
                        itemOrderString = ss + itemOrderString;
                        itemQuantString = sss + itemQuantString;
                        itemValueString = ssss + itemValueString;
                        value = value + Integer.parseInt(Integer.toString(a * b));
                    }
                    int a = itemOrderString.length();
                    itemOrderString = itemOrderString.substring(0, a - 1);
                    valueP = Integer.toString(value);

                    Intent intent = new Intent();
                    intent.setClass(this, SendOrderFinal.class);
                    intent.putExtra("place", place.getAddress().toString());
                    intent.putExtra("latitude", place.getLatLng().toString());
                    intent.putExtra("itemOrderString", itemOrderString);
                    intent.putExtra("itemOrderStringSend", itemOrderStringSend);
                    intent.putExtra("itemValueString", itemValueString);
                    intent.putExtra("itemQuantString", itemQuantString);
                    intent.putExtra("price", valueP);

                    if ((specialRemarks.getText().toString()) != null) {
                        intent.putExtra("specialRemarks", specialRemarks.getText().toString());
                        startActivity(intent);
                    } else {
                        intent.putExtra("specialRemarks", "");
                        startActivity(intent);
                    }

                } else {
                    Toast.makeText(this, "Select your location", Toast.LENGTH_SHORT).show();
                }
            }



        }
    }

    public void confirmOrder(View view) {


        status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(ProceedOrder.this);
        if (status != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
                GooglePlayServicesUtil.getErrorDialog(status, ProceedOrder.this,
                        100).show();
            }
        }
        if (status == ConnectionResult.SUCCESS) {
            int PLACE_PICKER_REQUEST = 199;
            LatLng topLeft = new LatLng(0, 0);
            LatLng bottomRight = new LatLng(0,0);
            if(StoreSharedPreferences.getUserCustomLocation(ProceedOrder.this).equals("Gandhinagar")) {
                topLeft = new LatLng(23.179860, 72.649143);
                bottomRight = new LatLng(23.249227, 72.652202);
            }
            else if(StoreSharedPreferences.getUserCustomLocation(ProceedOrder.this).equals("Vadodara")){
                topLeft = new LatLng(22.265240, 73.144044);
                bottomRight = new LatLng(22.381635, 73.195201);
            }
            LatLngBounds bounds = new LatLngBounds(topLeft, bottomRight);
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            builder.setLatLngBounds(bounds);
            //PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            //Context context = this;
            try {
                startActivityForResult(builder.build(ProceedOrder.this), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }
    }

    public void useCustomLocation(View view) {
        order.clear();
        (new StoreSharedPreferences()).removeAllQuant(getApplicationContext());
        (new StoreSharedPreferences()).removeAllVegQuant(getApplicationContext());
        (new StoreSharedPreferences()).removeAllNonQuant(getApplicationContext());
        (new StoreSharedPreferences()).removeAllBevQuant(getApplicationContext());
        finish();
    }
}