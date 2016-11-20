package rajeevpc.bims_kitchenapp;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class NonVeg extends Fragment {

    private List<Food> foodList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FoodAdapter mAdapter;
    FoodQuantity foodQuantity = new FoodQuantity();
    Button ua, da,dialogOk;
    Firebase ref;
    TextView count;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NonVeg() {
    }

    public static NonVeg newInstance(String param1, String param2) {
        NonVeg fragment = new NonVeg();
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


        Firebase.setAndroidContext(getContext());
        ref = new Firebase(Server.URL);
        View view = inflater.inflate(R.layout.fragment_non_veg, container, false);

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

                final Food food = foodList.get(position);
                foodQuantity.setFood(food.getFood());
                foodQuantity.setPrice(food.getPrice());

                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_counter);
                dialog.setTitle(food.getFood());
                ImageView image = (ImageView) dialog.findViewById(R.id.image);
                count = (TextView) dialog.findViewById(R.id.count);
                count.setText("0");
                ua = (Button) dialog.findViewById(R.id.buttonUp);
                da = (Button) dialog.findViewById(R.id.buttonDown);


                ImageView dialogImage = (ImageView) dialog.findViewById(R.id.dialogImageBox);

                Picasso.with(dialogImage.getContext())
                        .load(food.getImageUrl())
                        .transform(new CircleTransform())
                        .into(dialogImage);

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
                        //Toast.makeText(getContext(), count.getText().toString(), Toast.LENGTH_SHORT).show();
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
            getNonVegMenu();
        }
        return view;
    }


    HashMap<String, Integer> hmPrice = new HashMap<>();
    HashMap<String, Integer> hmQuant = new HashMap<>();
    FoodQuantity fa = new FoodQuantity();


    public void setValue(String str){
        foodQuantity.setQuantity(str);
    }
    public void storeData(FoodQuantity fq){
        StoreSharedPreferences s = new StoreSharedPreferences();
        s.addFoodQuantity(getActivity(), fq);
//        StoreSharedPreferences s = new StoreSharedPreferences();
//        hmPrice.put(fq.getFood(), Integer.parseInt(fq.getPrice()));
//        hmQuant.put(fq.getFood(), Integer.parseInt(fq.getQuantity()));
//
//
//        System.out.println("here211");
//        s.removeAllNonQuant(getActivity());
//        Iterator it = hmPrice.entrySet().iterator();
//        while (it.hasNext()) {
//            System.out.println("hereregaadfg2");
//            Map.Entry pair = (Map.Entry)it.next();
//            System.out.println(pair.getKey() + " = " + pair.getValue());
//
//            for(Map.Entry<String, Integer> entry: hmPrice.entrySet()) {
//                System.out.println(entry.getKey() + " : " + entry.getValue() + " : "+  hmQuant.get(pair.getKey()));
//                fa.setFood(pair.getKey().toString());
//                fa.setPrice(Integer.toString(hmPrice.get(pair.getKey())));
//                fa.setQuantity(Integer.toString(hmQuant.get(pair.getKey())));
//            }
//            s.addFoodNonQuantity(getActivity(), fa);
//        }
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


    public void getNonVegMenu(){
        Firebase objRef = ref.child("Menu");
        Query pendingTasks = objRef.orderByChild("cat").equalTo("Nonveg");
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
