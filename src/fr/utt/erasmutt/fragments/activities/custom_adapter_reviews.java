package fr.utt.erasmutt.fragments.activities;

import java.util.List;

import android.content.Context;
import android.util.Log;
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

public class custom_adapter_reviews extends  ArrayAdapter<Review>{
	private List<Review> listData;
    private LayoutInflater layoutInflater;
    private DatabaseHelper db;
	
	public custom_adapter_reviews(Context context,
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
            convertView = layoutInflater.inflate(R.layout.custom_list_reviews, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.titleReview);
            holder.desc = (TextView) convertView.findViewById(R.id.descReview);
            holder.name = (TextView) convertView.findViewById(R.id.nameUserReview);
            holder.date = (TextView) convertView.findViewById(R.id.dateReview);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBarReview);
            holder.imageView = (ImageView) convertView.findViewById(R.id.thumbImageUser);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Review newsItem = (Review) listData.get(position);
 
        holder.title.setText(newsItem.getTitle());
        holder.desc.setText(newsItem.getDescription());
        holder.date.setText(newsItem.getDateTime());
        holder.name.setText(db.getUserById(newsItem.getIdUser()).getFirstname());
        holder.ratingBar.setRating(newsItem.getMark());
        
       if (holder.imageView != null) {
            new ImageDownloaderTaskList(holder.imageView).execute(db.getUserById(newsItem.getIdUser()).getPictureString());
        }
 
        return convertView;
	}
    
 
    public static class ViewHolder {
        TextView title;
        TextView desc;
        TextView name;
        TextView date;        
        RatingBar ratingBar;
        ImageView imageView;
    }
}
