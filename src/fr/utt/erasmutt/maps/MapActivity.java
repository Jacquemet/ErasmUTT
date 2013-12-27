package fr.utt.erasmutt.maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import fr.utt.erasmutt.Constants;
import fr.utt.erasmutt.LoginActivity;
import fr.utt.erasmutt.R;
import fr.utt.erasmutt.sqlite.DatabaseHelper;
import fr.utt.erasmutt.sqlite.model.Activities;

public class MapActivity extends Activity implements LocationListener{
        LatLng myPosition;
        GoogleMap map;
        Polyline line;
        Context context;
        String urlTopass;
        
        Bundle bundle;
        
        List<Activities> listActivityUser;
        
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_map);

                getActionBar().setDisplayHomeAsUpEnabled(true);
                
                bundle = getIntent().getExtras();
                
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
        //Location location = map.getMyLocation();
        context = this;
        if(location!=null){
         onLocationChanged(location);
         addActivitiesCloseToUser();
        
        }

        }
        public void addActivitiesCloseToUser() {
                DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                if(bundle.getBoolean("LoadAll")) {
                        listActivityUser= db.getAllActivities();
                        for(int i=0 ; i<listActivityUser.size() ; i++) {
                                String name =listActivityUser.get(i).getName();
                                LatLng position= new LatLng(Double.parseDouble(listActivityUser.get(i).getLatitude()),Double.parseDouble(listActivityUser.get(i).getLongitude()));
                                map.addMarker(new MarkerOptions().position(position).title(name).snippet(listActivityUser.get(i).getDesciptionActivity().substring(0, 15)+" ..."));
                        }
                } else {
                        Activities act = db.getActivitiesById(bundle.getInt("idActivity"));
                        String name = act.getName();
                        LatLng position= new LatLng(Double.parseDouble(act.getLatitude()),Double.parseDouble(act.getLongitude()));
                        map.addMarker(new MarkerOptions().position(position).title(name).snippet(act.getDesciptionActivity().substring(0, 15)+" ..."));
                        
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14));
                }
                

                map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                                AlertDialog.Builder boite;
         boite = new AlertDialog.Builder(context);
         boite.setTitle(R.string.searchRoute);
         boite.setMessage(getString(R.string.searchRouteDescription)+" "+ marker.getTitle());
         urlTopass = makeURL(myPosition.latitude,
                         myPosition.longitude, marker.getPosition().latitude,
                         marker.getPosition().longitude);
         boite.setPositiveButton(R.string.query_hint, new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int which) {
                                  new connectAsyncTask(urlTopass).execute();
         }
         });
         boite.setNegativeButton(R.string.cancel_label, new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int which) {
                                  dialog.dismiss();
         }
         });
         boite.show();
                        }
                });
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
        
        case R.id.action_help:
                
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Constants.urlHelp));
                
            if (i.resolveActivity(getPackageManager()) != null) {
                    startActivity(i);
            } else {
                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
            }
                
                return true;        
        case R.id.action_logout:
                DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                db.userLogout();
                String goodbyeMessage = String.format(getString(R.string.GoodBye), Constants.user.getFirstname());
                Constants.user.logout();
                Intent intentLogout = new Intent(getApplicationContext(),LoginActivity.class);
                intentLogout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                
                Toast.makeText(getApplicationContext(), goodbyeMessage, Toast.LENGTH_LONG).show();
                startActivity(intentLogout);
                
                return true;
        default:
            return super.onOptionsItemSelected(item);
         }
        }
        
        @Override
        public void onLocationChanged(Location location) {
        // Getting latitude of the current location
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        // Creating a LatLng object for the current location

         myPosition = new LatLng(latitude, longitude);
         
         map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 14));
        }

        @Override
        public void onProviderDisabled(String provider) {
                
        }

        @Override
        public void onProviderEnabled(String provider) {
                
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
                
        }
        
        private class connectAsyncTask extends AsyncTask<Void, Void, String> {
	         private ProgressDialog progressDialog;
	         String url;
	
	         connectAsyncTask(String urlPass) {
	         url = urlPass;
	         }
	
	         @Override
	         protected void onPreExecute() {
	         super.onPreExecute();
		         progressDialog = new ProgressDialog(context);
		         progressDialog.setMessage(getString(R.string.fetching_route));
		         progressDialog.setIndeterminate(true);
		         progressDialog.show();
	         }
	
	         @Override
	         protected String doInBackground(Void... params) {
		         JSONParser jParser = new JSONParser();
		         String json = jParser.getJSONFromUrl(url);
		         return json;
	         }
	
	         @Override
	         protected void onPostExecute(String result) {
		         super.onPostExecute(result);
		         progressDialog.dismiss();
		         if (result != null) {
		        	 drawPath(result);
		         }
		    }
        }
        public String makeURL(double sourcelat, double sourcelog, double destlat,
            double destlog) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString.append(Double.toString(sourcelog));
        urlString.append("&destination=");// to
        urlString.append(Double.toString(destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        return urlString.toString();
    }

    public class JSONParser {

        InputStream is = null;
        JSONObject jObj = null;
        String json = "";

        // constructor
        public JSONParser() {
        }

        public String getJSONFromUrl(String url) {

            // Making HTTP request
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                json = sb.toString();
                is.close();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            return json;

        }
    }

    public void drawPath(String result) {
        if (line != null) {
            map.clear();
            addActivitiesCloseToUser();
            
        }
        try {
            // Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes
                    .getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);

            for (int z = 0; z < list.size() - 1; z++) {
                LatLng src = list.get(z);
                LatLng dest = list.get(z + 1);
                line = map.addPolyline(new PolylineOptions()
                        .add(new LatLng(src.latitude, src.longitude),
                                new LatLng(dest.latitude, dest.longitude))
                        .width(5).color(Color.BLUE).geodesic(true));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
        
}

