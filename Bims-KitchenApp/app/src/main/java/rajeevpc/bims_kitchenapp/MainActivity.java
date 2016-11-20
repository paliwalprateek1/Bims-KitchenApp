package rajeevpc.bims_kitchenapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Handler mHandler;
    private ProgressDialog mDialog;
    private Handler mHandler2 = new Handler();
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Toast.makeText(this, "248", Toast.LENGTH_SHORT).show();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ImageView myImageView= (ImageView)findViewById(R.id.splashscreen);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        myImageView.startAnimation(myFadeInAnimation);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if((StoreSharedPreferences.getUserEmail(MainActivity.this).length()==0)){
                    Intent mainIntent = new Intent(MainActivity.this, Login.class);
                    MainActivity.this.startActivity(mainIntent);
                    MainActivity.this.finish();
                }
                else{
                    Intent mainIntent = new Intent(MainActivity.this, MenuPage.class);
                    MainActivity.this.startActivity(mainIntent);
                    MainActivity.this.finish();


                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}