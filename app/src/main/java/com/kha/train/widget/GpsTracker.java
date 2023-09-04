package com.kha.train.widget;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class GpsTracker extends Service implements LocationListener {
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 60000;
    boolean canGetLocation = false;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    double latitude;
    Location location;
    protected LocationManager locationManager;
    double longitude;
    private final Context mContext;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onLocationChanged(Location location) {
    }

    public void onProviderDisabled(String str) {
    }

    public void onProviderEnabled(String str) {
    }

    public void onStatusChanged(String str, int i, Bundle bundle) {
    }

    public GpsTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        String str = "network";
        String str2 = "gps";
        try {
            this.locationManager = (LocationManager) this.mContext.getSystemService(Context.LOCATION_SERVICE);
            this.isGPSEnabled = this.locationManager.isProviderEnabled(str2);
            this.isNetworkEnabled = this.locationManager.isProviderEnabled(str);
            if (!this.isGPSEnabled && !this.isNetworkEnabled) {
                return this.location;
            }
            this.canGetLocation = true;
            String str3 = "android.permission.ACCESS_COARSE_LOCATION";
            if (this.isNetworkEnabled) {
                if (!(ContextCompat.checkSelfPermission(this.mContext, str3) == 0 || ContextCompat.checkSelfPermission(this.mContext, str3) == 0)) {
                    ActivityCompat.requestPermissions((Activity) this.mContext, new String[]{str3, str3}, 101);
                }
                this.locationManager.requestLocationUpdates("network", MIN_TIME_BW_UPDATES, 10.0f, this);
                if (this.locationManager != null) {
                    this.location = this.locationManager.getLastKnownLocation(str);
                    if (this.location != null) {
                        this.latitude = this.location.getLatitude();
                        this.longitude = this.location.getLongitude();
                    }
                }
            }
            if (this.isGPSEnabled && this.location == null) {
                if (!(ContextCompat.checkSelfPermission(this.mContext, str3) == 0 || ContextCompat.checkSelfPermission(this.mContext, str3) == 0)) {
                    ActivityCompat.requestPermissions((Activity) this.mContext, new String[]{str3, str3}, 101);
                }
                this.locationManager.requestLocationUpdates("gps", MIN_TIME_BW_UPDATES, 10.0f, this);
                if (this.locationManager != null) {
                    this.location = this.locationManager.getLastKnownLocation(str2);
                    if (this.location != null) {
                        this.latitude = this.location.getLatitude();
                        this.longitude = this.location.getLongitude();
                    }
                }
            }
            return this.location;
        } catch (Exception e) {
            e.printStackTrace();
            return this.location;
        }
    }

    public double getLatitude() {
        Location location = this.location;
        if (location != null) {
            this.latitude = location.getLatitude();
        }
        return this.latitude;
    }

    public boolean isGPSEnable() {
        this.locationManager = (LocationManager) this.mContext.getSystemService(Context.LOCATION_SERVICE);
        this.isGPSEnabled = this.locationManager.isProviderEnabled("gps");
        return this.isGPSEnabled;
    }

    public double getLongitude() {
        Location location = this.location;
        if (location != null) {
            this.longitude = location.getLongitude();
        }
        return this.longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }
}
