package rajeevpc.bims_kitchenapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by prateek on 7/10/16.
 */
public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.MyViewHolder> {

    private List<Food> foodList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView food, price;
        public ImageView foodItemIcon;

        public MyViewHolder(View view) {
            super(view);
            food = (TextView) view.findViewById(R.id.food);
            price = (TextView) view.findViewById(R.id.price);
            foodItemIcon = (ImageView) view.findViewById(R.id.foodItemIcon);
        }
    }


    public FoodAdapter(List<Food> foodList) {
        this.foodList = foodList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_item, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Food food = foodList.get(position);
        holder.food.setText(food.getFood());
        holder.price.setText(food.getPrice());
        Bitmap image=null;

        Picasso.with(holder.foodItemIcon.getContext())
                .load("http://sj.uploads.im/bgszo.png")
                .into(holder.foodItemIcon);
//        try {
//            URL url = new URL("http://sj.uploads.im/bgszo.png");
//            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//        } catch(IOException e) {
//            System.out.println(e);
//        }
       // holder.foodItemIcon.setImageBitmap(image);

    }


    @Override
    public int getItemCount() {
        return foodList.size();
    }
}
