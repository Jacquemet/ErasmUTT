package fr.utt.erasmutt;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Blob;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import fr.utt.erasmutt.networkConnection.HttpCallback;
import fr.utt.erasmutt.networkConnection.HttpCallbackByte;
import fr.utt.erasmutt.networkConnection.HttpRequest;
import fr.utt.erasmutt.sqlite.DatabaseHelper;
import fr.utt.erasmutt.sqlite.model.Activities;
import fr.utt.erasmutt.sqlite.model.Review;

public class LoginActivity extends Activity {

	TextView textViewLogin = null;
	TextView textViewPassword = null;

	EditText login = null;
	EditText password = null;

	Button buttonSignIn = null;
	Button buttonNewAccount = null;

	ProgressBar progBar = null;

	private HttpRequest request = null;
	private HttpRequest requestActivities = null;
	private HttpRequest requestReviews = null;
	
	private	DatabaseHelper db;
	
	private Activities afu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		getActionBar().hide();

		textViewLogin = (TextView) findViewById(R.id.textView_mail);
		textViewPassword = (TextView) findViewById(R.id.textView_password);
		login = (EditText) findViewById(R.id.editText_mail);
		password = (EditText) findViewById(R.id.editText_password);
		buttonSignIn = (Button) findViewById(R.id.button_signIn);
		buttonNewAccount = (Button) findViewById(R.id.button_createAccount);
		progBar = (ProgressBar) findViewById(R.id.progressBar);

        db = new DatabaseHelper(this);
        
		buttonSignIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				request = new HttpRequest(new HttpCallback() {
					
					@Override
					public Object call(JSONObject jsonResponse) {

						try {
							//TODO : Exploiter les données reçues
							if (!jsonResponse.getBoolean("error")) {
								Constants.user.setIdUser(Integer.parseInt(jsonResponse.getString("idUser")));
								Constants.user.setFirstname(jsonResponse.getString("firstname"));
								Constants.user.setToken(jsonResponse.getString("token"));
								Constants.user.setLastname(jsonResponse.getString("lastname"));
								Constants.user.setMail(jsonResponse.getString("mail"));
								
								//TODO : Vérifier que le User n'existe pas déjà 
								if(!db.isExistUser(Constants.user.getIdUser())) {
						             db.addUser(Constants.user);
					        	} else {
					        		db.updateUser(Constants.user);
					        	}
								
								loadActitivies();
								
							} else {
								Toast.makeText(getApplicationContext(), R.string.invalid_password, Toast.LENGTH_LONG).show();
								endLoading();
							}
						} catch (JSONException e) {
							Toast.makeText(getApplicationContext(), R.string.error_network, Toast.LENGTH_LONG).show();
							endLoading();
						}
						
						return null;
					}
				});
				
				//On vérifie que la connexion au réseau est valide
		        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
		        
		        if (activeInfo != null && activeInfo.isConnected()) {
		        	startLoading();
		        	final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		        	imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		        	request.execute(Constants.urlRoot+"connexion.php?typeConnexion=connecter&mail="+login.getText().toString()+"&password="+password.getText().toString());
		        }else {
		        	Toast.makeText(getApplicationContext(), R.string.network_disabled, Toast.LENGTH_LONG).show();
		        }
			}
		});

        // Action sur le bouton de création d'un nouveau compte
		buttonNewAccount.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), NewAccountActivity.class);
				startActivity(intent);
			}
		});
	}
	
	private void loadActitivies(){
		requestActivities = new HttpRequest(new HttpCallback() {
			
			@Override
			public Object call(JSONObject jsonResponse) {

				try {
					//TODO : Exploiter les données reçues
					if (!jsonResponse.getBoolean("error")) {

						JSONArray jObject = jsonResponse.getJSONArray("listActivities");

				        for (int i = 0; i < jObject.length(); i++) {

				        	JSONObject menuObject = jObject.getJSONObject(i);

				        	 afu= new Activities();
				             afu.setIdActivity(Integer.parseInt(menuObject.getString("idActivity")));
				             afu.setName(menuObject.getString("name"));
				             afu.setDesciptionActivity(menuObject.getString("desc"));
				             afu.setAverageMark(Float.parseFloat(menuObject.getString("averageMark")));
				             afu.setLatitude(menuObject.getString("latitude"));
				             afu.setLongitude(menuObject.getString("longitude"));
				             afu.setWebsite(menuObject.getString("website"));
				             afu.setFocusOn(Integer.parseInt(menuObject.getString("focusOn")));
				             afu.setAddress(menuObject.getString("address"));
				             afu.setPictureActivityString(menuObject.getString("picture"));
				             afu.setPictureActivity(null);
				            				           
				             afu.setAddress(menuObject.getString("address"));
				        	
				        	if(!db.isExistActivity(Integer.parseInt(menuObject.getString("idActivity")))) {
					             db.addActivity(afu);
				        	} else {
				        		db.updateActivity(afu);
				        	}
				            
				        }
						loadReviews();
					}
				} catch (JSONException e) {
					Toast.makeText(getApplicationContext(),"erreur parsage", Toast.LENGTH_LONG).show();
					endLoading();
				}
				
				return null;
			}
		});
		
		//On vérifie que la connexion au réseau est valide
    	requestActivities.execute(Constants.urlRoot+"activiesManager.php?typeActivies=lister&token="+Constants.user.getToken());
	}

	private void loadReviews() {
		requestReviews = new HttpRequest(new HttpCallback() {

			@Override
			public Object call(JSONObject jsonResponse) {

				try {

					JSONArray jObject = jsonResponse.getJSONArray("listReviews");

					for (int i = 0; i < jObject.length(); i++) {

						JSONObject menuObject = jObject.getJSONObject(i);

						Review rv = new Review();
						rv.setIdReview(Integer.parseInt(menuObject.getString("idReview")));
						rv.setIdUser(Integer.parseInt(menuObject.getString("idUser")));
						rv.setIdActivity(Integer.parseInt(menuObject.getString("idActivity")));
						rv.setTitle(menuObject.getString("title"));
						rv.setDescription(menuObject.getString("description"));
						rv.setMark(Float.parseFloat(menuObject.getString("mark")));
						rv.setDateTime(menuObject.getString("date"));
						rv.setLanguage(menuObject.getString("language"));

						if (!db.isExistReview(Integer.parseInt(menuObject.getString("idReview")))) {
							db.addReview(rv);
						} else {
							db.updateReview(rv);
						}
						
					}
					
					launchHome();

				} catch (JSONException e) {
					Toast.makeText(getApplicationContext(),"Erreur parsage sur les avis", Toast.LENGTH_LONG).show();
					endLoading();
				}

				return null;
			}
		});

		// On vérifie que la connexion au réseau est valide
		requestReviews.execute(Constants.urlRoot + "reviewsManager.php?typeReviews=lister&token=" + Constants.user.getToken());

	}

	private void startLoading() {
		progBar.setVisibility(View.VISIBLE);

		login.setVisibility(View.INVISIBLE);
		password.setVisibility(View.INVISIBLE);
		buttonSignIn.setVisibility(View.INVISIBLE);
		buttonNewAccount.setVisibility(View.INVISIBLE);
		textViewLogin.setVisibility(View.INVISIBLE);
		textViewPassword.setVisibility(View.INVISIBLE);
	}

	private void endLoading() {
		progBar.setVisibility(View.INVISIBLE);

		login.setVisibility(View.VISIBLE);
		password.setVisibility(View.VISIBLE);
		buttonSignIn.setVisibility(View.VISIBLE);
		buttonNewAccount.setVisibility(View.VISIBLE);
		textViewLogin.setVisibility(View.VISIBLE);
		textViewPassword.setVisibility(View.VISIBLE);

	}
	
	private void launchHome() {
		// Create an Intent which clear the back stack
		// and launch the home activity
		Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

		// Toast qui confirme la connexion
		Resources res = getResources();
		String helloMessage = String.format(res.getString(R.string.hello),Constants.user.getFirstname());
		Toast.makeText(getApplicationContext(), helloMessage, Toast.LENGTH_LONG).show();
		endLoading();
		// Start the activity
		startActivity(intent);
	}
}
