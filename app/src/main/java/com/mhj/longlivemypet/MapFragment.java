package com.mhj.longlivemypet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class MapFragment extends Fragment implements OnMapReadyCallback{
    MapView mapView;
    GoogleMap map;
    BottomNavigationView bottomNavigationView;
    MarkerOptions hLocationMarker, pLocationMarker;
    LocationManager manager;
    MainActivity mainActivity;

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
        mapView = rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this); // 비동기적 방식으로 구글 맵 실행

        bottomNavigationView = rootView.findViewById(R.id.bottom_navigation);
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
        //GPS on/off 확인
        if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //GPS 켜달라는 요청
            Toast.makeText(getContext(), "위치 추적 기능을 켜주세요",Toast.LENGTH_SHORT).show();
        }
        map.setMyLocationEnabled(true);
        map.getUiSettings(). setMyLocationButtonEnabled(false);
        map.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        map.animateCamera(CameraUpdateFactory.zoomTo(10));
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        manager = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);

    }

    public void startLocationService(){
        try {
            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location == null){
                location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if(location != null){
                // 기준점 : 날짜변경선 영국 그리니티천문대
                double latitude = location.getLatitude(); // 위도 - 가로좌표
                double longitude = location.getLongitude(); // 경도 - 세로좌표
                showCurrentLocation(latitude, longitude);
                String message = "startLocationService() 최근 위치\nLat: " + latitude + "\nLon: " + longitude;
                Log.e(TAG, message);

                MapFragment.GPSListener gpsListener = new MapFragment.GPSListener();
                long minTime = 10000; //10초 타임 아웃
                float minDistance = 0; // 0미터 오차허용범위
                manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener); //리스너 등록

            }else{
                Log.e(TAG, "위치로드 실패");
            }
        }catch (SecurityException e){
            Log.e(TAG, "오류발생");
            e.printStackTrace();
        }
    }

    class GPSListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude(); //위도
            double longitude = location.getLongitude();// 경도
            String message = "내 위치 -> Lat:" + latitude + "\nLon:" + longitude;
            Log.e("MapActivity" , message);

        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { }
        @Override
        public void onProviderEnabled(String provider) { }
        @Override
        public void onProviderDisabled(String provider) { }
    }

    private void  showCurrentLocation(Double lat, Double lon){
        LatLng curPos = new LatLng(lat, lon);
        this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPos, 15));
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
                    String newAddress;
                    String oldAddress;

                    for(int i = 0; i < row.length(); i++){
                        JSONObject object = row.getJSONObject(i);
                        hname = object.get("BIZPLC_NM").toString();
                        inservice = object.get("BSN_STATE_NM").toString();
                        latitude = object.get("REFINE_WGS84_LAT").toString();
                        longitude = object.get("REFINE_WGS84_LOGT").toString();
                        newAddress = object.get("REFINE_ROADNM_ADDR").toString();
                        oldAddress = object.get("REFINE_LOTNO_ADDR").toString();

                        if(object.get("LOCPLC_FACLT_TELNO").toString().length() < 7){
                            hphone = "사업자미등록";
                        }
                        else {
                            hphone = object.get("LOCPLC_FACLT_TELNO").toString();
                        }

                        double lat = Double.parseDouble(latitude);
                        double lon = Double.parseDouble(longitude);
                        LatLng curPos = new LatLng(lat, lon);
                        if(inservice.equals("정상")){
                            if(hLocationMarker == null ){
                                if(!newAddress.equals(null))
                                    hLocationMarker = new MarkerOptions().position(curPos).title(hname + "\n").snippet("전화번호: " + hphone + "\n" + "주소:"+oldAddress).icon(BitmapDescriptorFactory.fromResource(R.drawable.iconfinder_ic_local_hospital_24px_352501));
                                else if(newAddress.equals(null) && !oldAddress.equals(null))
                                    hLocationMarker = new MarkerOptions().position(curPos).title(hname + "\n").snippet("전화번호: " + hphone + "\n" + "주소:"+newAddress).icon(BitmapDescriptorFactory.fromResource(R.drawable.iconfinder_ic_local_hospital_24px_352501));
                                else
                                    hLocationMarker = new MarkerOptions().position(curPos).title(hname + "\n").snippet("전화번호: " + hphone + "\n" + "주소: 사업자 미등록").icon(BitmapDescriptorFactory.fromResource(R.drawable.iconfinder_ic_local_hospital_24px_352501));
                                map.addMarker(hLocationMarker);

                            }else{
                                if(!newAddress.equals(null))
                                    hLocationMarker = new MarkerOptions().position(curPos).title(hname + "\n").snippet("전화번호: " + hphone + "\n" + "주소:"+oldAddress).icon(BitmapDescriptorFactory.fromResource(R.drawable.iconfinder_ic_local_hospital_24px_352501));
                                else if(newAddress.equals(null) && !oldAddress.equals(null))
                                    hLocationMarker = new MarkerOptions().position(curPos).title(hname + "\n").snippet("전화번호: " + hphone + "\n" + "주소:"+newAddress).icon(BitmapDescriptorFactory.fromResource(R.drawable.iconfinder_ic_local_hospital_24px_352501));
                                else
                                    hLocationMarker = new MarkerOptions().position(curPos).title(hname + "\n").snippet("전화번호: " + hphone + "\n" + "주소: 사업자 미등록").icon(BitmapDescriptorFactory.fromResource(R.drawable.iconfinder_ic_local_hospital_24px_352501));
                                map.addMarker(hLocationMarker);

                            }
                            map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                @Override
                                public View getInfoWindow(Marker arg0) {
                                    return null;
                                }

                                @Override
                                public View getInfoContents(Marker marker) {

                                    LinearLayout info = new LinearLayout(getContext());
                                    info.setOrientation(LinearLayout.VERTICAL);
                                    info.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                    TextView title = new TextView(getContext());
                                    title.setTextColor(Color.parseColor("#B96548"));
                                    title.setGravity(Gravity.CENTER);
                                    title.setTypeface(null, Typeface.BOLD);
                                    title.setText(marker.getTitle());

                                    TextView snippet = new TextView(getContext());
                                    snippet.setTextColor(Color.GRAY);
                                    snippet.setText(marker.getSnippet());

                                    info.addView(title);
                                    info.addView(snippet);

                                    GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
                                        @Override
                                        public void onInfoWindowClick(Marker marker) {
                                            String pnum = null;
                                            String num1 = marker.getSnippet();
                                            int idx = num1.indexOf("\n");
                                            String num2 = num1.substring(6, idx);
                                            if(num2.length() >= 7) {
                                                pnum = num2.replace("-", "");
                                                if (pnum.length() <= 7) {
                                                    pnum = "031" + pnum;
                                                    Log.e("tes", pnum);
                                                }
                                            } else if(num2.equals("사업자미등록")){
                                                pnum = "사업자미등록";
                                            }
                                            calling(pnum);
                                        }
                                    };
                                    map.setOnInfoWindowClickListener(infoWindowClickListener);

                                    return info;
                                }
                            });

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

    public void calling(String pnum){
        if(pnum.equals("사업자미등록")){
            Toast.makeText(getContext(), "사업자 전화번호 미등록",Toast.LENGTH_SHORT).show();
        } else {
            Intent call = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + pnum));
            getActivity().startActivity(call);
        }
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
                    String newAddress;
                    String oldAddress;

                    for(int i = 0; i < row.length(); i++){
                        JSONObject object = row.getJSONObject(i);
                        pname = object.get("BIZPLC_NM").toString();
                        inservice = object.get("BSN_STATE_NM").toString();
                        latitude = object.get("REFINE_WGS84_LAT").toString();
                        longitude = object.get("REFINE_WGS84_LOGT").toString();
                        newAddress = object.get("REFINE_ROADNM_ADDR").toString();
                        oldAddress = object.get("REFINE_LOTNO_ADDR").toString();

                        if(object.get("LOCPLC_FACLT_TELNO").toString().length() < 7){
                            pphone = "사업자미등록";
                        }
                        else {
                            pphone = object.get("LOCPLC_FACLT_TELNO").toString();
                        }

                        double lat = Double.parseDouble(latitude);
                        double lon = Double.parseDouble(longitude);
                        LatLng curPos = new LatLng(lat, lon);

                        if(inservice.equals("정상")) {
                            if (pLocationMarker == null) {
                                if(!newAddress.equals(null))
                                    pLocationMarker = new MarkerOptions().position(curPos).title(pname + "\n").snippet("전화번호: " + pphone + "\n" + "주소:"+oldAddress).icon(BitmapDescriptorFactory.fromResource(R.drawable.iconfinder_ic_local_pharmacy_24px_352509));
                                else if(newAddress.equals(null) && !oldAddress.equals(null))
                                    pLocationMarker = new MarkerOptions().position(curPos).title(pname + "\n").snippet("전화번호: " + pphone + "\n" + "주소:"+newAddress).icon(BitmapDescriptorFactory.fromResource(R.drawable.iconfinder_ic_local_pharmacy_24px_352509));
                                else
                                    pLocationMarker = new MarkerOptions().position(curPos).title(pname + "\n").snippet("전화번호: " + pphone + "\n" + "주소: 사업자 미등록"+oldAddress).icon(BitmapDescriptorFactory.fromResource(R.drawable.iconfinder_ic_local_pharmacy_24px_352509));
                                map.addMarker(pLocationMarker);
                            } else {
                                if(!newAddress.equals(null))
                                    pLocationMarker = new MarkerOptions().position(curPos).title(pname + "\n").snippet("전화번호: " + pphone + "\n" + "주소:"+oldAddress).icon(BitmapDescriptorFactory.fromResource(R.drawable.iconfinder_ic_local_pharmacy_24px_352509));
                                else if(newAddress.equals(null) && !oldAddress.equals(null))
                                    pLocationMarker = new MarkerOptions().position(curPos).title(pname + "\n").snippet("전화번호: " + pphone + "\n" + "주소:"+newAddress).icon(BitmapDescriptorFactory.fromResource(R.drawable.iconfinder_ic_local_pharmacy_24px_352509));
                                else
                                    pLocationMarker = new MarkerOptions().position(curPos).title(pname + "\n").snippet("전화번호: " + pphone + "\n" + "주소: 사업자 미등록"+oldAddress).icon(BitmapDescriptorFactory.fromResource(R.drawable.iconfinder_ic_local_pharmacy_24px_352509));
                                map.addMarker(pLocationMarker);
                            }
                            map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                @Override
                                public View getInfoWindow(Marker arg0) {
                                    return null;
                                }

                                @Override
                                public View getInfoContents(Marker marker) {

                                    LinearLayout info = new LinearLayout(getContext());
                                    info.setOrientation(LinearLayout.VERTICAL);
                                    info.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                    TextView title = new TextView(getContext());
                                    title.setTextColor(Color.parseColor("#B96548"));
                                    title.setGravity(Gravity.CENTER);
                                    title.setTypeface(null, Typeface.BOLD);
                                    title.setText(marker.getTitle());

                                    TextView snippet = new TextView(getContext());
                                    snippet.setTextColor(Color.GRAY);
                                    snippet.setText(marker.getSnippet());

                                    info.addView(title);
                                    info.addView(snippet);

                                    GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
                                        @Override
                                        public void onInfoWindowClick(Marker marker) {
                                            String pnum = null;
                                            String num1 = marker.getSnippet();
                                            int idx = num1.indexOf("\n");
                                            String num2 = num1.substring(6, idx);
                                            if(num2.length() >= 7) {
                                                pnum = num2.replace("-", "");
                                                if (pnum.length() <= 7) {
                                                    pnum = "031" + pnum;
                                                    Log.e("tes", pnum);
                                                }
                                            } else if(num2.equals("사업자미등록")){
                                                pnum = "사업자미등록";
                                            }
                                            calling(pnum);
                                        }
                                    };
                                    map.setOnInfoWindowClickListener(infoWindowClickListener);

                                    return info;
                                }
                            });
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
