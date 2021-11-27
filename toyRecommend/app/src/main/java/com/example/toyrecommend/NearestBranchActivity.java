package com.example.toyrecommend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.toyrecommend.Prevalent.Prevalent;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NearestBranchActivity extends FragmentActivity implements OnMapReadyCallback {

    private TextView branchName, branchAddress1,branchAddress2,branchAddress3 ;
    private String address, city;
    GoogleMap map;
    FusedLocationProviderClient fusedLocationProviderClient;
    SupportMapFragment mf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_branch);

        mf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getCurrentLocation();

        mf.getMapAsync(this);

//        branchName = (TextView) findViewById(R.id.branch_name);
//        branchAddress1 = (TextView) findViewById(R.id.branch_address1);
//        branchAddress2 = (TextView) findViewById(R.id.branch_address2);
//        branchAddress3 = (TextView) findViewById(R.id.branch_address3);
//
        address = Prevalent.currentOnlineUser.getAddress();
        String[] c = address.split(",");
        city = c[c.length-1];
        city = city.replaceAll("\\s+","");
//        Log.d("address",address);
//        Log.d("address1",c[0]);
        Log.d("address2",city);
//        getCity(city);

    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NearestBranchActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    mf.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            MarkerOptions options = new MarkerOptions().position(latLng).title("Current Location");
                            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                            map.addMarker(options);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng Colombo = new LatLng(7.982316, 80.834206);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(Colombo, 8), 5000, null);

        if(city.equals("Colombo") || city.equals("colombo")){
//            ArrayList<Double> values = null;
//            try {
//                values = getLatLng("Colombo", this);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            LatLng marker = new LatLng(6.933488, 79.845436);
            map.addMarker(new MarkerOptions().position(marker).title("Fort"));

            LatLng marker1 = new LatLng(6.937774, 79.861575);
            map.addMarker(new MarkerOptions().position(marker1).title("Aluthkade"));
        }
        else if(city.equals("Gampaha") || city.equals("gampaha")){
            LatLng marker = new LatLng(7.085144, 79.994188);
            map.addMarker(new MarkerOptions().position(marker).title("Medagama"));

            LatLng marker1 = new LatLng(7.123144, 79.979799);
            map.addMarker(new MarkerOptions().position(marker1).title("Udugampola"));

        }
        if(city.equals("Kaluthara") || city.equals("kaluthara")){
            LatLng marker = new LatLng(6.580495, 80.000880);
            map.addMarker(new MarkerOptions().position(marker).title("Bombuwala"));

            LatLng marker1 = new LatLng(6.513038, 80.102928);
            map.addMarker(new MarkerOptions().position(marker1).title("Mathugama"));

        }
        if(city.equals("Galle") || city.equals("galle")) {
            LatLng marker = new LatLng(6.059205, 80.267928);
            map.addMarker(new MarkerOptions().position(marker).title("Akmeemana"));

            LatLng marker1 = new LatLng(6.024871, 80.331100);
            map.addMarker(new MarkerOptions().position(marker1).title("Kahanda"));
        }
    }

    public ArrayList<Double> getLatLng(final String locations, final Context context) throws IOException {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        ArrayList<Double> result = new ArrayList<>();

        List LatLngList = geocoder.getFromLocationName(locations, 1);
        if (LatLngList != null && LatLngList.size() > 0) {
            Address address = (Address) LatLngList.get(0);
            result.add(address.getLatitude());
            result.add(address.getLongitude());
        }
        return result;
    }

//
//    private void getCity(String city) {
//
//        if(city.equals("Colombo") || city.equals("colombo")){
//            branchName.setText("Toy center");
//            branchAddress1.setText("No.3A\n5th Lane\nColombo");
//        }
//        else if(city.equals("Gampaha") || city.equals("gampaha")){
//            branchName.setText("Toy center");
//            branchAddress1.setText("No.231B\nSirikurusa Road\nGampaha");
//
//        }
//        if(city.equals("Kaluthara") || city.equals("kaluthara")){
//            branchName.setText("Toy center");
//            branchAddress1.setText("No.60/6\nOld Road\nKaluthara");
//
//        }
//        if(city.equals("Galle") || city.equals("galle")){
//            branchName.setText("Toy center");
//            branchAddress1.setText("No.4A\nMain Street\nGalle");
//
//        }
//    }
}