package org.example.locationtest.locationtest;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class LocationTest extends Activity implements LocationListener {

    private LocationManager mgr;
    private TextView output;
    private String best;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_test);

        mgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        output = (TextView) findViewById(R.id.output);

        log("Location providers : ");
        dumpProviders();

        Criteria criteria = new Criteria();
        best = mgr.getBestProvider(criteria, true);

        log("Best provider is : " + best);

        /*
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }*/

        // 권한을 가지고 있는지 체크
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if(permissionCheck== PackageManager.PERMISSION_DENIED){
            // 권한 없음
            Log.d("","권한없음!");
        }else{
            // 권한 있음
            Log.d("","권한 있음!");
        }


        log("Locations (starting with last known) : ");
        Location location = mgr.getLastKnownLocation(best);
        dumpLocation(location);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 권한을 가지고 있는지 체크
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if(permissionCheck== PackageManager.PERMISSION_DENIED){
            // 권한 없음
            Log.d("","권한없음!");
        }else{
            // 권한 있음
            Log.d("","권한 있음!");
        }

        mgr.requestLocationUpdates(best, 15000, 1 , this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // 권한을 가지고 있는지 체크
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if(permissionCheck== PackageManager.PERMISSION_DENIED){
            // 권한 없음
            Log.d("","권한없음!");
        }else{
            // 권한 있음
            Log.d("","권한 있음!");
        }

        mgr.removeUpdates(this);
    }

    // 人間が解析できる名前を定義
    private static final String[] A = {"invalid" , "n/a", "fine", "coarse"};
    private static final String[] P = {"invalid", "n/a", "low", "medium", "high"};
    private static final String[] S = {"out of service", "temporarily unavailable", "available"};

    /* 出力ウィンドウに文字列を書き込む*/
    private void log (String string){

        output.append(string + "\n");
    }

    /* あらゆる位置プロバイダーからの情報を書き込む*/
    private void dumpProviders(){
        List<String> providers = mgr.getAllProviders();
        for (String provider : providers){
            dumpProvider(provider);
        }
    }

    private void dumpProvider(String provider){
        LocationProvider info = mgr.getProvider(provider);
        StringBuilder builder = new StringBuilder();
        builder.append("LocationProvider[")
                .append("name=")
                .append(info.getName())
                .append(", enabled=")
                .append(mgr.isProviderEnabled(provider))
                .append(", getAccuracy=")
                .append(A[info.getAccuracy() + 1])
                .append(", getPowerRequirement=")
                .append(P[info.getPowerRequirement() + 1])
                .append(", hasMonetaryCost=")
                .append(info.hasMonetaryCost())
                .append(", requiresCell=")
                .append(info.requiresCell())
                .append(", requiresSatellite=")
                .append(info.requiresSatellite())
                .append(", requiresNetwork=")
                .append(info.requiresNetwork())
                .append(", requiresAltitude=")
                .append(info.supportsAltitude())
                .append(", requiresBearing=")
                .append(info.supportsBearing())
                .append(", reuqiresSpeed=")
                .append(info.supportsSpeed())
                .append("]");

        log(builder.toString());
    }

    private void dumpLocation(Location location){
        if (location == null)
            log("\nLocation[unkown]");
        else
            log("\n" + location.toString());
    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onLocationChanged(Location location) {
        dumpLocation(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
