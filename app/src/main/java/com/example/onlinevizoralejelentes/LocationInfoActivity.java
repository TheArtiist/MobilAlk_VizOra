package com.example.onlinevizoralejelentes;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class LocationInfoActivity extends AppCompatActivity {
    private ImageView spinningImage;
    private Animation rotateAnimation;
    private Notifications mNotifications;

    private EditText editTextPostalCode, editTextCity, editTextStreet, editTextHouseNumber, editTextMeterValue;
    private Button buttonSubmit;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FusedLocationProviderClient fusedLocationClient; //Helymeghatározás

    private static final int REQUEST_IMAGE_CAPTURE = 1;//Kamera
    private static final int PICK_IMAGE_REQUEST = 2;//Kamera

    private Bitmap capturedImageBitmap; //Kamera
    private Uri imageUri; //Kamera kép
    private ActivityResultLauncher<Intent> captureImageLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_location_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            getLocation();
        }

        spinningImage = findViewById(R.id.spinningImage);

        // Load animation
        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.spinin_anim);
        spinningImage.startAnimation(rotateAnimation);

        mNotifications = new Notifications(this);


        db = FirebaseFirestore.getInstance();

        editTextPostalCode = findViewById(R.id.editTextPostalCode);
        editTextCity = findViewById(R.id.editTextCity);
        editTextStreet = findViewById(R.id.editTextStreet);
        editTextHouseNumber = findViewById(R.id.editTextHouseNumber);
        editTextMeterValue = findViewById(R.id.editTextMeterValue);
        buttonSubmit = findViewById(R.id.buttonUpload);

        buttonSubmit.setOnClickListener(view -> saveDataToFirebase());

        captureImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        if (extras != null) {
                            capturedImageBitmap = (Bitmap) extras.get("data");
                            ImageView imagePreview = findViewById(R.id.imagePreview);
                            imagePreview.setImageBitmap(capturedImageBitmap);

                            //Kép URI mentése, hogy később feltölthető legyen
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            capturedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            String path = MediaStore.Images.Media.insertImage(getContentResolver(), capturedImageBitmap, "MeterImage", null);
                            imageUri = Uri.parse(path);
                        }
                    }
                }
        );


    }

    @Override
    protected void onResume() {
        super.onResume();
        spinningImage.startAnimation(rotateAnimation);  // Starts spinning when user is active
    }

    @Override
    protected void onPause() {
        super.onPause();
        spinningImage.clearAnimation();  // Stops spinning when user leaves the page
    }

    public void uploadMeter(View view){

    }

    private void saveDataToFirebase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail;
        if (user != null) {
            userEmail = user.getEmail();  // Get the logged-in user's email
        } else {
            userEmail = "";
        }
        String postalCodeStr = editTextPostalCode.getText().toString().trim();
        String city = editTextCity.getText().toString().trim();
        String street = editTextStreet.getText().toString().trim();
        String houseNumberStr = editTextHouseNumber.getText().toString().trim();
        String meterValueStr = editTextMeterValue.getText().toString().trim();

        if (postalCodeStr.isEmpty() || city.isEmpty() || street.isEmpty() || houseNumberStr.isEmpty() || meterValueStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        int postalCode = Integer.parseInt(postalCodeStr);
        int houseNumber = Integer.parseInt(houseNumberStr);
        int meterValue = Integer.parseInt(meterValueStr);

        if (imageUri != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference("meter_images/" + System.currentTimeMillis() + ".jpg");
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString(); // Kép URL mentése

                        //Adatok mentése Firestore-ba, már a kép URL-lel együtt
                        Invoices invoice = new Invoices(userEmail, postalCode, city, street, houseNumber, meterValue, imageUrl);
                        db.collection("meter_readings")
                                .add(invoice)
                                .addOnSuccessListener(documentReference -> Toast.makeText(this, "Data uploaded successfully!", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(this, "Failed to upload data!", Toast.LENGTH_SHORT).show());
                    }))
                    .addOnFailureListener(e -> Toast.makeText(this, "Image upload failed!", Toast.LENGTH_SHORT).show());
        } else {
            //Ha nincs kép, mentjük az adatokat kép nélkül
            Invoices invoice = new Invoices(userEmail, postalCode, city, street, houseNumber, meterValue, null);
            db.collection("meter_readings")
                    .add(invoice)
                    .addOnSuccessListener(documentReference -> Toast.makeText(this, "Data uploaded successfully!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to upload data!", Toast.LENGTH_SHORT).show());
        }
    }
    public void cancelButton(View view){
        Intent intent = new Intent(this, MainPage.class);
        startActivity(intent);
        mNotifications.send("Vízóra feltöltés megszakítva");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        getAddressFromLocation(location.getLatitude(), location.getLongitude());
                    }
                });
    }

    private void getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                String city = addresses.get(0).getLocality();
                String street = addresses.get(0).getThoroughfare();

                EditText cityField = findViewById(R.id.editTextCity);
                EditText streetField = findViewById(R.id.editTextStreet);

                cityField.setText(city);
                streetField.setText(street);
            }
        } catch (IOException e) {
            Log.e("Location", "Hiba a cím lekérésekor!", e);
        }
    }

    //////// Lent a kamerához kapcsolodó eszlözök

    public void captureImage(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            captureImageLauncher.launch(takePictureIntent); // 🔹 Az új API szerint indítjuk
        } else {
            Log.e("Camera", "Nincs megfelelő kamera alkalmazás telepítve!");
        }
    }


    //Ha nem kamerával, hanem gallériából akarnám megoldani a képet
    /*public void selectImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            capturedImageBitmap = (Bitmap) data.getExtras().get("data");
            ImageView imagePreview = findViewById(R.id.imagePreview);
            imagePreview.setImageBitmap(capturedImageBitmap);

            //Kép URI létrehozása
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            capturedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), capturedImageBitmap, "MeterImage", null);
            imageUri = Uri.parse(path);
        }
    }



}