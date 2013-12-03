package sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

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

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		// SQL statement to create book table
		
        String CREATE_ACTIVITIES_TABLE = "CREATE TABLE activities ( " +
                "idActivity INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, "+
                "descriptionActivity TEXT, "+
                "pictureActivity TEXT, "+
                "averageMark REAL, "+
                "longitude TEXT, "+
                "latitude TEXT, "+
                "website TEXT, "+
                "focusOn INTEGER )";
        
        String CREATE_ACTIVITIES_GROUPS_TABLE =  "CREATE TABLE activitiesgroups ("+
        		"idActivity INTEGER PRIMARY KEY" +
        		"idGroup INTEGER PRIMARY KEY)";
        
        String CREATE_GROUPS_TABLE="CREATE TABLE groups("+
      		  "idGroup INTEGER PRIMARY KEY AUTO_INCREMENT,"+
      		  "name TEXT,"+
      		  "descriptionGroup TEXT,"+
      		  "pictureGroup TEXT)";
        
        String CREATE_MESSAGES_TABLE = "CREATE TABLE messages ("+
				"idMessage INTEGER PRIMARY KEY AUTOINCREMENT,"+
				"idUserSender INTEGER,"+
				"idUserReceiver` INTEGER,"+
				"message` TEXT,"+
				"date` TEXT,"+
				"read` TEXT)";
        
        String CREATE_REVIEW_TABLE ="CREATE TABLE review ("+
        		"idReview INTEGER PRIMARY KEY AUTO_INCREMENT,"+
        		"idUSer INTEGER,"+
        		"idActivity INTEGER,"+
        		"title TEXT,"+
        		"description TEXT,"+
        		"mark REAL,"+
        		"date TEXT,"+
        		"language TEXT)";
        
        String CREATE_USERS_TABLE = "CREATE TABLE users ("+
        		  "idUser INTEGER PRIMARY KEY AUTO_INCREMENT,"+
        		  "firstname TEXT,"+
        		  "lastname TEXT,"+
        		  "password TEXT,"+
        		  "pictureUser TEXT,"+
        		  "mail TEXT,"+
        		  "banned INTEGER,"+
        		  "lastConnection TEXT,"+
        		  "token TEXT,"+
        		  "nbTentative INTEGER,"+
        		  "BannedDate TEXT)";
        
        String CREATE_USERSGROUPS_TABLE ="CREATE TABLE usersgroups("+
        		  "idUser INTEGER PRIMARY KEY,"+
        		  "idGroup INTEGER PRIMARY KEY)";
        	
        
        db.execSQL(CREATE_ACTIVITIES_TABLE);
        db.execSQL(CREATE_ACTIVITIES_GROUPS_TABLE);
        db.execSQL(CREATE_GROUPS_TABLE);
        db.execSQL(CREATE_MESSAGES_TABLE);
        db.execSQL(CREATE_REVIEW_TABLE);
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_USERSGROUPS_TABLE);
      
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS activities");
        db.execSQL("DROP TABLE IF EXISTS activitiesgroups");
        db.execSQL("DROP TABLE IF EXISTS groups");
        db.execSQL("DROP TABLE IF EXISTS messages");
        db.execSQL("DROP TABLE IF EXISTS review");
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS usersgroups");
 
        // create fresh books table
        this.onCreate(db);
	}

}
