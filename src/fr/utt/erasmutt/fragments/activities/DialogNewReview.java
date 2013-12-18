package fr.utt.erasmutt.fragments.activities;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import fr.utt.erasmutt.Constants;
import fr.utt.erasmutt.R;
import fr.utt.erasmutt.networkConnection.HttpCallback;
import fr.utt.erasmutt.networkConnection.HttpRequest;
import fr.utt.erasmutt.sqlite.DatabaseHelper;
import fr.utt.erasmutt.sqlite.model.Activities;
import fr.utt.erasmutt.sqlite.model.Review;

public class DialogNewReview extends Dialog {

	private EditText editTextResume;
	private EditText editTextComment;
	private RatingBar ratingReview;
	
	private int idActivity;
	private Review rv;
	private Button sendNewReview;
	
	private DatabaseHelper db;
	private HttpRequest requestAdd = null;
	private HttpRequest requestUpdate = null;
	
    public DialogNewReview(Context context, int idActitivty) {
		super(context);
		setContentView(R.layout.dialog_new_review);
		
		editTextResume = (EditText) findViewById(R.id.editTextResume);
		editTextComment = (EditText) findViewById(R.id.editTextComment);
		ratingReview = (RatingBar) findViewById(R.id.ratingBarReview);
		sendNewReview = (Button) findViewById(R.id.buttonSendNewReview);
		this.idActivity = idActitivty;
		db = new DatabaseHelper(getContext());
		
		sendNewReview.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				rv = new Review();
				rv.setTitle(editTextResume.getText().toString());
				rv.setDescription(editTextComment.getText().toString());
				rv.setMark(ratingReview.getRating());
				rv.setIdUser(Constants.user.getIdUser());
				rv.setIdActivity(idActivity);
				rv.setLanguage(Locale.getDefault().getDisplayLanguage());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
				rv.setDateTime(sdf.format(new Date()));
				
				addNewReview();
				
			}
		});
		
		setTitle("Avis de "+ Constants.user.getFirstname());
    }

	public int getIdActivity() {
		return idActivity;
	}

	public void setIdActivity(int idActivity) {
		this.idActivity = idActivity;
	}

	private void updateMarkActivity() {
		requestUpdate = new HttpRequest(new HttpCallback() {

			@Override
			public Object call(JSONObject jsonResponse) {
				
				Activities activity = db.getActivitiesById(idActivity);
				try {
					activity.setAverageMark(Float.valueOf(jsonResponse.get("mark").toString()));
					db.updateActivity(activity);
					
					Toast.makeText(getContext(), "Votre Avis a été publié !", Toast.LENGTH_SHORT).show();
					hide();
					
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				return null;
			}
		});

		// On vérifie que la connexion au réseau est valide
		Log.d("URL", Constants.urlRoot + "activiesManager.php?typeActivies=markUpdate&id="+idActivity+"&token=" + Constants.user.getToken());
		requestUpdate.execute(Constants.urlRoot + "activiesManager.php?typeActivies=markUpdate&id="+idActivity+"&token=" + Constants.user.getToken());

	}
	
	private void addNewReview() {
		requestAdd = new HttpRequest(new HttpCallback() {

			@Override
			public Object call(JSONObject jsonResponse) {
				try {
					rv.setIdReview(Integer.valueOf(jsonResponse.get("idReview").toString()));
					db.addReview(rv);
					updateMarkActivity();
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return null;
			}
		});

		// On vérifie que la connexion au réseau est valide
		requestAdd.execute(Constants.urlRoot + "reviewsManager.php?typeReviews=ajouter&token=" + Constants.user.getToken()+"&idUser="+Constants.user.getIdUser()+"&idActivity="+idActivity + "&title="+URLEncoder.encode(rv.getTitle())+"&description="+URLEncoder.encode(rv.getDescription())+"&mark="+rv.getMark()+"&date="+URLEncoder.encode(rv.getDateTime())+"&language="+rv.getLanguage());
	}
	
}
