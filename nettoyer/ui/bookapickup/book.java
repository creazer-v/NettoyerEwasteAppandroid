package com.play.nettoyer.ui.bookapickup;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.play.nettoyer.R;
import com.play.nettoyer.ui.loading;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class book extends Fragment {
    private static final String TAG = "bookActivity";
    static final int SELECT_IMAGE_REQUEST = 10002;
    static final int REQUEST_IMAGE_CAPTURE = 10001;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    TextView textview,textViewview;
    Spinner spinner;
    ImageView imageView1, imageView2, imageView3;
  EditText prdname,prddes,editText;
    Firebase reference1, reference2;
    private DatabaseReference myDatabase;
    private static final int SELECT_FILE1 = 1;
    private static final int SELECT_FILE2 = 2;
    String selectedPath1 = "NONE";
    String selectedPath2 = "NONE";
    private final int CODE_IMG = 1;
    Button btn;
    private final int CODE_MULTIPLE_IMG_GALLERY = 2;

    private BookViewModel mViewModel;

    public static book newInstance() {
        return new book();
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.book_fragment, container, false);
final Date currentTime = Calendar.getInstance().getTime();
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        imageView1 = v.findViewById(R.id.image1);
        imageView2 = v.findViewById(R.id.image2);
        imageView3 = v.findViewById(R.id.image3);
        prdname=v.findViewById(R.id.product_name);
        prddes=v.findViewById(R.id.product_description);
        editText=v.findViewById(R.id.viewmsg);
        textview=v.findViewById(R.id.textw);
        textViewview=v.findViewById(R.id.textViewview);
        final loading loading =new  loading(getActivity());
      //  StorageReference storageRef = storage.getReference();
        final EditText phone_no = v.findViewById(R.id.editTextPhone);
        final EditText address = v.findViewById(R.id.editTextPhone2);
spinner=v.findViewById(R.id.list_of_products);
btn=v.findViewById(R.id.request_pickup);



//String typevalue=spinner.getSelectedItem().toString();




        myDatabase= FirebaseDatabase.getInstance().getReference("Information");

myDatabase.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        String[] Messages = Objects.requireNonNull(dataSnapshot.getValue()).toString().split(",");

        textview.setText(""); //cleaning text null

        for (int i = 0; i < Messages.length; i++) {
            String[] finalmsg = Messages[i].split("=");
            textview.append(finalmsg[1] + "\n");
        }
    }
    //   textView.setText(dataSnapshot.getValue().toString());



    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
            textview.setText("CANCELLED");
    }
});







//Array Adapter
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getActivity(),
                R.array.choose_product,android.R.layout.simple_spinner_item);
        // specifying
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // applying to adapter
      spinner.setAdapter(adapter);

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectmultiplephoto();
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectmultiplephoto();
            }

        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectmultiplephoto();
                /**  Intent intent1 = new Intent();
                 intent1.setType("image/*");
                 intent1.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                 intent1.setAction(Intent.ACTION_GET_CONTENT);
                 startActivityForResult(Intent.createChooser(intent1,"Select 3 Images"),
                 CODE_MULTIPLE_IMG_GALLERY); **/
            }

        });

btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {


        final String prdN=prdname.getText().toString();
        final String prdD=prddes.getText().toString();
        final String phonenumbr=phone_no.getText().toString();
        final String addresscon=address.getText().toString();


       editText.setText("Product Name : \t" + prdN + "\n Product Description : \t" +prdD+ "\n Phone Number : \t" +phonenumbr+
                "\n Address : \t" +addresscon +"\n Time  : \t" +currentTime);


      //  editText.setText("");
//Dialog box alert



        String listslected=(String)spinner.getSelectedItem();
        String code = prdname.getText().toString().trim();
        String code1 = prddes.getText().toString().trim();
        if (code.isEmpty()) {
            prdname.setError("Product Name Is Empty");
            prdname.requestFocus();
            return;
        }else if (code1.isEmpty()) {
            prddes.setError("Product Condition");
            prddes.requestFocus();
            return;

        }

        final String prd_name = prdname.getText().toString();
        final String prd_desc = prddes.getText().toString();


        DocumentReference documentReference = fStore.collection("users").document(userId);
        userId = fAuth.getCurrentUser().getUid();
        Map<String, Object> user = new HashMap<>();
        user.put("Product_Name", prd_name);
        user.put("Product_Description", prd_desc);
        user.put("Category", listslected);


        documentReference.set(user, SetOptions.merge());


        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm");
        builder.setMessage("Are You Sure ?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


//editText.getText().toString();

                String details=   editText.getText().toString();



               Toast.makeText(getActivity(), "Thank You", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(),com.play.nettoyer.thank_you.class);
               myDatabase.child(Long.toString(System.currentTimeMillis())).setValue(editText.getText().toString());
                startActivity(intent);
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert=builder.create();
        alert.show();


    }

});

        userId = fAuth.getCurrentUser().getUid();
        final DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(
                getActivity(), new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@com.google.firebase.database.annotations.Nullable DocumentSnapshot
                                                documentSnapshot, @com.google.firebase.database.annotations.Nullable FirebaseFirestoreException e) {


                        address.setText(documentSnapshot.getString("Address"));
                        phone_no.setText(documentSnapshot.getString("Phone"));


                    }
                });

        return v;
    }







    private void handleImageUpload() {
        loading.startloading();
        imageView1.setDrawingCacheEnabled(true);
        imageView1.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable)  imageView1.getDrawable()).getBitmap();
        Bitmap bitmap1 =((BitmapDrawable)  imageView2.getDrawable()).getBitmap();
        Bitmap bitmap2 =((BitmapDrawable)  imageView3.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        bitmap1.compress(Bitmap.CompressFormat.JPEG, 10, baos1);
        bitmap2.compress(Bitmap.CompressFormat.JPEG, 10, baos2);
        byte[] data = baos.toByteArray();
        byte[] data1 = baos1.toByteArray();
        byte[] data2 = baos2.toByteArray();
        int date= Calendar.getInstance().getTime().getDate();
        int month= Calendar.getInstance().getTime().getMonth();
        int year= Calendar.getInstance().getTime().getYear();

        Date time= Calendar.getInstance().getTime();
        String listslected=(String)spinner.getSelectedItem();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final StorageReference reference = FirebaseStorage.getInstance().getReference()
                .child("Requested Pickups")
                .child("Date  /"+ year)
                .child("Time  / "+month )
                .child("Time  /"+date)
                .child(uid)
                .child("user"+ date)

                .child(listslected+ 1+ ".jpeg");


        final StorageReference reference1 = FirebaseStorage.getInstance().getReference()
                .child("Requested Pickups")
                .child("Date  /"+ year)
                .child("Time  / "+month )
                .child("Time  /"+date)
                .child(uid)
                .child("user"+ date)

                .child(listslected+ 2+ ".jpeg");

        final StorageReference reference2 = FirebaseStorage.getInstance().getReference()
                .child("Requested Pickups")
                .child("Date  /"+ year)
                .child("Time  / "+month )
                .child("Time  /"+date)
                .child(uid)
                .child("user"+ date)

                .child(listslected+ 3+ ".jpeg");


        UploadTask uploadTask = reference.putBytes(data);
        UploadTask uploadTask1 = reference1.putBytes(data1);
        uploadTask1.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
        UploadTask uploadTask2 = reference2.putBytes(data2);
        uploadTask2.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                loading.dismissDialog();
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });


    }

    private void selectPhotoFromGallery() {
        startActivityForResult(Intent.createChooser(new Intent().
                        setAction(Intent.ACTION_GET_CONTENT).setType("image/*"),
                "Select Images"), CODE_IMG);

    }

    private void selectmultiplephoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Images"),
                CODE_MULTIPLE_IMG_GALLERY);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_MULTIPLE_IMG_GALLERY && resultCode == RESULT_OK
                && null != data) {

           // Uri imageUri = data.getData();
            //if (imageUri != null) {
              //  imageView1.setImageURI(imageUri); }

          // else if(requestCode == CODE_MULTIPLE_IMG_GALLERY && requestCode == RESULT_OK && null !=data) {
              ClipData clipData = data.getClipData();

                if (clipData != null) {
                    imageView2.setImageURI(clipData.getItemAt(0).getUri());
                    imageView3.setImageURI(clipData.getItemAt(1).getUri());
                    imageView1.setImageURI(clipData.getItemAt(2).getUri());

                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        Uri uri = item.getUri();
                        Log.e("Multiple Images", uri.toString());




                    }

handleImageUpload();
                    }
                }
            }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(BookViewModel.class);
        // TODO: Use the ViewModel
    }

}
