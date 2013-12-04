package fr.utt.erasmutt.maps;

import java.util.List;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import fr.utt.erasmutt.Constants;
import fr.utt.erasmutt.R;
import fr.utt.erasmutt.sqlite.DatabaseHelper;
import fr.utt.erasmutt.sqlite.model.Activities;

public class MapActivity extends Activity implements LocationListener{
	LatLng myPosition;
	LatLng positionResto1;
	GoogleMap map;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		
        // Getting GoogleMap object from the fragment
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        // Enabling MyLocation Layer of Google Map
		map.setMyLocationEnabled(true);

        // Getting LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);

        if(location!=null){
	        // Getting latitude of the current location
	        double latitude = location.getLatitude();
	
	        // Getting longitude of the current location
	        double longitude = location.getLongitude();
	
	        // Creating a LatLng object for the current location
	
	         myPosition = new LatLng(latitude, longitude);
	         
	         map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 13));
	         
	         addActivitiesCloseToUser();
	         
        }

	}
	public void addActivitiesCloseToUser(){
		DatabaseHelper db = new DatabaseHelper(getApplicationContext());
		List<Activities> listActivityUser= db.getAllActivities();
		for(int i=0 ; i<listActivityUser.size() ; i++) { 
			String name =listActivityUser.get(i).getName();
			LatLng position= new LatLng(Double.parseDouble(listActivityUser.get(i).getLatitude()),Double.parseDouble(Constants.tabActivityForUser.get(i).getLongitude()));
			map.addMarker(new MarkerOptions().position(position).title(name));
		}
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	


	
}
