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
import android.widget.FrameLayout;
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
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


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
    private List<FoodQuantity> orderChange = new ArrayList<>();
    private Handler mHandler;
    private ProgressDialog mDialog;
    private final int CANCEL_DIALOG = 1;
    private Handler mHandler2 = new Handler();
    private OnFragmentInteractionListener mListener;

    HashMap<String, Integer> hmPrice = new HashMap<>();
    HashMap<String, Integer> hmQuant = new HashMap<>();

    public Veg() {
    }

    public static Veg newInstance(String param1, String param2) {
        Veg fragment = new Veg();
        Bundle args = new Bundle();
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



                StoreSharedPreferences s = new StoreSharedPreferences();
                s.removeFavoriteQuantity(getActivity(), foodQuantity);

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
                    }
                });

                da.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int s = Integer.parseInt(count.getText().toString());
                        if(s>0) {
                            s--;
                            count.setText(Integer.toString(s));
                        }
                    }
                });


                dialogOk = (Button) dialog.findViewById(R.id.dialogOk);

                dialogOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getActivity(), count.getText().toString(), Toast.LENGTH_SHORT).show();
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
        if(foodList.size()==0) {
            getVegMenu();
            mHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == CANCEL_DIALOG) {
                        mDialog.cancel();
                    }

                    return false;
                }
            });
            mDialog = new ProgressDialog(getActivity());
            mDialog.setMessage("Fetching Menu....");
            mDialog.show();
            mHandler.sendEmptyMessageDelayed(CANCEL_DIALOG, 7500);
        }


        return view;
    }

    public void setValue(String str){
        foodQuantity.setQuantity(str);
    }
    FoodQuantity fa = new FoodQuantity();


//    public void storeData(FoodQuantity fq){
//        StoreSharedPreferences s = new StoreSharedPreferences();
//
//
//            hmPrice.put(fq.getFood(), Integer.parseInt(fq.getPrice()));
//            hmQuant.put(fq.getFood(), Integer.parseInt(fq.getQuantity()));
//            //s.removeAllVegQuant(getActivity());
//
//            System.out.println("here211");
//            s.removeAllQuant(getActivity());
//            Iterator it = hmPrice.entrySet().iterator();
//            while (it.hasNext()) {
//                System.out.println("hereregaadfg2");
//                Map.Entry pair = (Map.Entry) it.next();
//                System.out.println(pair.getKey() + " = " + pair.getValue());
//
//                for (Map.Entry<String, Integer> entry : hmPrice.entrySet()) {
//                    System.out.println(entry.getKey() + " : " + entry.getValue() + " : " + hmQuant.get(pair.getKey()));
//                    fa.setFood(entry.getKey());
//                    fa.setPrice(Integer.toString(entry.getValue()));
//                    fa.setQuantity(Integer.toString(hmQuant.get(entry.getKey())));
//                    s.addFoodVegQuantity(getActivity(), fa);
//                }
//            }
//            v = s.loadFoodVegQuantity(getActivity());
//            nv = s.loadFoodNonQuantity(getActivity());
//            bv = s.loadFoodBevQuantity(getActivity());
//
//
//            if (v != null) {
//                for (int l = 0; l < v.size(); l++) {
//                    FoodQuantity foff = new FoodQuantity();
//                    foff.setFood(v.get(l).getFood());
//                    foff.setPrice(v.get(l).getPrice());
//                    foff.setQuantity(v.get(l).getQuantity());
//                    s.addFoodQuantity(getActivity(), foff);
//                }
//            }
//            if (nv != null) {
//                for (int l = 0; l < nv.size(); l++) {
//                    FoodQuantity foff = new FoodQuantity();
//                    foff.setFood(nv.get(l).getFood());
//                    foff.setPrice(nv.get(l).getPrice());
//                    foff.setQuantity(nv.get(l).getQuantity());
//                    s.addFoodQuantity(getActivity(), foff);
//                }
//            }
//            if (bv != null) {
//                for (int l = 0; l < bv.size(); l++) {
//                    FoodQuantity foff = new FoodQuantity();
//                    foff.setFood(bv.get(l).getFood());
//                    foff.setPrice(bv.get(l).getPrice());
//                    foff.setQuantity(bv.get(l).getQuantity());
//                    s.addFoodQuantity(getActivity(), foff);
//                }
//            }
//    }

    public void storeData(FoodQuantity fq){
        StoreSharedPreferences s = new StoreSharedPreferences();
        hmPrice.put(fq.getFood(), Integer.parseInt(fq.getPrice()));
        hmQuant.put(fq.getFood(), Integer.parseInt(fq.getQuantity()));


        System.out.println("here211");
        s.removeAllVegQuant(getActivity());
        Iterator it = hmPrice.entrySet().iterator();
        while (it.hasNext()) {
            System.out.println("hereregaadfg2");
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());

            for(Map.Entry<String, Integer> entry: hmPrice.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue() + " : "+  hmQuant.get(pair.getKey()));
                fa.setFood(pair.getKey().toString());
                fa.setPrice(Integer.toString(hmPrice.get(pair.getKey())));
                fa.setQuantity(Integer.toString(hmQuant.get(pair.getKey())));
            }
            s.addFoodVegQuantity(getActivity(), fa);
        }
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
        Firebase objRef = ref.child("Menu");
        Query pendingTasks = objRef.orderByChild("cat").equalTo("Veg");
        pendingTasks.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                for (DataSnapshot snapshot: tasksSnapshot.getChildren()) {
                    Object value = snapshot.child("f").getValue();
                    Object valueF = snapshot.child("p").getValue();
                    Object valueU = snapshot.child("url").getValue();
                    Food food = new Food(value.toString(), valueF.toString(), valueU.toString());
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

