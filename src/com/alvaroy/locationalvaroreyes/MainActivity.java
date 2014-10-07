package com.alvaroy.locationalvaroreyes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null) {
        	getSupportFragmentManager().beginTransaction().
        		add(R.layout.container, new PlaceholderFragment()).commit();
        }
    }
    
    public static class PlaceholderFragment extends Fragment implements LocationListener {

    	protected Button mButton;
    	protected EditText name;
		protected boolean mStarted;
		private LocationManager mLocationManager;
		private PlaceholderFragment listener = this;
		private String TAG = PlaceholderFragment.class.getSimpleName();
		double mLat;
		double mLong;
		double mAlt;
		double mSpeed;
    	
		@Override
		public void onLocationChanged(Location location) {
			mLat = location.getLatitude();
			mLong = location.getLongitude();
			mAlt = location.getAltitude();			
		}
		
		@Override
		public void onStop() {
			super.onStop();
			Log.d(TAG,"onStop");
			stopCapturing();
		}
		
		@Override
		public void onResume() {
			super.onResume();
			Log.d(TAG,"onResume");
			if (mStarted){
				startCapturing();
			} else {
				
			}
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
    	
		public void startCapturing() {
			boolean gps_enabled = true, network_enabled = true;
			mLocationManager = (LocationManager) getActivity()
					.getSystemService(Context.LOCATION_SERVICE);

			try {
				gps_enabled = mLocationManager
						.isProviderEnabled(LocationManager.GPS_PROVIDER);
			} catch (Exception ex) {

			}
			try {
				network_enabled = mLocationManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			} catch (Exception ex) {

			}
			Log.d(TAG, "gps_enabled " + gps_enabled + " network_enabled "
					+ network_enabled);
			if (!gps_enabled || !network_enabled) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						getActivity());
				dialog.setMessage("GPS no esta habilitado!").setPositiveButton(
						"OK",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(
									DialogInterface paramDialogInterface,
									int paramInt) {
								// TODO Auto-generated method stub
								// Intent myIntent = new Intent(
								// Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								// getActivity().startActivity(myIntent);
							}
						});
				dialog.show();

			} else {
				mLocationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 0, 0, listener);
				Toast.makeText(getActivity(), "Started", Toast.LENGTH_SHORT)
						.show();
				mStarted = !mStarted;
				mButton.setText("STOP");
			}
		}
		
		public void stopCapturing() {
			if (mLocationManager != null) {
				mLocationManager.removeUpdates(this);
			}
		}
		
    }
}
