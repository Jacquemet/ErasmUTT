package fr.utt.erasmutt.networkConnection;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.os.AsyncTask;
import android.util.Log;

public class RetreiveImgTask extends AsyncTask<String, Void, byte[]> {

    private Exception exception;

    HttpCallbackByte callback;

    public RetreiveImgTask(HttpCallbackByte Callback) {
        callback = Callback;	        
    }
    
	protected byte[] doInBackground(String... params) {
        try {
            return getLogoImage(params[0]);
        } catch (Exception e) {
            this.exception = e;
            Log.e("Error in RetreiveImgTask", e.getMessage());
            return null;
        }
    }

    protected void onPostExecute(byte[] tabImg) {
    	this.callback.call(tabImg);
    }
    
    public byte[] getLogoImage(String url){
	     try {
	             URL imageUrl = new URL(url);
	             URLConnection ucon = imageUrl.openConnection();
	             InputStream is = ucon.getInputStream();
	             BufferedInputStream bis = new BufferedInputStream(is);
	             ByteArrayBuffer baf = new ByteArrayBuffer(500);
	             int current = 0;
	             while ((current = bis.read()) != -1) {
	                     baf.append((byte) current);
	             }
	             Log.v("getLogoImage Fin de url : ",url);
	             return baf.toByteArray();
	     } catch (Exception e) {
	             Log.v("ImageManager", "Error: " + e.toString());
	     }
	     return null;
	}
    
}