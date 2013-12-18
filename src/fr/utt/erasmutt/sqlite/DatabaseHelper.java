package fr.utt.erasmutt.sqlite;

import java.util.LinkedList;
import java.util.List;

import fr.utt.erasmutt.Constants;
import fr.utt.erasmutt.sqlite.model.Activities;
import fr.utt.erasmutt.sqlite.model.Review;
import fr.utt.erasmutt.sqlite.model.User;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	 // Logcat tag
    private static final String LOG = "DatabaseHelper";
 
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "erasmutt";
    
	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	
	public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION); 
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		// SQL statement to create book table
		dropTables(db);
        String CREATE_ACTIVITIES_TABLE = "CREATE TABLE activities ( " +
                "idActivity INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, "+
                "descriptionActivity TEXT, "+
                "pictureActivity BLOB, "+
                "averageMark REAL, "+
                "longitude TEXT, "+
                "latitude TEXT, "+
                "website TEXT, "+
                "focusOn INTEGER,"+
                "address TEXT, "+
                "pictureActivityString TEXT)";
       
        String CREATE_GROUPS_TABLE="CREATE TABLE groups("+
        		  "idGroup INTEGER PRIMARY KEY AUTOINCREMENT,"+
        		  "name TEXT,"+
        		  "descriptionGroup TEXT,"+
        		  "pictureGroup TEXT)";
        
        String CREATE_ACTIVITIES_GROUPS_TABLE =  "CREATE TABLE activitiesgroups ("+
        		"idActivity INTEGER," +
        		"idGroup INTEGER,"+
        		"FOREIGN KEY (idActivity) REFERENCES activities(idActivity),"+
       		    "FOREIGN KEY (idGroup) REFERENCES groups(idGroup))";
        
        
        
        String CREATE_MESSAGES_TABLE = "CREATE TABLE messages ("+
				"idMessage INTEGER PRIMARY KEY AUTOINCREMENT,"+
				"idUserSender INTEGER,"+
				"idUserReceiver INTEGER,"+
				"message TEXT,"+
				"date TEXT,"+
				"read TEXT,"+
				"FOREIGN KEY (idUserSender) REFERENCES users(idUser),"+
       		    "FOREIGN KEY (idUserReceiver) REFERENCES users(idUser))";
        
        String CREATE_REVIEW_TABLE ="CREATE TABLE review ("+
        		"idReview INTEGER PRIMARY KEY AUTOINCREMENT,"+
        		"idUser INTEGER,"+
        		"idActivity INTEGER,"+
        		"title TEXT,"+
        		"description TEXT,"+
        		"mark REAL,"+
        		"date TEXT,"+
        		"language TEXT)";
        
        String CREATE_USERS_TABLE = "CREATE TABLE users ("+
        		  "idUser INTEGER PRIMARY KEY AUTOINCREMENT,"+
        		  "firstname TEXT,"+
        		  "lastname TEXT,"+
        		  "password TEXT,"+
        		  "pictureUserString TEXT,"+
        		  "mail TEXT,"+
        		  "token TEXT," +
        		  "pictureUser BLOB)";
        
        String CREATE_USERSGROUPS_TABLE ="CREATE TABLE usersgroups("+
        		  "idUser INTEGER,"+
        		  "idGroup INTEGER,"+
        		  "FOREIGN KEY (idUser) REFERENCES users(idUser),"+
        		  "FOREIGN KEY (idGroup) REFERENCES groups(idGroup))";
        	
        
        db.execSQL(CREATE_ACTIVITIES_TABLE);
        Log.v("dbtable", "CREATE_ACTIVITIES_TABLE");
        db.execSQL(CREATE_GROUPS_TABLE);
        Log.v("dbtable", "CREATE_GROUPS_TABLE");
        db.execSQL(CREATE_USERS_TABLE);
        Log.v("dbtable", "CREATE_USERS_TABLE");
        db.execSQL(CREATE_MESSAGES_TABLE);
        Log.v("dbtable", "CREATE_MESSAGES_TABLE");
        db.execSQL(CREATE_REVIEW_TABLE);
        Log.v("dbtable", "CREATE_REVIEW_TABLE");
        db.execSQL(CREATE_ACTIVITIES_GROUPS_TABLE);
        Log.v("dbtable", "CREATE_ACTIVITIES_GROUPS_TABLE");
        db.execSQL(CREATE_USERSGROUPS_TABLE);
        Log.v("dbtable", "CREATE_USERSGROUPS_TABLE");
        
      
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// Drop older books table if existed
		dropTables(db);
 
        // create fresh books table
        this.onCreate(db);
	}
	
	
	public void dropTables(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS activities");
        db.execSQL("DROP TABLE IF EXISTS activitiesgroups");
        db.execSQL("DROP TABLE IF EXISTS groups");
        db.execSQL("DROP TABLE IF EXISTS messages");
        db.execSQL("DROP TABLE IF EXISTS review");
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS usersgroups");
		
	}
	 //---------------------------------------------------------------------
	 
    /**
     * CRUD operations (create "add", read "get", update, delete) user + getUser
     */

	
	public void addUser(User user){
        Log.d("user", user.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("idUser", user.getIdUser());
        values.put("firstname", user.getFirstname()); 
        values.put("lastname", user.getLastname()); 
        values.put("mail", user.getMail()); 
        values.put("pictureUserString", user.getPictureString());
        values.put("token", user.getToken()); 
        values.put("pictureUser", user.getPicture());
        
        // 3. insert
        db.insert("users", // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
 
        // 4. close
        db.close();
    }
	
	 public void updateUser(User user){
	        // 1. get reference to writable DB
	        SQLiteDatabase db = this.getWritableDatabase();
	 
	        // 2. create ContentValues to add key "column"/value
	        ContentValues values = new ContentValues();
	        values.put("idUser", user.getIdUser());
	        values.put("firstname", user.getFirstname()); 
	        values.put("lastname", user.getLastname()); 
	        values.put("mail", user.getMail()); 
	        values.put("token", user.getToken());
	        values.put("pictureUserString", user.getPictureString());
	        values.put("pictureUser", user.getPicture());
	 
	        // 3. insert
	        db.update("users", values, "idUser = ? ", new String[] {String.valueOf(user.getIdUser())});
	 
	        // 4. close
	        db.close();
    }

	 public void updateImageUser(User user){
	        // 1. get reference to writable DB
	        SQLiteDatabase db = this.getWritableDatabase();
	        ContentValues values = new ContentValues();
	       
	        values.put("pictureUser", user.getPicture());
	        // 3. insert
	        db.update("users", values, "idUser = ? ", new String[] {String.valueOf(user.getIdUser())});
	 
	        // 4. close
	        db.close();
	    }
	 
	 public User getUserById(int id){
	        // 1. get reference to writable DB
	        SQLiteDatabase db = this.getReadableDatabase();
	 
	        // 1. build the query
	        String query = "SELECT * FROM users where idUser = " + id;
	        // 2. get reference to writable DB
	        Cursor cursor = db.rawQuery(query, null);
	 
	        // 3. go over each row, build book and add it to list
	        User user = null;
	        if (cursor.moveToFirst()) {
	            do {
	            	user = new User();
	            	user.setIdUser(Integer.parseInt(cursor.getString(0)));
	            	user.setFirstname(cursor.getString(1));
	                user.setLastname(cursor.getString(2));
	                user.setPictureString(cursor.getString(4));
	                user.setMail(cursor.getString(5));
	                user.setPicture(cursor.getBlob(6));
	                
	            } while (cursor.moveToNext());
	        }

	        return user;
 }
	 
	 public Boolean isExistUser(int idUser){
		 
	        // 1. get reference to readable DB
	        SQLiteDatabase db = this.getReadableDatabase();
	 
	        // 1. build the query
	        String query = "SELECT * FROM users where idUser = " + idUser;
	 
	        // 2. get reference to writable DB
	        Cursor cursor = db.rawQuery(query, null);
	 
	        // 3. if we got results get the first one
	        if (cursor != null && cursor.getCount()!=0) {
	        	db.close();
	        	 return true;
	        }
	        else {
	        	db.close();
	        	return false;
	        }
	 
	    }
	 public void userLogout(){
		 SQLiteDatabase db = this.getWritableDatabase();
		 
	        // 2. create ContentValues to add key "column"/value
	        ContentValues values = new ContentValues();
	        values.put("token", "");
	 
	        // 3. insert
	        db.update("users", values, "idUser = ? ", new String[] {String.valueOf(Constants.user.getIdUser())});
	 
	        // 4. close
	        db.close();
		 
	 }
	 //---------------------------------------------------------------------
	 
	    /**
	     * CRUD operations (create "add", read "get", update, delete) activities
	     */

	 	public void addActivity(Activities activity){
	        // 1. get reference to writable DB
	        SQLiteDatabase db = this.getWritableDatabase();
	 
	        // 2. create ContentValues to add key "column"/value
	        ContentValues values = new ContentValues();
	        values.put("name", activity.getName()); 
	        values.put("descriptionActivity", activity.getDesciptionActivity()); 
	        values.put("pictureActivity", activity.getPictureActivity()); 
	        values.put("averageMark", activity.getAverageMark()); 
	        values.put("longitude", activity.getLongitude()); 
	        values.put("latitude", activity.getLatitude()); 
	        values.put("website", activity.getWebsite()); 
	        values.put("focusOn", activity.getFocusOn()); 
	        values.put("address", activity.getAddress()); 
	        values.put("pictureActivityString", activity.getPictureActivityString());
	        // 3. insert
	        db.insert("activities", // table
	                null, //nullColumnHack
	                values); // key/value -> keys = column names/ values = column values
	 
	        // 4. close
	        db.close();
	    }
	 	
	 	//TODO : 
	 	public void updateActivity(Activities activity){
	        // 1. get reference to writable DB
	        SQLiteDatabase db = this.getWritableDatabase();
	 
	        // 2. create ContentValues to add key "column"/value
	        ContentValues values = new ContentValues();
	        values.put("name", activity.getName()); 
	        values.put("descriptionActivity", activity.getDesciptionActivity()); 
	        values.put("pictureActivity", activity.getPictureActivity()); 
	        values.put("averageMark", activity.getAverageMark()); 
	        values.put("longitude", activity.getLongitude()); 
	        values.put("latitude", activity.getLatitude()); 
	        values.put("website", activity.getWebsite()); 
	        values.put("focusOn", activity.getFocusOn()); 
	        values.put("address", activity.getAddress()); 
	        values.put("pictureActivityString", activity.getPictureActivityString());
	        // 3. insert
	        db.update("activities", values, "idActivity = ? ", new String[] {String.valueOf(activity.getIdActivity())});
	 
	        // 4. close
	        db.close();
	    }
	 	
	 	public void updateImageActivity(Activities activity){
	        // 1. get reference to writable DB
	        SQLiteDatabase db = this.getWritableDatabase();
	        ContentValues values = new ContentValues();
	       
	        values.put("pictureActivity", activity.getPictureActivity());
	        // 3. insert
	        db.update("activities", values, "idActivity = ? ", new String[] {String.valueOf(activity.getIdActivity())});
	 
	        // 4. close
	        db.close();
	    }
	 	
	 	public List<Activities> getAllActivities() {
	        List<Activities> activities = new LinkedList<Activities>();
	  
	        // 1. build the query
	        String query = "SELECT  * FROM activities";
	  
	        // 2. get reference to writable DB
	        SQLiteDatabase db = this.getWritableDatabase();
	        Cursor cursor = db.rawQuery(query, null);
	  
	        // 3. go over each row, build book and add it to list
	        Activities act = null;
	        if (cursor.moveToFirst()) {
	            do {
	            	act = new Activities();
	            	act.setIdActivity(Integer.parseInt(cursor.getString(0)));
	            	act.setName(cursor.getString(1));
	                act.setDesciptionActivity(cursor.getString(2));
	                act.setPictureActivity(cursor.getBlob(3));
	                act.setAverageMark(Float.parseFloat(cursor.getString(4)));
	                act.setLongitude(cursor.getString(5));
	                act.setLatitude(cursor.getString(6));
	                act.setWebsite(cursor.getString(7));
	                act.setFocusOn(Integer.parseInt(cursor.getString(8)));
	                act.setAddress(cursor.getString(9)); 
	                act.setPictureActivityString(cursor.getString(10));
	         
	                activities.add(act);
	            } while (cursor.moveToNext());
	        }
	        return activities;
	    }
	 	
		 public Boolean isExistActivity(int idActivity){
		 
	        // 1. get reference to readable DB
	        SQLiteDatabase db = this.getReadableDatabase();
	 
	        // 1. build the query
	        String query = "SELECT  * FROM activities where idActivity = " + idActivity;
	 
	        // 2. get reference to writable DB
	        Cursor cursor = db.rawQuery(query, null);
	 
	        // 3. if we got results get the first one
	        if (cursor != null && cursor.getCount()!=0) {
	        	db.close();
	        	 return true;
	        }
	        else {
	        	db.close();
	        	return false;
	        }
	    }
		 
		 public List<Activities> getSearchableActivities(String search) {
		        List<Activities> activities = new LinkedList<Activities>();
		  
		        // 1. build the query
		        String query = "SELECT  * FROM activities where name like '%"+search+"%'";
		  
		        // 2. get reference to writable DB
		        SQLiteDatabase db = this.getWritableDatabase();
		        Cursor cursor = db.rawQuery(query, null);
		  
		        // 3. go over each row, build book and add it to list
		        Activities act = null;
		        if (cursor.moveToFirst()) {
		            do {
		            	act = new Activities();
		            	act.setIdActivity(Integer.parseInt(cursor.getString(0)));
		            	act.setName(cursor.getString(1));
		                act.setDesciptionActivity(cursor.getString(2));
		                act.setPictureActivity(cursor.getBlob(3));
		                act.setAverageMark(Float.parseFloat(cursor.getString(4)));
		                act.setLongitude(cursor.getString(5));
		                act.setLatitude(cursor.getString(6));
		                act.setWebsite(cursor.getString(7));
		                act.setFocusOn(Integer.parseInt(cursor.getString(8)));
		                act.setAddress(cursor.getString(9)); 
		                act.setPictureActivityString(cursor.getString(10));
		                activities.add(act);
		            } while (cursor.moveToNext());
		        }
		        // return act
		        return activities;
		    }
		 
		 	public Activities getActivitiesById(Integer idAct) {
		        		  
		        // 1. build the query
		        String query = "SELECT  * FROM activities where idActivity ="+idAct;
		  
		        // 2. get reference to writable DB
		        SQLiteDatabase db = this.getWritableDatabase();
		        Cursor cursor = db.rawQuery(query, null);
		  
		        // 3. go over each row, build act and add it to list
		        Activities act = null;
		        if (cursor.moveToFirst()) {
		            do {
		            	act = new Activities();
		            	act.setIdActivity(Integer.parseInt(cursor.getString(0)));
		            	act.setName(cursor.getString(1));
		                act.setDesciptionActivity(cursor.getString(2));
		                act.setPictureActivity(cursor.getBlob(3));
		                act.setAverageMark(Float.parseFloat(cursor.getString(4)));
		                act.setLongitude(cursor.getString(5));
		                act.setLatitude(cursor.getString(6));
		                act.setWebsite(cursor.getString(7));
		                act.setFocusOn(Integer.parseInt(cursor.getString(8)));
		                act.setAddress(cursor.getString(9)); 
		                act.setPictureActivityString(cursor.getString(10));
		            } while (cursor.moveToNext());
		        }
		        // return act
		        return act;
		    }
		 
		 	
		 	public List<Activities> getFocusOnActivities() {
		        List<Activities> activities = new LinkedList<Activities>();
		  
		        // 1. build the query
		        String query = "SELECT * FROM activities where focusOn = 1";
		  
		        // 2. get reference to writable DB
		        SQLiteDatabase db = this.getWritableDatabase();
		        Cursor cursor = db.rawQuery(query, null);
		  
		        // 3. go over each row, build book and add it to list
		        Activities act = null;
		        if (cursor.moveToFirst()) {
		            do {
		            	act = new Activities();
		            	act.setIdActivity(Integer.parseInt(cursor.getString(0)));
		            	act.setName(cursor.getString(1));
		                act.setDesciptionActivity(cursor.getString(2));
		                act.setPictureActivity(cursor.getBlob(3));
		                act.setAverageMark(Float.parseFloat(cursor.getString(4)));
		                act.setLongitude(cursor.getString(5));
		                act.setLatitude(cursor.getString(6));
		                act.setWebsite(cursor.getString(7));
		                act.setFocusOn(Integer.parseInt(cursor.getString(8)));
		                act.setAddress(cursor.getString(9));
		                act.setPictureActivityString(cursor.getString(10));
		                
		                // Add act to acts
		                activities.add(act);
		            } while (cursor.moveToNext());
		        }
		        
		        // return act
		        return activities;
		    }
//---------------------------------------------------------------------
			 
		    /**
		     * CRUD operations (create "add", read "get", update, delete) Review
		     */

			
			public void addReview(Review review){
		        Log.d("review", review.toString());
		        // 1. get reference to writable DB
		        SQLiteDatabase db = this.getWritableDatabase();
		 
		        // 2. create ContentValues to add key "column"/value
		        ContentValues values = new ContentValues();
		        values.put("idReview", review.getIdReview());
		        values.put("idUser", review.getIdUser()); 
		        values.put("idActivity", review.getIdActivity()); 
		        values.put("title", review.getTitle()); 
		        values.put("description", review.getDescription()); 
		        values.put("mark", review.getMark());
		        values.put("date", review.getDateTime());
		        values.put("language", review.getLanguage());
		 
		        // 3. insert
		        db.insert("review", // table
		                null, //nullColumnHack
		                values); // key/value -> keys = column names/ values = column values
		 
		        // 4. close
		        db.close();
		    }
			
			 public void updateReview(Review review){
			        // 1. get reference to writable DB
			        SQLiteDatabase db = this.getWritableDatabase();
			 
			        // 2. create ContentValues to add key "column"/value
			        ContentValues values = new ContentValues();
			        values.put("idReview", review.getIdReview());
			        values.put("idUser", review.getIdUser()); 
			        values.put("idActivity", review.getIdActivity()); 
			        values.put("title", review.getTitle()); 
			        values.put("description", review.getDescription()); 
			        values.put("mark", review.getMark());
			        values.put("date", review.getDateTime());
			        values.put("language", review.getLanguage());
			 
			        // 3. insert
			        db.update("review", values, "idReview = ? ", new String[] {String.valueOf(review.getIdReview())});
			 
			        // 4. close
			        db.close();
		    }

			 public Boolean isExistReview(int idReview){
				 
			        // 1. get reference to readable DB
			        SQLiteDatabase db = this.getReadableDatabase();
			 
			        // 1. build the query
			        String query = "SELECT * FROM review where idReview = " + idReview;
			 
			        // 2. get reference to writable DB
			        Cursor cursor = db.rawQuery(query, null);
			 
			        // 3. if we got results get the first one
			        if (cursor != null && cursor.getCount()!=0) {
			        	db.close();
			        	 return true;
			        }
			        else {
			        	db.close();
			        	return false;
			        }
			 
			    }
			
			 	public Review getReviewById(Integer idReview) {
	        		  
			        // 1. build the query
			        String query = "SELECT  * FROM review where idReview ="+idReview;
			  
			        // 2. get reference to writable DB
			        SQLiteDatabase db = this.getWritableDatabase();
			        Cursor cursor = db.rawQuery(query, null);
			  
			        // 3. go over each row, build act and add it to list
			        Review rv = null;
			        if (cursor.moveToFirst()) {
			            do {
			            	rv = new Review();
			            	rv.setIdReview(Integer.parseInt(cursor.getString(0)));
			            	rv.setIdUser(Integer.parseInt(cursor.getString(1)));
			            	rv.setIdActivity(Integer.parseInt(cursor.getString(2)));
			            	rv.setTitle(cursor.getString(3));
			            	rv.setDescription(cursor.getString(4));
			            	rv.setMark(Float.parseFloat(cursor.getString(5)));
			            	rv.setDateTime(cursor.getString(6));
			            	rv.setLanguage(cursor.getString(7));
			            	
			            } while (cursor.moveToNext());
			        }
			        // return act
			        return rv;
			    }
			 	
		public List<Review> getReviewsByActivity(int idActivity) {
	        List<Review> reviews = new LinkedList<Review>();
	  
	        // 1. build the query
	        String query = "SELECT  * FROM review WHERE idActivity = " + idActivity;
	  
	        // 2. get reference to writable DB
	        SQLiteDatabase db = this.getWritableDatabase();
	        Cursor cursor = db.rawQuery(query, null);
	  
	        // 3. go over each row, build book and add it to list
	        Review rv = null;
	        if (cursor.moveToFirst()) {
	            do {
	            	rv = new Review();
	            	rv.setIdReview(Integer.parseInt(cursor.getString(0)));
	            	rv.setIdUser(Integer.parseInt(cursor.getString(1)));
	            	rv.setIdActivity(Integer.parseInt(cursor.getString(2)));
	            	rv.setTitle(cursor.getString(3));
	            	rv.setDescription(cursor.getString(4));
	            	rv.setMark(Float.parseFloat(cursor.getString(5)));
	            	rv.setDateTime(cursor.getString(6));
	            	rv.setLanguage(cursor.getString(7));
	            	
	                // Add review to books
	                reviews.add(rv);
	            } while (cursor.moveToNext());
	        }
	        // return reviews
	        return reviews;
	    }
		
		public List<Review>  getReviewsByUserId(int idUser) {
	        List<Review> reviews = new LinkedList<Review>();
	  
	        // 1. build the query
	        String query = "SELECT * FROM review WHERE idUser = " + idUser;
	  
	        // 2. get reference to writable DB
	        SQLiteDatabase db = this.getWritableDatabase();
	        Cursor cursor = db.rawQuery(query, null);
	  
	        // 3. go over each row, build book and add it to list
	        Review rv = null;
	        if (cursor.moveToFirst()) {
	            do {
	            	rv = new Review();
	            	rv.setIdReview(Integer.parseInt(cursor.getString(0)));
	            	rv.setIdUser(Integer.parseInt(cursor.getString(1)));
	            	rv.setIdActivity(Integer.parseInt(cursor.getString(2)));
	            	rv.setTitle(cursor.getString(3));
	            	rv.setDescription(cursor.getString(4));
	            	rv.setMark(Float.parseFloat(cursor.getString(5)));
	            	rv.setDateTime(cursor.getString(6));
	            	rv.setLanguage(cursor.getString(7));
	            	
	                // Add review to books
	                reviews.add(rv);
	            } while (cursor.moveToNext());
	        }

	        // return reviews
	        return reviews;
	    }
}
