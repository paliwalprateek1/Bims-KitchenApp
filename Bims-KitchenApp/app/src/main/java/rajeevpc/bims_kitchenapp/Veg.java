package rajeevpc.bims_kitchenapp;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.vision.barcode.internal.client.BarcodeDetectorOptions;
import com.google.android.gms.vision.text.Text;

import java.io.BufferedOutputStream;
import java.util.ArrayList;
import java.util.List;


public class Veg extends Fragment{

    private List<Food> foodList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FoodAdapter mAdapter;
    NumberPicker np;
    ProgressDialog p;
    TextView count;
    Button ua, da,dialogOk;
    FoodQuantity foodQuantity = new FoodQuantity();
    Firebase ref;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private final int CANCEL_DIALOG = 1;
    private Handler mHandler;
    private ProgressDialog mDialog;
    protected ActionBarProvider mActionBar;
    private Handler mHandler2 = new Handler();

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






        Firebase.setAndroidContext(getActivity());
        //mActionBar.setActionBarColor(new ColorDrawable(Color.parseColor("#fff000")));

        ref = new Firebase(Server.URL);
        View view = inflater.inflate(R.layout.fragment_veg, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mAdapter = new FoodAdapter(foodList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                final Food food = foodList.get(position);
                foodQuantity.setFood(food.getFood());
                foodQuantity.setPrice(food.getPrice());

                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_counter);
                dialog.setTitle(food.getFood());
                ImageView image = (ImageView) dialog.findViewById(R.id.image);
                count = (TextView) dialog.findViewById(R.id.count);
                count.setText("0");
                ua = (Button) dialog.findViewById(R.id.buttonUp);
                da = (Button) dialog.findViewById(R.id.buttonDown);

                ua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int s = Integer.parseInt(count.getText().toString());
                        s++;

                        count.setText(Integer.toString(s));
                        //quantityof = Integer.parseInt(count.getText().toString());
                    }
                });

                da.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int s = Integer.parseInt(count.getText().toString());
                        s--;

                        count.setText(Integer.toString(s));
                        //quantityof = Integer.parseInt(count.getText().toString());

                    }
                });

                dialogOk = (Button) dialog.findViewById(R.id.dialogOk);

                dialogOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), count.getText().toString(), Toast.LENGTH_SHORT).show();
                        if(!(count.getText().toString()).equals("0")) {
                            setValue(count.getText().toString());
                            storeData(foodQuantity);
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
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
        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("Fetching Menu....");
        mDialog.show();
        mHandler.sendEmptyMessageDelayed(CANCEL_DIALOG, 6500);
        return view;
    }
    public void setValue(String str){
        foodQuantity.setQuantity(str);
    }
    public void storeData(FoodQuantity fq){
        StoreSharedPreferences s = new StoreSharedPreferences();
        s.addFoodQuantity(getActivity(), fq);
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
        });
    }
}

