package rajeevpc.bims_kitchenapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FeedBackForm extends AppCompatActivity {

    List<FeedBackData> feedBackData = new ArrayList<>();
    private RecyclerView recyclerView;
    Firebase ref;
    private FeedBackFormAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Firebase.setAndroidContext(this);
        ref = new Firebase(Server.URL);

        getFeedbacks();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_feedback_form);
        mAdapter = new FeedBackFormAdapter(feedBackData);
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


    public void getFeedbacks(){
        Firebase objRef = ref.child("Feedback");
        Query pendingTasks = objRef.orderByValue();
        pendingTasks.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                for (DataSnapshot snapshot: tasksSnapshot.getChildren()) {
                    Object valueName = snapshot.child("name").getValue();
                    Object valueRating = snapshot.child("rating").getValue();
                    Object valueFeedback = snapshot.child("feedback").getValue();

                    FeedBackData fbd = new FeedBackData();
                    fbd.setName(valueName.toString());
                    fbd.setRating(valueRating.toString());
                    fbd.setFeedback(valueFeedback.toString());
                    feedBackData.add(fbd);
                    mAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    public void goToSubmitFeedback(View view) {
        Intent intent = new Intent(this, GiveFeedback.class);
        startActivity(intent);
        finish();
    }
}
