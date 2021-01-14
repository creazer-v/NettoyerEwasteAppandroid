package com.play.nettoyer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.play.nettoyer.ui.loading;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class user_profile extends Fragment {

    TextView gemail,usershow;
    EditText fname, address_p, phone_p, landmark_p, pincode_p;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
   // ContentResolver resolver = getActivity().getContentResolver();
    Context applicationContext = drawer.getContextOfApplication();
    Button btn;
    ProgressBar progressBar;
    ImageView imageView;
    private static final String TAG = "ProfileActivity";
    private final int IMAGE_SELECTION_REQUEST = 1;
    // private final int PICK_IMAGE_REQUEST = 22;
    static final int REQUEST_IMAGE_CAPTURE = 10001;
    static final int SELECT_IMAGE_REQUEST = 10002;
    String PROFILE_IMAGE_URL = null;
    //user
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    // Storage
    private StorageReference firebaseStorage;
    private Uri filePath;

    // database
    private DatabaseReference databaseReference;
    ImageView floatingActionButton;


    public user_profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_user_profile, container, false);
        gemail = (TextView) v.findViewById(R.id.my_email);
        fname = v.findViewById(R.id.fullname);
        address_p = v.findViewById(R.id.address);
        phone_p = v.findViewById(R.id.phoneno);
        landmark_p = v.findViewById(R.id.landmark);
        pincode_p = v.findViewById(R.id.pincode);
progressBar=v.findViewById(R.id.pgbar1);
        imageView = v.findViewById(R.id.profile_image);
usershow=v.findViewById(R.id.user_name1);
        floatingActionButton = v.findViewById(R.id.btnChoose);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        btn = v.findViewById(R.id.apply);

        userId = fAuth.getCurrentUser().getUid();
        btn = v.findViewById(R.id.apply);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .into(imageView);
            }
        }

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                final String name = fname.getText().toString();
                final String phoneno = phone_p.getText().toString();
                final String address = address_p.getText().toString();
                final String landmark = landmark_p.getText().toString();
                final String pincode = pincode_p.getText().toString();


                DocumentReference documentReference = fStore.collection("users").document(userId);
                userId = fAuth.getCurrentUser().getUid();
                Map<String, Object> user = new HashMap<>();
                user.put("userName", name);
                user.put("Phone", phoneno);
                user.put("Address", address);
                user.put("Landmark", landmark);
                user.put("Pincode", pincode);


                documentReference.set(user, SetOptions.merge());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Changes Applied", Toast.LENGTH_SHORT).show();

            }
        });
        // Do something in response to button click


        final DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(

                getActivity(), new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot
                                                documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        gemail.setText(documentSnapshot.getString("email"));
                        fname.setText(documentSnapshot.getString("userName"));
                        address_p.setText(documentSnapshot.getString("Address"));
                        phone_p.setText(documentSnapshot.getString("Phone"));
                        landmark_p.setText(documentSnapshot.getString("Landmark"));
                        pincode_p.setText(documentSnapshot.getString("Pincode"));
usershow.setText(documentSnapshot.getString("userName"));
                        final loading loading =new  loading(getActivity());
                    }
                });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhotoFromGallery();
            }
        });

        return v;
    }

    private void selectPhotoFromGallery() {
        Intent choosePicture = new Intent(Intent.ACTION_GET_CONTENT);
        choosePicture.setType("image/*");
        startActivityForResult(choosePicture, SELECT_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK
                && data != null && data.getExtras() != null) {
            bitmap = (Bitmap) data.getExtras().get("data");
            // Set the profile picture.
            imageView.setImageBitmap(bitmap);

        }
        if (requestCode == SELECT_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        // Set the profile picture.
        imageView.setImageBitmap(bitmap);
        // Upload the profile picture to Firebase.
        if (bitmap != null) {
            handleUpload(bitmap);
        } else {
            Toast.makeText(getActivity(), "Failed to upload image.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void handleUpload(Bitmap bitmap) {
        loading.startloading();
btn.setVisibility(View.GONE);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final StorageReference reference = FirebaseStorage.getInstance().getReference()
                    .child("profileImages")
                    .child(uid + ".jpeg");

            reference.putBytes(baos.toByteArray())
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            getDownloadUrl(reference);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: ", e.getCause());
                        }
                    });
        }



        private void getDownloadUrl(StorageReference reference) {
            reference.getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d(TAG, "onSuccess: " + uri);
                            setUserProfileUrl(uri);
                        }
                    });
        }

        private void setUserProfileUrl(Uri uri) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(uri)
                    .build();

            user.updateProfile(request)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Updated succesfully", Toast.LENGTH_SHORT).show();
                            btn.setVisibility(View.VISIBLE);
                            loading.dismissDialog();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Profile image failed...", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }




