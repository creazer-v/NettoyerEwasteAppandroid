//package com.play.nettoyer;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


   // TextView textuser, textemail;
    //FirebaseAuth fAuth;
    //FirebaseFirestore fStore;
           //{String userId;


    //@Override
    //protected void super.onCreate(Bundle savedInstanceState) {
        //textuser = findViewById(R.id.nav_username);
        //textemail = findViewById(R.id.nav_useremail);

        //fAuth = FirebaseAuth.getInstance();
       // fStore = FirebaseFirestore.getInstance();

     //   userId = fAuth.getCurrentUser().getUid();

       // DocumentReference documentReference = fStore.collection("users").document(userId);
      //  documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            //@/Override
            //public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
          //      textuser.setText(documentSnapshot.getString("userName"));
        //        textemail.setText(documentSnapshot.getString("email"));
      //      }
    //    });
  //  }
//}
