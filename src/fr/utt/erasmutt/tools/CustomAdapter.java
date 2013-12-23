package fr.utt.erasmutt.tools;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import fr.utt.erasmutt.R;
import fr.utt.erasmutt.networkConnection.HttpCallbackByte;
import fr.utt.erasmutt.networkConnection.RetreiveImgTask;
import fr.utt.erasmutt.sqlite.DatabaseHelper;
import fr.utt.erasmutt.sqlite.model.Activities;

public class CustomAdapter extends  ArrayAdapter<Activities> {
	private List<Activities> listData;
	 
    private LayoutInflater layoutInflater;
    private DatabaseHelper db;
    private Activities newsItem;
    private ViewHolder holder;
	public CustomAdapter(Context context,
			List<Activities> listActivity) {
		 super(context, 0, listActivity);
		this.listData = listActivity;
		layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		db = new DatabaseHelper(getContext());
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
        newsItem = (Activities) listData.get(position);
 
        holder.name.setText(newsItem.getName());
        holder.ratingBar.setRating(newsItem.getAverageMark());
        if (holder.imageView != null && newsItem.getPictureActivity()==null) {
          new RetreiveImgTask(new HttpCallbackByte() {
				@Override
				public Object call(byte[] imgbyte) {
					Bitmap b = BitmapFactory.decodeByteArray( imgbyte,  0,imgbyte.length);
					holder.imageView.setImageBitmap(b);
            		newsItem.setPictureActivity(imgbyte);
					db.updateImageActivity(newsItem);	
					return null;
				}
			}).execute(newsItem.getPictureActivityString()); 	  
        }
        else if(newsItem.getPictureActivity()!=null){
        	Bitmap b = BitmapFactory.decodeByteArray( newsItem.getPictureActivity(),0,newsItem.getPictureActivity().length);
        	holder.imageView.setImageBitmap(b);
        }
 
        return convertView;
	}
    
 
    public static class ViewHolder {
        TextView name;
        RatingBar ratingBar;
        ImageView imageView;
    }

	
}
