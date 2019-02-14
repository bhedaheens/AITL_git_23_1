package com.allintheloop.Bean.GeoLocation;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;


import com.allintheloop.MainActivity;
import com.allintheloop.Util.SessionManager;

import java.util.ArrayList;

public class MyLocationListener implements LocationListener {

    Context context;
    SessionManager sessionManager;
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public Location previousBestLocation = null;
    private static final String TAG = "MyLocationListener";
    ArrayList<GeoLocationData.GeoLocationList> locationLists;


    public MyLocationListener(Context context, ArrayList<GeoLocationData.GeoLocationList> locationLists, SessionManager sessionManager) {
        this.context = context;
        this.locationLists = locationLists;
        this.sessionManager = sessionManager;
    }

    public void onLocationChanged(final Location loc) {
        if (isBetterLocation(loc, previousBestLocation)) {

            Log.d(TAG, "onLocationChanged() called with: loc Latitude  = [" + loc.getLatitude() + "]" + "" + "onLocationChanged() called with: loc longitude  = [" + loc.getLongitude() + "]" + "");
            for (int i = 0; i < locationLists.size(); i++) {
                double lat = Double.parseDouble(locationLists.get(i).getLat());
                double longitude = Double.parseDouble(locationLists.get(i).getLong());
                int radius = Integer.parseInt(locationLists.get(i).getRadius());
                if (distance(loc.getLatitude(), loc.getLongitude(), lat, longitude) < radius && !locationLists.get(i).getStatus()) {
//                    ToastC.show(context,locationLists.get(i).getTitle());
                    locationLists.get(i).setStatus(true);
                    ((MainActivity) context).sendGeoLocationBasedNotification(locationLists.get(i).getId(), i);
                }
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public void onProviderDisabled(String provider) {
//        Toast.makeText(context, "Gps Disabled", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onProviderDisabled() called with: provider = [" + provider + "]");
    }


    public void onProviderEnabled(String provider) {
//        Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onProviderEnabled() called with: provider = [" + provider + "]");
    }


    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }


    public static double distance(double lat1, double lon1, double lat2,
                                  double lon2) {

        Location loc1 = new Location("");

        loc1.setLatitude(lat1);
        loc1.setLongitude(lon1);

        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lon2);

        return loc1.distanceTo(loc2);
    }

}
