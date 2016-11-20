package rajeevpc.bims_kitchenapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;

public class NumberAndLocation extends AppCompatActivity {
    EditText  phno;
    TextView et;
    Firebase ref;
    UserToBeRegistered userToBeRegistered = new UserToBeRegistered();

    private Handler mHandler;
    private ProgressDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_and_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 1) {
                    mDialog.cancel();
                }

                return false;
            }
        });
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Signing In....");
        mDialog.show();
        mHandler.sendEmptyMessageDelayed(1, 3000);
        Firebase.setAndroidContext(this);

        ref = new Firebase(Server.URL);

        et = (TextView) findViewById(R.id.location);
        phno = (EditText) findViewById(R.id.phno);

    }

    public void selectLocation(View view) {

        android.app.AlertDialog.Builder builderSingle = new android.app.AlertDialog.Builder(NumberAndLocation.this);
        builderSingle.setTitle("Select Your Location");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                NumberAndLocation.this, android.R.layout.select_dialog_item);
        arrayAdapter.add("Gandhinagar");
        arrayAdapter.add("Vadodara");

        builderSingle.setAdapter(

                arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        android.app.AlertDialog.Builder builderInner = new android.app.AlertDialog.Builder(
                                NumberAndLocation.this);
                        if (strName == "Gandhinagar") {
                            et.setText(strName);
                            StoreSharedPreferences.setUserCustomLocation(NumberAndLocation.this, "Gandhinagar");



                        } else if (strName == "Vadodara") {
                            et.setText(strName);
                            StoreSharedPreferences.setUserCustomLocation(NumberAndLocation.this, "Vadodara");

                        }
                    }
                });
        builderSingle.create().show();

    }

    public void done(View view) {
        StoreSharedPreferences.setUserNumber(NumberAndLocation.this, phno.getText().toString());
        userToBeRegistered.setNumber(StoreSharedPreferences.getUserNumber(NumberAndLocation.this));
        userToBeRegistered.setEmail(StoreSharedPreferences.getUserEmail(NumberAndLocation.this));
        userToBeRegistered.setName(StoreSharedPreferences.getUserName(NumberAndLocation.this));
        userToBeRegistered.setLocation(StoreSharedPreferences.getUserCustomLocation(NumberAndLocation.this));
        Firebase newRef = ref.child("Users").push();
        Firebase newerRef = ref.child("UsersMail").push();
        newerRef.setValue(StoreSharedPreferences.getUserEmail(NumberAndLocation.this));
        newRef.setValue(userToBeRegistered);
        Intent intent = new Intent(this, MenuPage.class);
        startActivity(intent);
        finish();
    }
}
class UserToBeRegistered{
    private String email;
    private String number;
    private String name;
    private String location;

    public String getLocation(){ return location;}

    public void setLocation(String location){this.location = location;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}

