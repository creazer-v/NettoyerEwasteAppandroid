package com.play.nettoyer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link find_centre#newInstance} factory method to
 * create an instance of this fragment.
 */

public class find_centre extends Fragment implements OnMapReadyCallback {
    GoogleMap map;
   // LatLng position;




  /**  public  double lat;
    public  double lng;
public void LatLng(double lat, double lng){
    this.lat=lat;
    this.lng=lng;
} **/



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public find_centre() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment find_centre.
     */
    // TODO: Rename and change types and number of parameters
    public static find_centre newInstance(String param1, String param2) {
        find_centre fragment = new find_centre();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find_centre, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);



        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
       com.google.android.gms.maps.model.LatLng pp = new com.google.android.gms.maps.model.LatLng(12.843378, 77.675949);
        MarkerOptions options = new MarkerOptions();
        options.position(pp).title("Recycle Centre (IZEE COLLEGE)");
        map.addMarker(options);
        float zoom = 18;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pp, zoom));

    }


}