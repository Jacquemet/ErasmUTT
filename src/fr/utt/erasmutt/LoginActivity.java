package fr.utt.erasmutt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import fr.utt.erasmutt.networkConnection.HttpCallback;
import fr.utt.erasmutt.networkConnection.HttpRequest;
import fr.utt.erasmutt.sqlite.DatabaseHelper;
import fr.utt.erasmutt.sqlite.model.Activities;
import fr.utt.erasmutt.sqlite.model.Review;
import fr.utt.erasmutt.sqlite.model.User;
/**
 * This activity it's the first activity that this application show
 * We can log in or create account on this activity 
 * @author Thibault Jacquemet & Kévin Larue
 *
 */
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
		// Put fullscreen mode
		getActionBar().hide();

		//Instantiation of all the graphic elements
		textViewLogin = (TextView) findViewById(R.id.textView_mail);
		textViewPassword = (TextView) findViewById(R.id.textView_password);
		login = (EditText) findViewById(R.id.editText_mail);
		password = (EditText) findViewById(R.id.editText_password);
		buttonSignIn = (Button) findViewById(R.id.button_signIn);
		buttonNewAccount = (Button) findViewById(R.id.button_createAccount);
		progBar = (ProgressBar) findViewById(R.id.progressBar);
		
		//Create the DB Helper
        db = new DatabaseHelper(this);
        
		buttonSignIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				request = new HttpRequest(new HttpCallback() {
					
					@Override
					public Object call(JSONObject jsonResponse) {

						try {
							//If the connexion is successful
							if (!jsonResponse.getBoolean("error")) {
								//Setup the current user in a Constant
								Constants.user.setIdUser(Integer.parseInt(jsonResponse.getString("idUser")));
								Constants.user.setFirstname(jsonResponse.getString("firstname"));
								Constants.user.setToken(jsonResponse.getString("token"));
								Constants.user.setLastname(jsonResponse.getString("lastname"));
								Constants.user.setMail(jsonResponse.getString("mail"));
								Constants.user.setPictureString(jsonResponse.getString("pictureUser"));
								
								//We create the user in the local Db if not already the case 
								if(!db.isExistUser(Constants.user.getIdUser())) {
						             db.addUser(Constants.user);
					        	} else {
					        		db.updateUser(Constants.user);
					        	}
								
								JSONArray jObject = jsonResponse.getJSONArray("listUser");
						        for (int i = 0; i < jObject.length(); i++) {
						        	JSONObject menuObject = jObject.getJSONObject(i);
						        	User u = new User();
						        	u.setIdUser(Integer.parseInt(menuObject.getString("idUser")));
						        	u.setFirstname(menuObject.getString("firstname"));
						        	u.setLastname(menuObject.getString("lastname"));
						        	u.setMail(menuObject.getString("mail"));
						        	u.setPictureString(menuObject.getString("pictureUser"));
						        	
						        	if(!db.isExistUser(u.getIdUser())) {
							             db.addUser(u);
						        	} else {
						        		db.updateUser(u);
						        	}
						        }
						        
						        //Load the Activities stored on the server in the local DB
								loadActitivies();
								
							} else {
								Toast.makeText(getApplicationContext(), jsonResponse.getString("message"), Toast.LENGTH_LONG).show();
								endLoading();
							}
						} catch (JSONException e) {
							Toast.makeText(getApplicationContext(), R.string.error_network, Toast.LENGTH_LONG).show();
							endLoading();
						}
						
						return null;
					}
				});
				
				//We check if there a valid network connexion (WIFI or 3G)
		        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
		        
		        //If it is we execute the request
		        if (activeInfo != null && activeInfo.isConnected()) {
		        	startLoading();
		        	final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		        	imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		        	//Execute the first request which try to connect the user with the login/password filled
		        	request.execute(Constants.urlRoot+"connexion.php?typeConnexion=connecter&mail="+login.getText().toString()+"&password="+password.getText().toString());
		        }
		        // Else we show an error with a Toast
		        else {
		        	Toast.makeText(getApplicationContext(), R.string.network_disabled, Toast.LENGTH_LONG).show();
		        }
			}
		});

        // New account Action
		buttonNewAccount.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), NewAccountActivity.class);
				startActivity(intent);
			}
		});
	}
	
	/**
	 * Load the Activities stored on the server in the local DB
	 */
	private void loadActitivies(){
		requestActivities = new HttpRequest(new HttpCallback() {
			
			@Override
			public Object call(JSONObject jsonResponse) {

				try {
					//If the request is successful
					if (!jsonResponse.getBoolean("error")) {

						JSONArray jObject = jsonResponse.getJSONArray("listActivities");
						
						//For each reviews we create a Object Activities and we add or update it into the DB
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
				        
				        //Load the Reviews stored on the server in the local DB
						loadReviews();
					}
				} catch (JSONException e) {
					Toast.makeText(getApplicationContext(),"erreur parsage", Toast.LENGTH_LONG).show();
					endLoading();
				}
				
				return null;
			}
		});
		
    	requestActivities.execute(Constants.urlRoot+"activiesManager.php?typeActivies=lister&token="+Constants.user.getToken());
	}

	/**
	 * Load the Reviews stored on the server in the local DB
	 */
	private void loadReviews() {
		requestReviews = new HttpRequest(new HttpCallback() {

			@Override
			public Object call(JSONObject jsonResponse) {

				try {

					JSONArray jObject = jsonResponse.getJSONArray("listReviews");
					
					//For each reviews we create a Object Review and we add or update it into the DB
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
					
					//When the DB is filled with the required data we launch the next Activity
					launchHome();

				} catch (JSONException e) {
					Toast.makeText(getApplicationContext(),"Erreur parsage sur les avis", Toast.LENGTH_LONG).show();
					endLoading();
				}

				return null;
			}
		});

		requestReviews.execute(Constants.urlRoot + "reviewsManager.php?typeReviews=lister&token=" + Constants.user.getToken());

	}

	
	/**
	 * Show the progressBar and hide the other elements
	 * Called when we launch a connexion with the server
	 */
	private void startLoading() {
		progBar.setVisibility(View.VISIBLE);

		login.setVisibility(View.INVISIBLE);
		password.setVisibility(View.INVISIBLE);
		buttonSignIn.setVisibility(View.INVISIBLE);
		buttonNewAccount.setVisibility(View.INVISIBLE);
		textViewLogin.setVisibility(View.INVISIBLE);
		textViewPassword.setVisibility(View.INVISIBLE);
	}

	/**
	 * Hide the progressBar and show the other elements
	 * Called when the connexion with the server is succeed or failed
	 */
	private void endLoading() {
		progBar.setVisibility(View.INVISIBLE);

		login.setVisibility(View.VISIBLE);
		password.setVisibility(View.VISIBLE);
		buttonSignIn.setVisibility(View.VISIBLE);
		buttonNewAccount.setVisibility(View.VISIBLE);
		textViewLogin.setVisibility(View.VISIBLE);
		textViewPassword.setVisibility(View.VISIBLE);

	}
	
	//Method which launch the HomeActivity and clear the Back Satck
	private void launchHome() {
		// Create an Intent which clear the back stack and launch the home activity
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
