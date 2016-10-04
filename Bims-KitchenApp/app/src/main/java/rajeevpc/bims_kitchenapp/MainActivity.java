package rajeevpc.bims_kitchenapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "id you'll run this app nothing interedting is gonna happen", Toast.LENGTH_SHORT).show();

    }
}
