package com.mhj.longlivemypet;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mhj.longlivemypet.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static androidx.core.content.ContextCompat.getSystemService;


public class MapFragment extends Fragment implements OnMapReadyCallback{

    MapView mapView;

    GoogleMap map;

    BottomNavigationView bottomNavigationView;
    MarkerOptions myLocationMarker, hLocationMarker, pLocationMarker;

    LocationManager manager;

    private static final String TAG = "MAP";
    private RequestQueue queue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        /*Fragment내에서는 mapView로 지도를 실행*/
        mapView = (MapView)rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this); // 비동기적 방식으로 구글 맵 실행

        bottomNavigationView = (BottomNavigationView)rootView.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.tab1:

                        startLocationService();
                        return true;
                    case R.id.tab2:
                        map.clear();
                        hospitalLocationService();
                        return true;
                    case R.id.tab3:
                        map.clear();
                        pharmacyLocationService();
                        return true;


                }
                return false;
            }
        });
        return rootView;


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onMapReady(GoogleMap googleMap){

        Log.e("MapActivity", "지도 준비됨");
        map = googleMap;

        LatLng SEOUL = new LatLng(37.655728, 127.062539);

        map.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        map.animateCamera(CameraUpdateFactory.zoomTo(10));

    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        manager = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
    }

    public void startLocationService(){
        //LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        try {
            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null){
                //기준점: 날짜변경선
                double latitude = location.getLatitude(); //위도
                double longitude = location.getLongitude();// 경도
                String message = "최근 위치 -> Lat:" + latitude + "\nLon:" + longitude;
                Log.e("MapActivity" , message);
                showCurrentLocation(latitude, longitude);
            }



            Toast.makeText(getContext(), "GPS 좌표 용청함.", Toast.LENGTH_SHORT).show();

        }catch (SecurityException e){
            e.printStackTrace();
        }
    }


    private void  showCurrentLocation(Double lat, Double lon){
        LatLng curPos = new LatLng(lat, lon);
        this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPos, 15));
        showMyLocationMarker(curPos);

    }

    private void showMyLocationMarker(LatLng curPoint){

        if(myLocationMarker == null) {
            myLocationMarker = new MarkerOptions();
            myLocationMarker.position(curPoint);
            myLocationMarker.title("내 위치\n");
            myLocationMarker.snippet("GPS로 확인한 위치");
            myLocationMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.iconfinder_ic_my_location_24px_352557));
            map.addMarker(myLocationMarker);
        }else{
            myLocationMarker.position(curPoint);
            myLocationMarker.title("내 위치\n");
            myLocationMarker.snippet("GPS로 확인한 위치");
            myLocationMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.iconfinder_ic_my_location_24px_352557));
            map.addMarker(myLocationMarker);
        }
    }

    public void hospitalLocationService(){

        queue = Volley.newRequestQueue(getContext());
        String url = "https://openapi.gg.go.kr/Animalhosptl?Key=beeaf0846bc2484e8ed3aaa6e134a94e&Type=json&pIndex=1&pSize=100";

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray list = response.getJSONArray("Animalhosptl");
                    JSONObject list1 = (JSONObject) list.get(1);

                    JSONArray row = (JSONArray) list1.get("row");



                    String hname;
                    String inservice;
                    String hphone;
                    String latitude;
                    String longitude;

                    for(int i = 0; i < row.length(); i++){
                        JSONObject object = row.getJSONObject(i);
                        hname = object.get("BIZPLC_NM").toString();
                        inservice = object.get("BSN_STATE_NM").toString();
                        hphone = object.get("LOCPLC_FACLT_TELNO").toString();
                        latitude = object.get("REFINE_WGS84_LAT").toString();
                        longitude = object.get("REFINE_WGS84_LOGT").toString();

                        double lat = Double.parseDouble(latitude);
                        double lon = Double.parseDouble(longitude);
                        LatLng curPos = new LatLng(lat, lon);
                        if(inservice.equals("정상")){
                            if(hLocationMarker == null ){
                                hLocationMarker = new MarkerOptions();
                                hLocationMarker.position(curPos);
                                hLocationMarker.title(hname + "\n");
                                hLocationMarker.snippet("전화번호: " + hphone);
                                hLocationMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.iconfinder_ic_local_hospital_24px_352501));
                                map.addMarker(hLocationMarker);
                            }else{
                                hLocationMarker.position(curPos);
                                hLocationMarker.title(hname + "\n");
                                hLocationMarker.snippet("전화번호: " + hphone);
                                map.addMarker(hLocationMarker);

                            }
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        jsonRequest.setTag(TAG);
        queue.add(jsonRequest);




    }

    public void pharmacyLocationService(){

        queue = Volley.newRequestQueue(getContext());
        String url = "https://openapi.gg.go.kr/AnimalPharmacy?Key=beeaf0846bc2484e8ed3aaa6e134a94e&Type=json&pIndex=1&pSize=100";

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray list = response.getJSONArray("AnimalPharmacy");
                    JSONObject list1 = (JSONObject) list.get(1);

                    JSONArray row = (JSONArray) list1.get("row");

                    String pname;
                    String inservice;
                    String pphone;
                    String latitude;
                    String longitude;

                    for(int i = 0; i < row.length(); i++){
                        JSONObject object = row.getJSONObject(i);
                        pname = object.get("BIZPLC_NM").toString();
                        inservice = object.get("BSN_STATE_NM").toString();
                        pphone = object.get("LOCPLC_FACLT_TELNO").toString();
                        latitude = object.get("REFINE_WGS84_LAT").toString();
                        longitude = object.get("REFINE_WGS84_LOGT").toString();

                        double lat = Double.parseDouble(latitude);
                        double lon = Double.parseDouble(longitude);
                        LatLng curPos = new LatLng(lat, lon);

                        if(pLocationMarker == null ){
                            pLocationMarker = new MarkerOptions();
                            pLocationMarker.position(curPos);
                            pLocationMarker.title(pname + "\n");
                            pLocationMarker.snippet("전화번호: " + pphone);
                            pLocationMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.iconfinder_ic_local_pharmacy_24px_352509));
                            map.addMarker(pLocationMarker);
                        }else{
                            pLocationMarker.position(curPos);
                            pLocationMarker.title(pname + "\n");
                            pLocationMarker.snippet("전화번호: " + pphone);
                            map.addMarker(pLocationMarker);

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        jsonRequest.setTag(TAG);
        queue.add(jsonRequest);


    }


}
