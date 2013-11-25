package fr.utt.erasmutt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


public class Webservice {
	private String jsonResult;
	private Activity ha=null;
	
	// Async Task to access the web
		 private class JsonReadTask extends AsyncTask<String, Void, String> {
		  @Override
		  protected String doInBackground(String... params) {
		   HttpClient httpclient = new DefaultHttpClient();
		   HttpPost httppost = new HttpPost(params[0]);
		   try {
		    HttpResponse response = httpclient.execute(httppost);
		    jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
		   }
		 
		   catch (ClientProtocolException e) {
		    e.printStackTrace();
		   } catch (IOException e) {
		    e.printStackTrace();
		   }
		   return null;
		  }
		 
		  private StringBuilder inputStreamToString(InputStream is) {
		   String rLine = "";
		   StringBuilder answer = new StringBuilder();
		   BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		 
		   try {
		    while ((rLine = rd.readLine()) != null) {
		     answer.append(rLine);
		    }
		   }
		 
		   catch (IOException e) {
		    // e.printStackTrace();
		    Toast.makeText(ha.getApplicationContext(),
		      "Error..." + e.toString(), Toast.LENGTH_LONG).show();
		   }
		   return answer;
		  }
		 
		  @Override
		  protected void onPostExecute(String result) {
			 if(ha.getLocalClassName().toString().equalsIgnoreCase("HomeActivity")){
				 HomeActivity activityAct = (HomeActivity) ha;  
				 activityAct.ListDrwaer(jsonResult);
			 }
				 
		  }
		 }// end async task
		 
		 public void accessWebService(String url,Activity homa) {
		  ha=homa;
		  JsonReadTask task = new JsonReadTask();
		  // passes values for the urls string array
		  task.execute(new String[] { url });
		 }
		 
		 
}
