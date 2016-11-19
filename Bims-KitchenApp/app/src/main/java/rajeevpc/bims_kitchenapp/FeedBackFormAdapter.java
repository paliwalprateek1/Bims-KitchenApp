package rajeevpc.bims_kitchenapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by prateek on 19/11/16.
 */
public class FeedBackFormAdapter extends RecyclerView.Adapter<FeedBackFormAdapter.OtherViewHolder> {

    private List<FeedBackData> feedBackData;


    public class OtherViewHolder extends RecyclerView.ViewHolder {


        public TextView name, rating, feedback;

        public OtherViewHolder(View view) {


            super(view);
            name = (TextView) view.findViewById(R.id.feedback_name);
            rating = (TextView) view.findViewById(R.id.feedback_rating);
            feedback  =(TextView) view.findViewById(R.id.feedback_feedback);
        }
    }


    public FeedBackFormAdapter(List<FeedBackData> feedBackData) {
        this.feedBackData = feedBackData;
    }

    @Override
    public OtherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feedback_card, parent, false);

        return new OtherViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(OtherViewHolder holder, int position) {
        FeedBackData fbd = feedBackData.get(position);
        holder.name.setText(fbd.getName());
        holder.rating.setText(fbd.getRating() + "/5");
        holder.feedback.setText(fbd.getFeedback());
    }


    @Override
    public int getItemCount() {
        return feedBackData.size();
    }
}


class FeedBackData{
    String name;
    String rating;
    String feedback;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }


}
