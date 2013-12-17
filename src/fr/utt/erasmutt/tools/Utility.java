package fr.utt.erasmutt.tools;

import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utility {
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
        Log.v("Utility", String.valueOf(listAdapter.getCount()));
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            Log.v("getMeasuredHeight", String.valueOf(listItem.getMeasuredHeight()));
            if(totalHeight<(listItem.getMeasuredHeight()/10)){
            	totalHeight = (listItem.getMeasuredHeight()/10);
            }
            Log.v("totalHeight", String.valueOf(totalHeight));
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + 20;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
