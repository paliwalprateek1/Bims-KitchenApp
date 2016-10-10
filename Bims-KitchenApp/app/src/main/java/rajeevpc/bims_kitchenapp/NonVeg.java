package rajeevpc.bims_kitchenapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class NonVeg extends Fragment {

    private List<Food> foodList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FoodAdapter mAdapter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
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
                Food food = foodList.get(position);
                Toast.makeText(getActivity(), food.getFood() + " is added to your cart", Toast.LENGTH_SHORT).show();
                StoreSharedPreferences s = new StoreSharedPreferences();
                s.addFavorite(getContext(), food);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareFoodData();
        return view;    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

  /*  @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void prepareFoodData() {
        Food food = new Food("Non-Veg Maggie", "30 Rs");
        foodList.add(food);

        food = new Food("Non-Veg Burger", "60 Rs");
        foodList.add(food);

        food = new Food("Non-Veg Pizza", "90 Rs");
        foodList.add(food);

        food = new Food("Non-Veg Sandwich", "120 Rs");
        foodList.add(food);

        food = new Food("Non-Veg Maggie", "30 Rs");
        foodList.add(food);

        food = new Food("Non-Veg Burger", "60 Rs");
        foodList.add(food);

        food = new Food("Non-Veg Pizza", "90 Rs");
        foodList.add(food);

        food = new Food("Non-Veg Sandwich", "120 Rs");
        foodList.add(food);
        food = new Food("Non-Veg Maggie", "30 Rs");
        foodList.add(food);

        food = new Food("Non-Veg Burger", "60 Rs");
        foodList.add(food);

        food = new Food("Non-Veg Pizza", "90 Rs");
        foodList.add(food);

        food = new Food("Non-Veg Sandwich", "120 Rs");
        foodList.add(food);

        mAdapter.notifyDataSetChanged();
    }

}
