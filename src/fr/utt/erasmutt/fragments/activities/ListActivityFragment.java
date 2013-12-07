package fr.utt.erasmutt.fragments.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import fr.utt.erasmutt.HomeActivity;
import fr.utt.erasmutt.R;
import fr.utt.erasmutt.sqlite.DatabaseHelper;
import fr.utt.erasmutt.sqlite.model.Activities;

public class ListActivityFragment extends ListFragment {

	OnHeadlineSelectedListener mCallback;

	// This is the Adapter being used to display the list's data
	SimpleAdapter mAdapter;

	// The container Activity must implement this interface so the frag can
	// deliver messages
	public interface OnHeadlineSelectedListener {
		/** Called by HeadlinesFragment when a list item is selected */
		public void onArticleSelected(int position);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle = getArguments();
		List<Activities> listActivity;
		
		DatabaseHelper db = new DatabaseHelper(getActivity());
		listActivity = db.getSearchableActivities(bundle.getString("query").toString());
		
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
			
			// Each row in the list stores id activity and name_acticity
			List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

			for (int i = 0; i < listActivity.size(); i++) {
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("idActivity",String.valueOf(listActivity.get(i).getIdActivity()));
				hm.put("nameActivity", listActivity.get(i).getName());
				aList.add(hm);
			}

			// For the simple adapter, specify which columns go into which views
			String[] fromColumns = {"idActivity","nameActivity"};
			int[] toViews = { R.id.idActivities, R.id.nameListActivities }; 

			// Create an adapter with the require parameters 
			mAdapter = new SimpleAdapter(getActivity(), aList, R.layout.fragment_list_activity, fromColumns, toViews);

			// Create an array adapter for the list view
			setListAdapter(mAdapter);

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
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		//Title ActionBar update with the query
		Bundle bundle = getArguments();
		getActivity().getActionBar().setTitle(getResources().getString(R.string.title_activity_activity_handler) +" "+ bundle.getString("query").toString());

	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		//Get the id of the selected Element
		TextView tv = (TextView) v.findViewById(R.id.idActivities);
		
		// Notify the parent activity of selected item
		mCallback.onArticleSelected(Integer.parseInt(tv.getText().toString()));
		
		// Set the item as checked to be highlighted when in two-pane layout
		getListView().setItemChecked(position, true);
	}

}
