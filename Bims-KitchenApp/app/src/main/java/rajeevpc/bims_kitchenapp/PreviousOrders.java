package rajeevpc.bims_kitchenapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.location.places.Place;

import java.util.ArrayList;
import java.util.List;

public class PreviousOrders extends AppCompatActivity {


    List<PreviousOrderClass> previousOrderClass = new ArrayList<>();
    private RecyclerView recyclerView;
    Firebase ref;
    private PreviousFoodAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_orders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Firebase.setAndroidContext(this);
        ref = new Firebase(Server.URL);


        getPreviousOrder();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_previous_order);
        mAdapter = new PreviousFoodAdapter(previousOrderClass);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

    }



    public void getPreviousOrder(){
        Firebase objRef = ref.child("Order");
        Query pendingTasks = objRef.orderByChild("email").equalTo(StoreSharedPreferences.getUserEmail(PreviousOrders.this));
        pendingTasks.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                for (DataSnapshot snapshot: tasksSnapshot.getChildren()) {
                    Object valueName = snapshot.child("name").getValue();
                    Object valueFood = snapshot.child("food").getValue();
                    Object valueAddress = snapshot.child("place").getValue();
                    Object valuePrice = snapshot.child("price").getValue();
                    Object valueDate = snapshot.child("date").getValue();
                    PreviousOrderClass food = new PreviousOrderClass();
                    food.setDate(valueDate.toString());
                    food.setPrice(valuePrice.toString());
                    food.setAddress(valueAddress.toString());
                    food.setName(valueName.toString());
                    food.setOrder(valueFood.toString());
                    previousOrderClass.add(food);
                    mAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

}
