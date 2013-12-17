package fr.utt.erasmutt.fragments.activities;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import fr.utt.erasmutt.R;
import fr.utt.erasmutt.networkConnection.ImageDownloaderTaskList;
import fr.utt.erasmutt.sqlite.model.Activities;

public class custom_adapter extends  ArrayAdapter<Activities> {
	private List<Activities> listData;
	 
    private LayoutInflater layoutInflater;

	public custom_adapter(Context context,
			List<Activities> listActivity) {
		 super(context, 0, listActivity);
		this.listData = listActivity;
		
		layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

 
    @Override
    public int getCount() {
        return listData.size();
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 

    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.custom_adapter, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.title);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.averageRatingBarActivitySearch);
            holder.imageView = (ImageView) convertView.findViewById(R.id.thumbImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Activities newsItem = (Activities) listData.get(position);
 
        holder.name.setText(newsItem.getName());
        holder.ratingBar.setRating(newsItem.getAverageMark());
        if (holder.imageView != null) {
            new ImageDownloaderTaskList(holder.imageView).execute(newsItem.getPictureActivityString());
        }
 
        return convertView;
	}
    
 
    public static class ViewHolder {
        TextView name;
        RatingBar ratingBar;
        ImageView imageView;
    }

	
}
