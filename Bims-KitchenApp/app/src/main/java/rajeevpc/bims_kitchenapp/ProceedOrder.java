package rajeevpc.bims_kitchenapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProceedOrder extends AppCompatActivity {

    private static List<Food> order = new ArrayList<>();
    StoreSharedPreferences storeSharedPreferences = new StoreSharedPreferences();
    private RecyclerView recyclerView;
    private FoodAdapter mAdapter;

    Firebase ref;
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

    public void confirmOrder(View view) {
        int size = order.size();
        String fOrder="";
        String itemOrderString="";
        int value=0;
        for(int i=0;i<size;i++){
            String s = order.get(i).getFood()+"\t\t\t\t"+"-"+"\t\t\t\t"+order.get(i).getPrice()+"\n";
            fOrder = s+fOrder;
            String ss = order.get(i).getFood();
            itemOrderString = ss+itemOrderString;
            value = value + Integer.parseInt(order.get(i).getPrice());
        }

        Firebase.setAndroidContext(getApplicationContext());
        ref = new Firebase(Server.URL);

        final OrderSend os = new OrderSend();
        os.setAmount(String.valueOf(value));
        os.setItemString(itemOrderString);
        os.setLatitude("olay");
        os.setLongitude("yalo");
        os.setLatitude("prateekp987@gmail.com");
        Firebase newRef = ref.child("Order").push();
        newRef.setValue(os);

        Toast.makeText(ProceedOrder.this, "You have ordered" +size+"items.", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(ProceedOrder.this);
        builder.setTitle("YOUR ORDER").setMessage(fOrder)
                .setPositiveButton("OrderSend", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // FIRE ZE MISSILES!
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        builder.create().show();
    }
}

