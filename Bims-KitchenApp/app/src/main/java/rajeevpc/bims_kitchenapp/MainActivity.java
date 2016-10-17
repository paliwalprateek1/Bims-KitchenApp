package rajeevpc.bims_kitchenapp;

import android.content.Intent;
import android.os.Handler;
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

    private final int SPLASH_DISPLAY_LENGTH = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "56", Toast.LENGTH_SHORT).show();


        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ImageView myImageView= (ImageView)findViewById(R.id.splashscreen);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        myImageView.startAnimation(myFadeInAnimation);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(MainActivity.this, M.class);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

//        Button btn =(Button) findViewById(R.id.button1);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Intent i =new Intent(MainActivity.this,WelcomeActivity.class);
//                //startActivity(i);
//            }
//        });

    }
}
