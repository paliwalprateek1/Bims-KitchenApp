package rajeevpc.bims_kitchenapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prateek on 6/11/16.
 */
public class ProceedFoodAdapter extends RecyclerView.Adapter<ProceedFoodAdapter.OtherViewHolder> {

    private List<FoodQuantity> foodList;


    public class OtherViewHolder extends RecyclerView.ViewHolder {


        public TextView food_quant, price_quant, quantity;

        public OtherViewHolder(View view) {


            super(view);
            food_quant = (TextView) view.findViewById(R.id.food_quant);
            price_quant = (TextView) view.findViewById(R.id.price_quant);
            quantity =(TextView) view.findViewById(R.id.quantity);
        }
    }


    public ProceedFoodAdapter(List<FoodQuantity> foodList) {
        this.foodList = foodList;
    }

    @Override
    public OtherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.proceed_food_order_card, parent, false);

        return new OtherViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(OtherViewHolder holder, int position) {
        FoodQuantity food = foodList.get(position);

        if(!food.getQuantity().equals("0")) {
            holder.food_quant.setText(food.getFood());
            holder.price_quant.setText("    X    "+food.getQuantity() + "      =");
            int i = Integer.parseInt(food.getQuantity());
            int j = Integer.parseInt(food.getPrice());
            int value = i * j;
            holder.quantity.setText(Integer.toString(value));
        }
    }


    @Override
    public int getItemCount() {
        return foodList.size();
    }
}
