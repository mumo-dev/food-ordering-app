package com.android.mumo.swahilicuisine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.mumo.swahilicuisine.fragments.BlogFragment;
import com.android.mumo.swahilicuisine.fragments.DeliveryLocationFragment;
import com.android.mumo.swahilicuisine.fragments.MenuFragment;
import com.android.mumo.swahilicuisine.fragments.OrderFragment;
import com.android.mumo.swahilicuisine.fragments.RecipeBooksFragment;
import com.android.mumo.swahilicuisine.fragments.RestaurantsFragment;
import com.android.mumo.swahilicuisine.interfaces.OnLocationSetListener;
import com.android.mumo.swahilicuisine.interfaces.OnRestaurantClickListener;
import com.android.mumo.swahilicuisine.model.Restaurant;
import com.android.mumo.swahilicuisine.model.User;
import com.android.mumo.swahilicuisine.utils.PreferenceUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnLocationSetListener, OnRestaurantClickListener {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView= (NavigationView) findViewById(R.id.nav_view);
        Menu menuNav = navigationView.getMenu();
        MenuItem accountMenuItem = menuNav.findItem(R.id.nav_account);
        User user = PreferenceUtils.getUserDetails(this);
        if(user != null){
            accountMenuItem.setEnabled(false);
        }
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        Intent intent = getIntent();
        if(intent!= null){
            String fromOrders = intent.getStringExtra(ConfirmOrderActivity.EXTRA_FROM_CONFIRM_ORDER);
            if("yes".equals(fromOrders)){
                OrderFragment orderFragment = new OrderFragment();
                fragmentTransaction.replace(R.id.fragment_container, orderFragment);
                fragmentTransaction.commit();
            }else {
                DeliveryLocationFragment deliveryLocationFragmentFragement = new DeliveryLocationFragment();
                fragmentTransaction.add(R.id.fragment_container, deliveryLocationFragmentFragement);
                fragmentTransaction.commit();
            }
        }else {


            DeliveryLocationFragment deliveryLocationFragmentFragement = new DeliveryLocationFragment();
            fragmentTransaction.add(R.id.fragment_container, deliveryLocationFragmentFragement);
            fragmentTransaction.commit();
        }


    }

    @Override
    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();

        if (fragments > 0) {
//            Fragment fragment = getSupportFragmentManager().
            int index = fragments - 1; //get the current index of the fragment on backstack
            FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(index);
            String tag = backEntry.getName();
//            Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
            if (tag.equals("menu")) {
                MenuFragment fragment=(MenuFragment)getSupportFragmentManager().findFragmentByTag("menu");
//                    dialoger();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
// Add the buttons
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        getSupportFragmentManager().popBackStack();

                        return;
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();

                        return;
                    }
                });
                builder.setMessage("Do u want to leave?\n All items in your cart will be lost");
                AlertDialog dialog = builder.create();
                dialog.show();

                return;
            } else {
//            Toast.makeText(this, "Fragment poped off "+ tag, Toast.LENGTH_LONG).show();
                getSupportFragmentManager().popBackStack();
                return;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public void dialoger(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
// Add the buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                dialog.dismiss();
                return;
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
//                getSupportFragmentManager().popBackStack();
                return;
            }
        });
        builder.setMessage("Do u wwnat to leave?");
        AlertDialog dialog = builder.create();
        dialog.show();
//                return;
    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            PreferenceUtils.deleteUser(this);
            //enable navmenu item
            Menu menuNav = navigationView.getMenu();
            MenuItem accountMenuItem = menuNav.findItem(R.id.nav_account);
            accountMenuItem.setEnabled(true);
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_location) {
            DeliveryLocationFragment fragment = new DeliveryLocationFragment();
            replaceFragment(fragment, "location");
        } else if (id == R.id.nav_restuarants) {
            RestaurantsFragment fragment = new RestaurantsFragment();
            replaceFragment(fragment, "restaurant");
        } else if (id == R.id.nav_order) {
            OrderFragment fragment = new OrderFragment();
            replaceFragment(fragment, "orders");

        } else if (id == R.id.nav_blog) {
            BlogFragment fragment = new BlogFragment();
            replaceFragment(fragment, "blog");

        } else if (id == R.id.nav_book) {
            RecipeBooksFragment fragment = new RecipeBooksFragment();
            replaceFragment(fragment, "books");
        } else if(id == R.id.nav_account){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onLocationSet() {
        //open resturant fragment;;
//        Toast.makeText(this, "Location set", Toast.LENGTH_LONG).show();
        RestaurantsFragment fragment = RestaurantsFragment.newInstance();
        replaceFragment(fragment, "restaurant");

    }

    @Override
    public void onRestaurantClick(int resId, String name, String url, String time, Double deliveryFee) {
        //open Menu activity
        /*Intent menuIntent = MenuActivity.newIntent(this, resId, name, url, time, deliveryFee);
        startActivity(menuIntent);*/
        MenuFragment fragment = MenuFragment.newInstance(resId, name, url, time, deliveryFee);
        replaceFragment(fragment, "menu");
    }

    private void replaceFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(tag);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }


}
