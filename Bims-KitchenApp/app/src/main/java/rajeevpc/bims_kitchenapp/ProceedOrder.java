package rajeevpc.bims_kitchenapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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

    private static List<Food> order = new ArrayList<>();
    StoreSharedPreferences storeSharedPreferences = new StoreSharedPreferences();
    private RecyclerView recyclerView;
    private FoodAdapter mAdapter;
    private String latitude, longitude, address;
    int status = 1;
    String itemOrderString="";
    String fOrder="";
    int value=0;
    Firebase ref;
    TextView orderListDialog;

    @Override
    public void onBackPressed() {
        order.clear();
        storeSharedPreferences.removeAll(getApplicationContext());
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceed_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        order = storeSharedPreferences.loadFavorites(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new FoodAdapter(order);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Food food = order.get(position);
                storeSharedPreferences.removeFavorite(getApplicationContext(), food);
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getApplicationContext(), "Swipe to cancel", Toast.LENGTH_SHORT).show();
            }
        }));
        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
            {
                if(order.size()==1){storeSharedPreferences.removeAll(getApplicationContext());
                    finish();}
                else {
                    order.remove(order.get(viewHolder.getAdapterPosition()));
                   // Toast.makeText(ProceedOrder.this, "Removed" + order.size(), Toast.LENGTH_SHORT).show();
                    mAdapter.notifyDataSetChanged();
                }
            }

        });
        swipeToDismissTouchHelper.attachToRecyclerView(recyclerView);



    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100){
            status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        }
        if (requestCode == 199) {
            final int size = order.size();
            Place place = PlacePicker.getPlace(data, this);
            //while (place != null) {
                String toastMsg = String.format("Place: %s", place.getAddress());
                address = place.getAddress().toString();
                latitude = place.getLatLng().toString();
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
               /* AlertDialog.Builder builder = new AlertDialog.Builder(ProceedOrder.this);
                builder.setTitle("Order Summary").setMessage(fOrder)
                        .setPositiveButton("Confirm Order", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Firebase.setAndroidContext(getApplicationContext());
                                ref = new Firebase(Server.URL);
                                final OrderSend os = new OrderSend();
                                os.setAmount(String.valueOf(value));
                                os.setItemString(itemOrderString);
                                os.setLatitude(latitude);
                                os.setLongitude(address);
                                os.setUserMail("prateekp987@gmail.com");
                                Toast.makeText(ProceedOrder.this, "You have ordered" + size + "items.", Toast.LENGTH_SHORT).show();
                                Firebase newRef = ref.child("Order").push();
                                newRef.setValue(os);
                                Toast.makeText(getApplicationContext(), "Ordered", Toast.LENGTH_SHORT).show();
                                order.clear();
                                storeSharedPreferences.removeAll(getApplicationContext());
                                finish();
                            }
                        });
//                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                        }
//                    });
                builder.create().show();*/


                final Dialog dialog = new Dialog(ProceedOrder.this);
                dialog.setContentView(R.layout.dialog_order);
                dialog.setTitle("Order Summary");

//            orderListDialog = (TextView) findViewById(R.id.orderListDialog);
//            orderListDialog.setText(fOrder);

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Firebase.setAndroidContext(getApplicationContext());
                        ref = new Firebase(Server.URL);
                        final OrderSend os = new OrderSend();
                        os.setAmount(String.valueOf(value));
                        os.setItemString(itemOrderString);
                        os.setLatitude(latitude);
                        os.setLongitude(address);
                        os.setUserMail("prateekp987@gmail.com");
                        //Toast.makeText(ProceedOrder.this, "You have ordered" + size + "items.", Toast.LENGTH_SHORT).show();
                        Firebase newRef = ref.child("Order").push();
                        newRef.setValue(os);
                        Toast.makeText(getApplicationContext(), "Ordered", Toast.LENGTH_SHORT).show();
                        order.clear();
                        storeSharedPreferences.removeAll(getApplicationContext());
                        finish();
                        //dialog.dismiss();
                    }
                });
                dialog.show();
          //  }
        }
    }

    public void confirmOrder(View view) {
        int size = order.size();

        for(int i=0;i<size;i++){
            String s = order.get(i).getFood()+"\t\t\t\t"+"-"+"\t\t\t\t"+order.get(i).getPrice()+"\n";
            fOrder = s+fOrder;
            String ss = order.get(i).getFood() + ", ";
            itemOrderString = ss+itemOrderString;
            value = value + Integer.parseInt(order.get(i).getPrice());
        }
        status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(ProceedOrder.this);
        if (status != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
                GooglePlayServicesUtil.getErrorDialog(status, ProceedOrder.this,
                        100).show();
            }
        }
        if (status == ConnectionResult.SUCCESS) {
            int PLACE_PICKER_REQUEST = 199;
            LatLng topLeft = new LatLng(23.179860, 72.649143);
            LatLng bottomRight = new LatLng(23.249227 , 72.652202);
            LatLngBounds bounds = new LatLngBounds(topLeft,bottomRight);
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
}