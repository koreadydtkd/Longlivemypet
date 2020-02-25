package com.mhj.longlivemypet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    ImageView imgWeather;
    TextView txtWeather, txtTemp, txtRain, txtWind, txtRainper, txtWet, txtDust, txtNanodust, txtLocation;
    String address, address2, address3, sKey ,row, basedate, basetime, basetime2, nx, ny ,tmx ,tmy, type, station, simpletime,simpletime2,today,yesterday;
    SimpleDateFormat dateText = new SimpleDateFormat("yyyyMMdd"); //날짜 표시 형식
    SimpleDateFormat hourText = new SimpleDateFormat("HHmm"); //시간 표시 형식
    Button btnRefresh;
    long numtime; //현재 시간
    LocationManager manager;
    private RequestQueue queue;
    private static String clientId = "RyFahfSSqpWCX29EC4Or";
    private static String clientSecret = "KRGCcvYHDQ";
    static RequestQueue requestQueue;
    RecyclerView recyclerView;
    NewsAdapter adapter;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("잠시만 기다려주세요...");


        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(getContext());
        }

        imgWeather = rootView.findViewById(R.id.imgWeather);
        txtWeather = rootView.findViewById(R.id.txtWeather);
        txtTemp = rootView.findViewById(R.id.txtTemp);
        txtWind = rootView.findViewById(R.id.txtWind);
        txtRainper = rootView.findViewById(R.id.txtRainper);
        txtWet = rootView.findViewById(R.id.txtWet);
        txtRain= rootView.findViewById(R.id.txtRain);
        txtDust = rootView.findViewById(R.id.txtDust);
        txtNanodust = rootView.findViewById(R.id.txtNanodust);
        txtLocation = rootView.findViewById(R.id.txtLocation);
        recyclerView = rootView.findViewById(R.id.recyclerView);

        address = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst?";
        address2 = "http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getNearbyMsrstnList?";
        address3 = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?";
        sKey = "serviceKey=swlEfys72fvDOcvi5LfN3wFzJEdZoWn0QblHo2VWxkyff4hoQ83W5mEI5MGXQ2TRkYK%2BXR%2B8NtErVqrp%2BOUIGg%3D%3D";
        row = "&numOfRows=100&dataType=JSON&pageNo=1";
        type = "&_returnType=json";

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NewsAdapter();
        recyclerView.setAdapter(adapter);


        getNaverNews();

        if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //GPS 켜달라는 요청
            Toast.makeText(getContext(), "위치 추적 기능을 켜주세요",Toast.LENGTH_SHORT).show();
        } else if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            setWhether();

            btnRefresh = rootView.findViewById(R.id.btnRefresh);
            btnRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                        //GPS 켜달라는 요청
                        Toast.makeText(getContext(), "위치 추적 기능을 켜주세요",Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        setWhether();
                    }
                }
            });
        }



        return rootView;
    }
    private void setWhether() {
        Date today = new Date();
        setTime(today);
        setDay(today);
        startLocationService();
        queue = Volley.newRequestQueue(getContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String weather = address + sKey + row + basedate + basetime + nx + ny;
                String weather2 = address + sKey + row + basedate + basetime2 + nx + ny;
                String dust_station = address2 + tmx + tmy + sKey + type;

                Log.e("날씨 api:", weather);
                Log.e("강수량 api:", weather2);
                Log.e("측정소 api:", dust_station);

                //여기에 딜레이 후 시작할 작업들을 입력
                tellMetheWeather(weather);
                tellMetheWeather2(weather2);
                tellMetheStation(dust_station);

                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

            }
        }, 500);
    }

    public void setTime(Date date) {
        String now = hourText.format(date);
        numtime = Long.parseLong(now);
        basetimeCalc();
        basetimeCalc2();
        timeCalc();
        timeCalc2();
    }

    public void setDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        today = dateText.format(date);
        yesterday = dateText.format(calendar.getTime());

        if(numtime >= 0 && numtime < 300){
            basedate = "&base_date=" + yesterday;
        }else if(numtime >=300 && numtime < 600) {
            basedate = "&base_date=" + yesterday;
        } else{
            basedate = "&base_date=" + dateText.format(date);
        }
    }

    public void basetimeCalc(){
        if(numtime >= 0 && numtime < 300){
            basetime = "&base_time=2000";
        }else if(numtime >=300 && numtime < 600){
            basetime = "&base_time=2300";
        }else if(numtime >=600 && numtime < 900){
            basetime = "&base_time=0200";
        }else if(numtime >=900 && numtime < 1200){
            basetime = "&base_time=0500";
        }else if(numtime >=1200 && numtime < 1500){
            basetime = "&base_time=0800";
        }else if(numtime >=1500 && numtime < 1800){
            basetime = "&base_time=1100";
        }else if(numtime >=1800 && numtime < 2100){
            basetime = "&base_time=1400";
        }else if(numtime >=2100 && numtime <= 2359){
            basetime = "&base_time=1700";
        }
    }

    public void basetimeCalc2(){
        if(numtime >= 0 && numtime < 600){
            basetime2 = "&base_time=2000";
        }else if(numtime >=600 && numtime < 1200){
            basetime2 = "&base_time=0200";
        }else if(numtime >=1200 && numtime < 1800){
            basetime2 = "&base_time=0800";
        }else if(numtime >=1800 && numtime < 2359){
            basetime2 = "&base_time=1400";
        }
    }

    public void timeCalc(){
        if(numtime >= 0 && numtime < 300){
            simpletime = "0000";
        }else if(numtime >=300 && numtime < 600){
            simpletime = "0300";
        }else if(numtime >=600 && numtime < 900){
            simpletime = "0600";
        }else if(numtime >=900 && numtime < 1200){
            simpletime = "0900";
        }else if(numtime >=1200 && numtime < 1500){
            simpletime = "1200";
        }else if(numtime >=1500 && numtime < 1800){
            simpletime = "1500";
        }else if(numtime >=1800 && numtime < 2100){
            simpletime = "1800";
        }else if(numtime >=2100 && numtime <= 2359){
            simpletime = "2100";
        }
    }

    public void timeCalc2(){
        if(numtime >= 0 && numtime < 600){
            simpletime2 = "0000";
        }else if(numtime >=600 && numtime < 1200){
            simpletime2 = "0600";
        }else if(numtime >=1200 && numtime < 1800){
            simpletime2 = "1200";
        }else if(numtime >=1800 && numtime < 2359){
            simpletime2 = "1800";
        }
    }

    private void startLocationService() {
        try {
            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location == null){
                location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if(location != null){
                // 기준점 : 날짜변경선 영국 그리니티천문대
                double latitude = location.getLatitude(); // 위도 - 가로좌표
                double longitude = location.getLongitude(); // 경도 - 세로좌표

                HomeFragment.GPSListener gpsListener = new HomeFragment.GPSListener();
                long minTime = 10000; //10초 타임 아웃
                float minDistance = 0; // 0미터 오차허용범위
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        minTime, minDistance, gpsListener); //리스너 등록

                int lat = (int)latitude;
                int lon = (int)longitude;

                nx = "&nx=" + lat;
                ny = "&ny=" + lon;

                GeoPoint in_pt = new GeoPoint(longitude, latitude);
                GeoPoint tm_pt = GeoTrans.convert(GeoTrans.GEO, GeoTrans.TM, in_pt);

                tmx = "tmX=" + tm_pt.getX();
                tmy = "&tmY="+ tm_pt.getY() + "&";

                String message = "startLocationService() 최근 위치\nLat: " + latitude + "\nLon: " + longitude;
                String message2 = "geo in : xGeo="  + in_pt.getX() + ", yGeo=" + in_pt.getY();
                String message3 = "tm : xTM=" + tm_pt.getX() + ", yTM=" + tm_pt.getY();

                Log.e(TAG, message);
                Log.e(TAG, message2);
                Log.e(TAG, message3);
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

    public void tellMetheWeather(String url1) {
        Log.e("tellMetheWeather", "가동중");
        final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject list = response.getJSONObject("response");
                    JSONObject body = list.getJSONObject("body");
                    JSONObject items = body.getJSONObject("items");
                    JSONArray item = items.getJSONArray("item");
                    for (int i = 0; i < item.length(); i++) {
                        JSONObject object = item.getJSONObject(i);
                        double rain = 0;
                        if(object.getString("fcstTime").equals(simpletime) && object.getString("fcstDate").equals(today) ) {
                            Log.e("WeatherFragment", object.getString("category"));

                            if(object.getString("category").equals("POP")){
                                txtRainper.setText(object.get("fcstValue").toString() + "%");
                                Log.e("WeatherFragment", object.get("fcstValue").toString());
                            }

                            if(object.getString("category").equals("PTY")){
                                Log.e("WeatherFragment", object.get("fcstValue").toString());
                                rain = Double.parseDouble(object.get("fcstValue").toString());
                                if(rain == 0) {
                                    if(object.getString("category").equals("SKY")) {

                                        int sky = Integer.parseInt(object.get("fcstValue").toString());
                                        Log.e("WeatherFragment", sky + "단계");

                                        if (sky == 1 || sky == 2) {
                                            Log.e("WeatherFragment", numtime + "시");
                                            if (numtime >= 0600 && numtime < 1800) {
                                                imgWeather.setImageResource(R.drawable.sunny);
                                                txtWeather.setText("맑음");
                                            } else {
                                                imgWeather.setImageResource(R.drawable.night);
                                                txtWeather.setText("맑음");
                                            }
                                        } else if (sky == 3) {
                                            if (numtime >= 0600 && numtime < 1800) {
                                                imgWeather.setImageResource(R.drawable.cloud);
                                                txtWeather.setText("구름 조금");
                                            } else {
                                                imgWeather.setImageResource(R.drawable.cloudnight_118960);
                                                txtWeather.setText("구름 조금");
                                            }
                                        } else if (sky == 4) {
                                            imgWeather.setImageResource(R.drawable.cloundy_118962);
                                            txtWeather.setText("흐림");
                                        }

                                    }
                                }else if(rain >= 1 && rain < 2){
                                    imgWeather.setImageResource(R.drawable.rain);
                                    txtWeather.setText("비");
                                } else if(rain >= 2 && rain < 3){
                                    imgWeather.setImageResource(R.drawable.alert);
                                    txtWeather.setText("비또는 눈");
                                } else if(rain >= 3 && rain < 4){
                                    imgWeather.setImageResource(R.drawable.snow);
                                    txtWeather.setText("눈");
                                } else if(rain >= 4){
                                    imgWeather.setImageResource(R.drawable.lightrain);
                                    txtWeather.setText("소나기");
                                }
                            }

                            if(object.getString("category").equals("REH")){
                                txtWet.setText(object.get("fcstValue").toString() + "%");
                                Log.e("WeatherFragment", object.get("fcstValue").toString() + "%");
                            }



                            if (object.getString("category").equals("T3H")) {
                                txtTemp.setText(object.get("fcstValue").toString() + "ºc");
                                Log.e("WeatherFragment", object.get("fcstValue").toString() + "ºc");
                            }

                            if(object.getString("category").equals("WSD")){
                                double w = Double.parseDouble(object.get("fcstValue").toString());
                                if(w < 4){
                                    txtWind.setText("약한바람");
                                } else if( w >= 4 && w < 9){
                                    txtWind.setText("약간강한바람");
                                } else if( w >= 9 && w < 14){
                                    txtWind.setText("강한바람");
                                } else if(w > 14){
                                    txtWind.setText("매우강한바람");
                                }
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
                Log.e("TEST", "오류");
                error.printStackTrace();
            }
        });
        jsonRequest.setTag(TAG);
        queue.add(jsonRequest);
    }

    public void tellMetheWeather2(String url2) {
        Log.e("tellMetheWeather2", "가동중");
        final JsonObjectRequest jsonRequest2 = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject list = response.getJSONObject("response");
                    JSONObject body = list.getJSONObject("body");
                    JSONObject items = body.getJSONObject("items");
                    JSONArray item = items.getJSONArray("item");
                    for (int i = 0; i < item.length(); i++) {
                        JSONObject object = item.getJSONObject(i);
                        if(object.getString("fcstTime").equals(simpletime2) && object.getString("fcstDate").equals(today) ) {
                            Log.e("WeatherFragment", object.getString("category"));
                            if(object.getString("category").equals("R06")){
                                txtRain.setText(object.get("fcstValue").toString() + "mm");
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
                Log.e("TEST", "오류");
                error.printStackTrace();
            }
        });
        jsonRequest2.setTag(TAG);
        queue.add(jsonRequest2);
    }

    public void tellMetheStation(String url3){
        final JsonObjectRequest jsonRequest3 = new JsonObjectRequest(Request.Method.GET, url3, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray list = response.getJSONArray("list");
                        JSONObject object = list.getJSONObject(0);
                        station = "stationName=" + object.get("stationName").toString();
                        txtLocation.setText(object.get("stationName").toString());
                        Log.e("스테이션", station);
                        String dust_level = address3 + station + "&dataTerm=month&pageNo=1&numOfRows=10&" + sKey + "&ver=1.3" + type;
                        Log.e("미세먼지 api:", dust_level);
                        tellMetheDustlevel(dust_level);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TEST", "오류");
                error.printStackTrace();
            }
        });
        jsonRequest3.setTag(TAG);
        queue.add(jsonRequest3);
    }

    public void tellMetheDustlevel(String url4){
        final JsonObjectRequest jsonRequest4 = new JsonObjectRequest(Request.Method.GET, url4, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray list = response.getJSONArray("list");
                    JSONObject object = list.getJSONObject(0);
                    int micro_dust_level = Integer.parseInt(object.get("pm10Grade").toString());
                    int nano_dust_level = Integer.parseInt(object.get("pm25Grade").toString());
                    int micro_dust_value = Integer.parseInt(object.get("pm10Value").toString());
                    int nano_dust_value = Integer.parseInt(object.get("pm25Value").toString());

                    if(micro_dust_level == 1){
                        txtDust.setText("좋음" + "(" + micro_dust_value + "㎍/㎥" + ")");
                    } else if(micro_dust_level == 2){
                        txtDust.setText("보통"+ "(" + micro_dust_value + "㎍/㎥" + ")");
                    } else if(micro_dust_level == 3){
                        txtDust.setText("나쁨"+ "(" + micro_dust_value + "㎍/㎥" + ")");
                    } else if(micro_dust_level == 4){
                        txtDust.setText("매우나쁨" + "(" + micro_dust_value + "㎍/㎥" + ")");
                    }

                    if(nano_dust_level == 1){
                        txtNanodust.setText("좋음" + "(" + nano_dust_value + "㎍/㎥" +")");
                    } else if(nano_dust_level == 2){
                        txtNanodust.setText("보통"+ "(" + nano_dust_value +"㎍/㎥" + ")");
                    } else if(nano_dust_level == 3){
                        txtNanodust.setText("나쁨"+ "(" + nano_dust_value +"㎍/㎥" + ")");
                    } else if(nano_dust_level == 4){
                        txtNanodust.setText("매우나쁨"+ "(" + nano_dust_value +"㎍/㎥" + ")");
                    }

                    Log.e("미세먼지 단위:", "\n미세먼지:" + micro_dust_level + "\n초미세먼지:" + nano_dust_level);
                    Log.e("미세먼지 값:", "\n미세먼지농도:" + micro_dust_value + "\n초미세먼지농도" + nano_dust_value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TEST", "오류");
                error.printStackTrace();
            }
        });
        jsonRequest4.setTag(TAG);
        queue.add(jsonRequest4);
    }

    private void getNaverNews(){
        String url = "https://openapi.naver.com/v1/search/news?query=반려동물";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("items");
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject items = jsonArray.getJSONObject(i);
                                String title = items.getString("title");
                                String link = items.getString("link");
                                String description = items.getString("description");
                                adapter.addItem(new SearchDTO(title, link, description));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            adapter.notifyDataSetChanged();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "기사를 받아오지 못했습니다.", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap();
                params.put("X-Naver-Client-Id", clientId);
                params.put("X-Naver-Client-Secret", clientSecret);
                return params;
            }
        };
        requestQueue.add(request);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        manager = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }
}