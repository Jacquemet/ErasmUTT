package fr.utt.erasmutt.fragments.activities;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import fr.utt.erasmutt.HomeActivity;
import fr.utt.erasmutt.OnHeadlineSelectedListener;
import fr.utt.erasmutt.R;
import fr.utt.erasmutt.sqlite.DatabaseHelper;
import fr.utt.erasmutt.sqlite.model.Activities;
import fr.utt.erasmutt.tools.CustomAdapter;

public class ListActivityFragment extends ListFragment {

	OnHeadlineSelectedListener mCallback;

	// This is the Adapter being used to display the list's data
	SimpleAdapter mAdapter;
	CustomAdapter ca;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//TODO :  Bug à résoudre dans un affcihage tablette
		Bundle bundle = getArguments();
		List<Activities> listActivity;
		
		DatabaseHelper db = new DatabaseHelper(getActivity());
		if(bundle != null)
			listActivity = db.getSearchableActivities(bundle.getString("query").toString());
		else 
			listActivity = db.getSearchableActivities("t");
	
		
		if (listActivity.size() == 0) {
			AlertDialog.Builder boite;
			boite = new AlertDialog.Builder(getActivity());
			boite.setTitle(R.string.searchEmpty);
			boite.setMessage(R.string.searchEmpty);
			boite.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(getActivity(),
									HomeActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
						}
					});
			boite.show();
		} else {
					
			//ListView lv1 = (ListView)getActivity().getLayoutInflater().inflate(R.layout.fragment_list_activity, null).findViewById(R.id.custom_list);
			
			ca =  new CustomAdapter(getActivity().getLayoutInflater().getContext(), listActivity);
	        setListAdapter(ca);
		}

	}

	@Override
	public void onStart() {
		super.onStart();

		// When in two-pane layout, set the listview to highlight the selected
		// list item
		// (We do this during onStart because at the point the listview is
		// available.)
		if (getFragmentManager().findFragmentById(R.id.list_activity_frag) != null) {
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception.
		try {
			mCallback = (OnHeadlineSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()+ " must implement OnHeadlineSelectedListener");
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		//Title ActionBar update with the query
		Bundle bundle = getArguments();
		if(bundle != null)
			getActivity().getActionBar().setTitle(getResources().getString(R.string.title_activity_activity_handler) +" "+ bundle.getString("query").toString());
		else
			getActivity().getActionBar().setTitle(getResources().getString(R.string.title_activity_activity_handler) +" "+ "t");

	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		mCallback.onArticleSelected(ca.getItem(position).getIdActivity());
		getListView().setItemChecked(position, true);
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();

	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(inflater.inflate(R.layout.dialog_new_review, null))
	    // Add action buttons
	           .setPositiveButton(R.string.action_help, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	                   // sign in the user ...
	               }
	           });      
	    return builder.create();
	}
	
	
}
