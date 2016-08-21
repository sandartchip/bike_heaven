package com.shale.gpx_jxl_2;

import android.app.Activity;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class MainActivity extends Activity{
    private LocationManager locationManager = null; // 위치 정보 프로바이더
    private LocationListener locationListener = null; //위치 정보가 업데이트시 동작

    private Button btnGetLocation ;
    private TextView textLocation = null;
    private ProgressBar pb = null;

    private static final String TAG = "debug";
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private double longitude, latitude;
    public static String PACKAGE_NAME;
    String location_detail;
    Button btn_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);

        textLocation = (TextView) findViewById(R.id.locationText);


        PACKAGE_NAME = getApplicationContext().getPackageName();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        pb = (ProgressBar) findViewById(R.id.progressBar1);
        pb.setVisibility(View.INVISIBLE);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //GPS_PROVIDER: GPS를 통해 위치를 알려줌
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        //NETWORK_PROVIDER: WI-FI 네트워크나 통신사의 기지국 정보를 통해 위치를 알려줌
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isGPSEnabled && isNetworkEnabled) {
            textLocation.setText("방위가 바뀔때 까지 기다려주세요...");

            pb.setVisibility(View.VISIBLE);
            locationListener = new MyLocationListener();

            //선택된 프로바이더를 사용해 위치정보를 업데이트
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);
            }
            catch (SecurityException e) {
            }

        } else {
            alertbox("gps 상태!!", "당신의 gps 상태 : off");
        }
    }


    //현재 위치 정보를 받기위해 선택한 프로바이더에 위치 업데이터 요청! requestLocationUpdates()메소드를 사용함.
    private class MyLocationListener implements LocationListener {

        @Override
        //LocationListener을 이용해서 위치정보가 업데이트 되었을때 동작 구현
        public void onLocationChanged(Location loc) {
            textLocation.setText("");
            pb.setVisibility(View.INVISIBLE);

            longitude =   loc.getLongitude();
            latitude =    loc.getLatitude();

            String longiStr, latiStr;

            longiStr = Double.toString(longitude);
            latiStr =  Double.toString(latitude);

            Log.i("longi", longiStr);
            Log.i("lati", latiStr);
            // 도시명 구하기
            String cityName, state, country, guName, dongName, bunjiName, featureName;

            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;

            try {
                addresses = gcd.getFromLocation(latitude, longitude, 1);
                if (addresses.size() > 0) {

                    cityName = addresses.get(0).getAdminArea(); //시 이름
                    guName = addresses.get(0).getLocality(); //구 이름

                    dongName = addresses.get(0).getThoroughfare(); //동
                    bunjiName = addresses.get(0).getSubThoroughfare(); //번지

                    location_detail = cityName + " " + guName + " "  + dongName+ " " + bunjiName;
                    textLocation.setText(location_detail);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //도시 명 구하기 끝
            sendToListActivity();
            //리스트뷰 출력
        }

        public void sendToListActivity(){

            //쓰레기같긴 하지만 일단 계산 다한뒤에 putExtra하는걸로..
            Log.i("start_new","gg");
            Intent i = new Intent(getApplicationContext(), List_Activity.class);

            i.putExtra("longitude", longitude);
            i.putExtra("latitude", latitude);
            i.putExtra("locationText", location_detail);

            startActivity(i);

            finish();
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stubz
        }

    }

    // GPS 안켜졌을 때 동작함
    protected void alertbox(String title, String mymessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("your device's gps is disable")
                .setCancelable(false)
                .setTitle("**gps status**")
                .setPositiveButton("gps on", new DialogInterface.OnClickListener() {

                    //  폰 위치 설정 페이지로 넘어감
                    public void onClick(DialogInterface dialog, int id) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
