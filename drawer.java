package com.play.nettoyer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import de.hdodenhof.circleimageview.CircleImageView;

public class drawer extends AppCompatActivity  {
    TextView name, email,logout;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    EditText editText;
    CircleImageView imageView;
    ProgressBar progressBar;
    Fragment fragment;
    private static final String TAG = "drawer";

    private AppBarConfiguration mAppBarConfiguration;
    private Object AuthUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        progressBar = findViewById(R.id.pgbar);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();

logout=findViewById(R.id.logout_btn);
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_drawer);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        final View hView = navigationView.getHeaderView(0);
        name = hView.findViewById(R.id.nav_username);
        email = hView.findViewById(R.id.nav_useremail);
        imageView = hView.findViewById(R.id.nav_image);




        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if (user != null) {
                    if (user.getPhotoUrl() != null) {
                        Glide.with(drawer.this)
                                .load(user.getPhotoUrl())
                                .into(imageView);
                    }
                }
            }
        });


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_bookapickup, R.id.nav_myprofile, R.id.nav_Info)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        //  NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
       // navigationView.setNavigationItemSelectedListener(this);
//fetches data from firestore to display in navigation header
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@com.google.firebase.database.annotations.Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                name.setText(documentSnapshot.getString("userName"));
                email.setText(documentSnapshot.getString("email"));


            }
        });
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startLoginActivity();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_settings:
                startActivity(new Intent(this,image.class));
                return true;
            default:

        return super.onOptionsItemSelected(item);
    }
    }

    private void logout_of_application() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to LogOut?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logOut();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void startLoginActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

  /**   @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
     @SuppressWarnings("StatementWithEmptyBody")
     @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.executePendingTransactions();
    int id = item.getItemId();
      //   Toolbar toolbar = findViewById(R.id.toolbar);


    if (id == R.id.nav_logout) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("Do you want to LogOut?")
    .setCancelable(false)
    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    public void onClick(DialogInterface dialog, int id) {
    logOut();
    }
    })
    .setNegativeButton("No", new DialogInterface.OnClickListener() {
    public void onClick(DialogInterface dialog, int id) {
    dialog.cancel();
    }
    });
    AlertDialog alert = builder.create();
    alert.show();
    }else if (id == R.id.nav_myprofile) {
    fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, new user_profile()).commit();

    Objects.requireNonNull(getSupportActionBar()).setTitle("My Profile");

    }else if (id == R.id.nav_bookapickup) {
    fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, new book()).commit();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Book A Pickup");
        finishAndRemoveTask();
    }else if (id == R.id.nav_Info) {
    fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, new about()).commit();
        Objects.requireNonNull(getSupportActionBar()).setTitle("About");

    }else if (id == R.id.nav_centre) {
    fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, new find_centre()).commit();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Find Centre");

    }





    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
    } **/

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        drawer.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }


    private void logOut() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(drawer.this, MainActivity.class);
         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
       startLoginActivity();
    }

    public void logoutbtn(View view) {
        logout_of_application();
    }
}









