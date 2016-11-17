package rajeevpc.bims_kitchenapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.lang.UCharacter;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class MenuPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    ImageView imageView;

    NavigationView navigationView;


    Menu menu;
    MenuItem nav_location;
    // Handle navigation view item clicks here.

    public List<Food> orderedList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StoreSharedPreferences storeSharedPreferences = new StoreSharedPreferences();
                List a = storeSharedPreferences.loadFoodQuantity(getApplicationContext());
                if(a==null){
                    Toast.makeText(MenuPage.this, "Select atleast on item", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(MenuPage.this, ProceedOrder.class);
                    startActivity(intent);
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
//        imageView = (ImageView)findViewById(R.id.imageView);



        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        menu = navigationView.getMenu();
        nav_location =   menu.findItem(R.id.nav_location);
        nav_location.setTitle(StoreSharedPreferences.getUserCustomLocation(MenuPage.this));

    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Veg(), "VEG");
        adapter.addFragment(new NonVeg(), "NON-VEG");
        adapter.addFragment(new V(), "Beverages");
        viewPager.setAdapter(adapter);
    }

    public void profMenu(MenuItem item) {

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {



        int id = item.getItemId();

        if (id == R.id.nav_prof) {
            Intent intent = new Intent(MenuPage.this, UserProfile.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_track) {

        } else if (id == R.id.nav_refer) {

        } else if (id == R.id.nav_prevOrder) {
            Intent intent = new Intent(MenuPage.this, PreviousOrders.class);
            startActivity(intent);

        } else if (id == R.id.nav_feedback) {

        } else if (id == R.id.nav_about) {

        } else if( id == R.id.nav_location){
            android.app.AlertDialog.Builder builderSingle = new android.app.AlertDialog.Builder(MenuPage.this);
            builderSingle.setTitle("Select Your Location");
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    MenuPage.this, android.R.layout.select_dialog_item);
            arrayAdapter.add("Gandhinagar");
            arrayAdapter.add("Vadodara");

            final TextView tv= (TextView) findViewById(R.id.nav_location);


            builderSingle.setAdapter(

                    arrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String strName = arrayAdapter.getItem(which);
                            android.app.AlertDialog.Builder builderInner = new android.app.AlertDialog.Builder(
                                    MenuPage.this);
                            if (strName == "Gandhinagar") {
                                nav_location.setTitle("Gandhinagar");
                                StoreSharedPreferences.setUserCustomLocation(MenuPage.this, "Gandhinagar");

                            } else if (strName == "Vadodara") {
                                StoreSharedPreferences.setUserCustomLocation(MenuPage.this, "Vadodara");
                                nav_location.setTitle("Vadodara");
                            }
                        }
                    });
            builderSingle.create().show();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

