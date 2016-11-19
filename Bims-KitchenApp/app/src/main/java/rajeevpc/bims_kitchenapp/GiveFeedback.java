package rajeevpc.bims_kitchenapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

public class GiveFeedback extends AppCompatActivity {

    private RatingBar ratingBar;
    private TextView txtRatingValue;
    private Button btnSubmit;
    Firebase ref;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_feedback);
        et = (EditText)findViewById(R.id.write_feedback);

        et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        addListenerOnRatingBar();
        addListenerOnButton();
    }

    public void addListenerOnRatingBar() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

            }
        });
    }

    public void addListenerOnButton() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        btnSubmit = (Button) findViewById(R.id.submit_feedback);

        //if click on me, then display the current rating value.
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendData(String.valueOf(ratingBar.getRating()));

            }

        });

    }
    public void sendData(String rating){
        FeedBackData fbd = new FeedBackData();
        fbd.setName(StoreSharedPreferences.getUserName(GiveFeedback.this));
        fbd.setRating(rating);
        fbd.setFeedback(et.getText().toString());

        Firebase.setAndroidContext(getApplicationContext());
        ref = new Firebase(Server.URL);
        Firebase newRef = ref.child("Feedback").push();
        newRef.setValue(fbd);
        startActivity(new Intent(this, FeedBackForm.class));
        finish();

    }
}
