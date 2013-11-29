package fr.utt.erasmutt;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import fr.utt.erasmutt.networkConnection.HttpCallback;
import fr.utt.erasmutt.networkConnection.HttpRequest;

public class LoginActivity extends Activity {

	TextView textViewLogin = null;
	TextView textViewPassword = null;

	EditText login = null;
	EditText password = null;

	Button buttonSignIn = null;
	Button buttonNewAccount = null;

	ProgressBar progBar = null;

	private HttpRequest request = null;
	
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

        	
		buttonSignIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				request = new HttpRequest(new HttpCallback() {
					
					@Override
					public Object call(JSONObject jsonResponse) {

						try {
							//TODO : Exploiter les données reçues
							if (!jsonResponse.getBoolean("error")) {
								
								Constants.user.setFirstname(jsonResponse.getString("firstname"));
								Constants.user.setToken(jsonResponse.getString("token"));
								Constants.user.setLastname(jsonResponse.getString("lastname"));
								Constants.user.setMail(jsonResponse.getString("mail"));
								Constants.user.setMessage(jsonResponse.getString("message"));
								Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
								
								startActivity(intent);
								
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

}
