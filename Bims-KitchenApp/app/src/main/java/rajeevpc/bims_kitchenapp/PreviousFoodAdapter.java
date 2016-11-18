package rajeevpc.bims_kitchenapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by prateek on 18/11/16.
 */
public class PreviousFoodAdapter extends RecyclerView.Adapter<PreviousFoodAdapter.OtherViewHolder> {

    private List<PreviousOrderClass> prevOrderList;


    public class OtherViewHolder extends RecyclerView.ViewHolder {


        public TextView name, date, address, order, price;

        public OtherViewHolder(View view) {


            super(view);
            name = (TextView) view.findViewById(R.id.namePrevOrder);
            date = (TextView) view.findViewById(R.id.datePrevOrder);
            address  =(TextView) view.findViewById(R.id.address);
            order = (TextView) view.findViewById(R.id.orderPrevOrder);
            price = (TextView)view.findViewById(R.id.pricePrevOrder);
        }
    }


    public PreviousFoodAdapter(List<PreviousOrderClass> prevOrderList) {
        this.prevOrderList = prevOrderList;
    }

    @Override
    public OtherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.previous_order_card, parent, false);

        return new OtherViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(OtherViewHolder holder, int position) {
        PreviousOrderClass poc = prevOrderList.get(position);
        holder.name.setText(poc.getName());
        holder.price.setText(poc.getPrice());
        holder.address.setText(poc.getAddress());
        holder.date.setText(poc.getDate());
        holder.order.setText(poc.getOrder());

    }


    @Override
    public int getItemCount() {
        return prevOrderList.size();
    }
}
