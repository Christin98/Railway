package com.kha.train.widget;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender.SendIntentException;
import android.location.LocationManager;
import android.widget.Toast;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsRequest.Builder;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class GpsUtils {
    int GPS_REQUEST = 76;
    private Context context;
    private LocationManager locationManager;
    private LocationRequest locationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private SettingsClient mSettingsClient;

    public interface onGpsListener {
        void gpsStatus(boolean z);
    }

    public GpsUtils(Context context) {
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.mSettingsClient = LocationServices.getSettingsClient(context);
        this.locationRequest = LocationRequest.create();
        this.locationRequest.setPriority(100);
        this.locationRequest.setInterval(10000);
        this.locationRequest.setFastestInterval(2000);
        Builder addLocationRequest = new Builder().addLocationRequest(this.locationRequest);
        this.mLocationSettingsRequest = addLocationRequest.build();
        addLocationRequest.setAlwaysShow(true);
    }

    public void turnGPSOn(final onGpsListener ongpslistener) {
        if (!this.locationManager.isProviderEnabled("gps")) {
            this.mSettingsClient.checkLocationSettings(this.mLocationSettingsRequest).addOnSuccessListener((Activity) this.context, new OnSuccessListener<LocationSettingsResponse>() {
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    //onGpsListener ongpslistener = ongpslistener;
                    if (ongpslistener != null) {
                        ongpslistener.gpsStatus(true);
                    }
                }
            }).addOnFailureListener((Activity) this.context, new OnFailureListener() {
                public void onFailure(Exception exception) {
                    int statusCode = ((ApiException) exception).getStatusCode();
                    if (statusCode == 6) {
                        try {
                            ((ResolvableApiException) exception).startResolutionForResult((Activity) GpsUtils.this.context, GpsUtils.this.GPS_REQUEST);
                        } catch (SendIntentException unused) {
                        }
                    } else if (statusCode == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE) {
                        Toast.makeText((Activity) GpsUtils.this.context, "Location settings are inadequate, and cannot be fixed here. Fix in Settings.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else if (ongpslistener != null) {
            ongpslistener.gpsStatus(true);
        }
    }
}
