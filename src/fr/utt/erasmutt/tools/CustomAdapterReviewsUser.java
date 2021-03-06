package fr.utt.erasmutt.tools;

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
import fr.utt.erasmutt.sqlite.DatabaseHelper;
import fr.utt.erasmutt.sqlite.model.Review;

public class CustomAdapterReviewsUser extends  ArrayAdapter<Review>{
	private List<Review> listData;
    private LayoutInflater layoutInflater;
    private DatabaseHelper db;
    
    public CustomAdapterReviewsUser(Context context,
			List<Review> listReviews) {
		 super(context, 0, listReviews);
		this.listData = listReviews;
		db = new DatabaseHelper(getContext());
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
            convertView = layoutInflater.inflate(R.layout.custom_list_reviews_user, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.nameReviewUser);
            holder.desc = (TextView) convertView.findViewById(R.id.descReviewUser);
            holder.nameActivity = (TextView) convertView.findViewById(R.id.nameActivity);
            holder.date = (TextView) convertView.findViewById(R.id.dateReviewUser);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBarReviewUser);
            holder.imageView = (ImageView) convertView.findViewById(R.id.thumbImageActivity);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Review newsItem = (Review) listData.get(position);
 
        holder.title.setText(newsItem.getTitle());
        holder.desc.setText(newsItem.getDescription());
        holder.date.setText(newsItem.getDateTime());
        holder.nameActivity.setText(db.getActivitiesById(newsItem.getIdActivity()).getName());
        holder.ratingBar.setRating(newsItem.getMark());
        
       if (holder.imageView != null) {
            new ImageDownloaderTaskList(holder.imageView).execute(db.getActivitiesById(newsItem.getIdActivity()).getPictureActivityString());
        }
 
        return convertView;
	}
    
 
    public static class ViewHolder {
        TextView title;
        TextView desc;
        TextView nameActivity;
        TextView date;        
        RatingBar ratingBar;
        ImageView imageView;
    }
}

