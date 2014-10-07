package com.alvaroy.locationalvaroreyes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
        		add(R.id.main, new PlaceholderFragment()).commit();
        }
    }
    
    public static class PlaceholderFragment extends Fragment implements LocationListener {

    	protected Button mButton;
    	protected Button sButton;
    	protected static EditText name;
		protected boolean mStarted;
		private LocationManager mLocationManager;
		private String TAG = PlaceholderFragment.class.getSimpleName();
		private View rootView;
    	
		@Override
		public View onCreateView(LayoutInflater inflater,
				ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.container, container, false);
			mButton = (Button) rootView.findViewById(R.id.button1);
			sButton = (Button) rootView.findViewById(R.id.button4);
			name = (EditText) rootView.findViewById(R.id.editText1);
			
			mButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(name.getText().toString().isEmpty()) {
						AlertDialog.Builder dialog = new AlertDialog.Builder(
								getActivity());
						dialog.setMessage("Escriba un nombre para el archivo antes de comenzar!").setPositiveButton("OK", null);
						dialog.show();
					} else {
						if(mStarted) {
							stopCapturing();
							Toast.makeText(rootView.getContext(), "Detenido", Toast.LENGTH_SHORT).show();
							mStarted = !mStarted;
							mButton.setText("Comenzar");
						} else {
							startCapturing();
						}
					}					
				}
			});
			
			sButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					File f = new File(Environment.getExternalStorageDirectory()+"/Android/data/com.alvaroy.locationalvaroreyes"+File.separator+name.getText().toString()+".txt");
					if(f.exists()) { 
						Uri u = Uri.fromFile(f);
						Intent emailIntent = new Intent(Intent.ACTION_SEND);
						// set the type to 'email'
						emailIntent.setType("vnd.android.cursor.dir/email");
						// the attachment
						emailIntent.putExtra(Intent.EXTRA_STREAM, u);
						startActivity(Intent.createChooser(emailIntent , "Enviando correo..."));
					} else {
						AlertDialog.Builder dialog = new AlertDialog.Builder(
								getActivity());
						dialog.setMessage("El archivo con ese nombre no existe!").setPositiveButton("OK", null);
						dialog.show();
					}
				};
			});
			
			return rootView;
		}

		@Override
		public void onLocationChanged(Location location) {	
			try {
				File f = new File(Environment.getExternalStorageDirectory()+"/Android/data/com.alvaroy.locationalvaroreyes");	
				f.mkdirs();
				f = new File(f.getAbsolutePath()+File.separator+name.getText().toString()+".txt");
				String separator = System.getProperty("line.separator");
				FileOutputStream fOut = new FileOutputStream(f, true);
				OutputStreamWriter osw = new OutputStreamWriter(fOut); 
				osw.append(String.valueOf(location.getLongitude())+","+String.valueOf(location.getLatitude())+","+String.valueOf(location.getAltitude()));
				osw.append(separator);
				osw.flush();
			    osw.close();
			    Log.i(TAG, "File wrote");
			} catch (FileNotFoundException e) {
				Log.i(TAG, "A");
			} catch (IOException e) {
				Log.i(TAG, "B");
			}
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
			boolean gps_enabled = true;
			mLocationManager = (LocationManager) rootView.getContext().getSystemService(Context.LOCATION_SERVICE);

			try {
				gps_enabled = mLocationManager
						.isProviderEnabled(LocationManager.GPS_PROVIDER);
			} catch (Exception ex) {			
			}
			
			Log.d(TAG, "gps_enabled " + gps_enabled);
			if (!gps_enabled ) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						getActivity());
				dialog.setMessage("GPS no esta habilitado!").setPositiveButton("OK", null);
				dialog.show();

			} else {
				mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
				Toast.makeText(getActivity(), "Comenzado", Toast.LENGTH_SHORT)
						.show();
				mStarted = !mStarted;
				mButton.setText("Detener");
			}
		}
		
		public void stopCapturing() {
			if (mLocationManager != null) {
				mLocationManager.removeUpdates(this);
			}
		}
		
    }
}
