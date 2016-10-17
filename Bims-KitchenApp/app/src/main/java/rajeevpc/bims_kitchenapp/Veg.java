package rajeevpc.bims_kitchenapp;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Veg extends Fragment{

    private List<Food> foodList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FoodAdapter mAdapter;
    ProgressDialog p;
    Firebase ref;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private final int CANCEL_DIALOG = 1;
    private Handler mHandler;
    private ProgressDialog mDialog;

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1000; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 3600000; //0 in Milliseconds
    protected LocationManager locationManager;

    MenuPage order = new MenuPage();

    private OnFragmentInteractionListener mListener;

    public Veg() {
    }

    public static Veg newInstance(String param1, String param2) {
        Veg fragment = new Veg();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //StoreSharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Firebase.setAndroidContext(getContext());
        ref = new Firebase(Server.URL);


        View view = inflater.inflate(R.layout.fragment_veg, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mAdapter = new FoodAdapter(foodList);


        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Food food = foodList.get(position);
                Log.d("adf"+food.getFood().toString(), "adsf");
                Toast.makeText(getActivity(), food.getFood() + " is added to your cart", Toast.LENGTH_SHORT).show();

                //StoreSharedPreferences.setPrice(getContext(), food.getPrice());
                StoreSharedPreferences s = new StoreSharedPreferences();
                s.addFavorite(getContext(), food);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        getVegMenu();
        mHandler = new Handler(new Handler.Callback()
        {
            @Override
            public boolean handleMessage(Message msg)
            {
                if(msg.what == CANCEL_DIALOG)
                {
                    mDialog.cancel();
                }

                return false;
            }
        });




        mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("Fetching Menu....");
        mDialog.show();
        mHandler.sendEmptyMessageDelayed(CANCEL_DIALOG, 4600);
        //prepareFoodData();

        return view;
    }



    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void prepareFoodData() {
        Food food = new Food("Maggie", "Aukad ke bahar");
        foodList.add(food);

        food = new Food("Burger", "60");
        foodList.add(food);

        food = new Food("Pizza", "90");
        foodList.add(food);

        food = new Food("Sandwich", "120");
        foodList.add(food);

        food = new Food("Maggie", "30");
        foodList.add(food);

        food = new Food("Burger", "60");
        foodList.add(food);

        mAdapter.notifyDataSetChanged();
    }


    private void getVegMenu(){
        //final Food food = new Food(null, null);
        Firebase objRef = ref.child("Menu");
        Query pendingTasks = objRef.orderByChild("cat").equalTo("veg");
        pendingTasks.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                for (DataSnapshot snapshot: tasksSnapshot.getChildren()) {
                    Object value = snapshot.child("f").getValue();
                    Object valueF = snapshot.child("p").getValue();
                    //prepareFoodData(value.toString(), valueF.toString());
                    Food food = new Food(value.toString(), valueF.toString());
                    foodList.add(food);
                    mAdapter.notifyDataSetChanged();

                    Log.d("food "+value.toString(), "price "+valueF.toString());
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });//p.dismiss();
    }
}

