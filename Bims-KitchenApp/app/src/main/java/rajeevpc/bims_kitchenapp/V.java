package rajeevpc.bims_kitchenapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import java.util.ArrayList;
import java.util.List;


public class V extends Fragment {

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
    private final int CANCEL_DIALOG = 1;
    private Handler mHandler;
    private ProgressDialog mDialog;

    private Handler mHandler2 = new Handler();

    private OnFragmentInteractionListener mListener;

    public V() {
        // Required empty public constructor
    }

    public static V newInstance(String param1, String param2) {
        V fragment = new V();
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
        View view = inflater.inflate(R.layout.fragment_v, container, false);
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

                try {
                    (new StoreSharedPreferences()).removeFood(getActivity(), food);
                }catch (Exception e){}

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
        getBevMenu();


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
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void getBevMenu(){
        //final Food food = new Food(null, null);
        Food food = new Food("a", "22", "c");
        foodList.add(food);
        mAdapter.notifyDataSetChanged();
        Firebase objRef = ref.child("Menu");
        Query pendingTasks = objRef.orderByChild("cat").equalTo("bev");
        pendingTasks.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                for (DataSnapshot snapshot: tasksSnapshot.getChildren()) {
                    Object value = snapshot.child("f").getValue();
                    Object valueF = snapshot.child("p").getValue();
                    //prepareFoodData(value.toString(), valueF.toString());
                    Food food = new Food(value.toString(), valueF.toString(), StoreSharedPreferences.getImageuri(getActivity()));
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
