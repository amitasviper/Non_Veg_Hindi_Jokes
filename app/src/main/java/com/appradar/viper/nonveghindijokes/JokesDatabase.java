package com.appradar.viper.nonveghindijokes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by viper on 27/4/15.
 */
public class JokesDatabase {

    private DataBaseHelper myDbHelper;
    private SQLiteDatabase myDb;
    private Context context;

    public static final String DATABASE_NAME = "khatta";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "jokes";
    public static final String KEY_ROW_ID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_VIEWED = "viewed";
    public static final String KEY_FAVOURITE = "favourite";

    public static final String DATABASE_CREATE = "CREATE TABLE if not exists "+TABLE_NAME+" (" +
            KEY_ROW_ID +" integer PRIMARY KEY autoincrement, " +
            KEY_TITLE +" varchar(50), " +
            KEY_CONTENT +" TEXT, " +
            KEY_CATEGORY + " varchar(10), " +
            KEY_VIEWED + " varchar(1), " +
            KEY_FAVOURITE + " varchar(1)" +
            ");";

    private static class DataBaseHelper extends SQLiteOpenHelper{

        DataBaseHelper(Context context){
            super(context,DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //Log.d("DatabaseHelper","Database created :onCreate:");
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //Log.d("DatabaseHelper","Database updation from " + oldVersion + " to "+newVersion+", which will destroy all old data :onCreate:");
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
            onCreate(db);
        }
    }

    public JokesDatabase(Context context){
        this.context = context;
    }

    public JokesDatabase open(){
        myDbHelper = new DataBaseHelper(context);
        myDb = myDbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        if(myDbHelper != null){
            myDbHelper.close();
        }
    }

    public long insertEntry(String title, String content, String category, String viewed, String favourite){
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, title);
        values.put(KEY_CONTENT, content);
        values.put(KEY_CATEGORY, category);
        values.put(KEY_VIEWED,viewed);
        values.put(KEY_FAVOURITE, favourite);

        //Log.d("JokesDatabase","Inserted *"+title+"* :insertEntry:");
        return myDb.insert(TABLE_NAME, null, values);
    }

    public Cursor fetchAllJokes( String category){
        Cursor cursor = myDb.query(TABLE_NAME, new String[]{KEY_ROW_ID, KEY_TITLE, KEY_CONTENT, KEY_VIEWED, KEY_CATEGORY, KEY_FAVOURITE}, KEY_CATEGORY +" ='"+category+"'", null, null, null, KEY_VIEWED +" ASC");

        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor getRandomRow(){
        Cursor cursor = myDb.query(TABLE_NAME + " Order BY RANDOM() LIMIT 1",
                new String[] { "*" }, null, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchNext(String _id, String category){
        String new_id = "", viewed = "";
        Integer id = Integer.parseInt(_id);
        id += 1;
        Cursor cursor = myDb.query(TABLE_NAME, new String[]{KEY_ROW_ID, KEY_TITLE, KEY_CONTENT, KEY_VIEWED, KEY_FAVOURITE}, KEY_ROW_ID+" == '"+id+"' and "+ KEY_CATEGORY+" ='"+category+"'", null, null, null,KEY_VIEWED +" ASC","1");
        return cursor;
    }

    public Cursor fetchNextFavourite(String _id, String favourite){
        String new_id = "", viewed = "";
        Cursor cursor = myDb.query(TABLE_NAME, new String[]{KEY_ROW_ID, KEY_TITLE, KEY_CONTENT, KEY_VIEWED, KEY_FAVOURITE}, KEY_ROW_ID+" > '"+_id+"' and "+ KEY_FAVOURITE+" ='"+favourite+"'", null, null, null,KEY_VIEWED +" ASC","1");
        return cursor;
    }

    public Cursor fetchPrevious(String _id, String category){
        String new_id = "", viewed = "";
        Integer id = Integer.parseInt(_id);
        id -= 1;
        Cursor cursor = myDb.query(TABLE_NAME, new String[]{KEY_ROW_ID, KEY_TITLE, KEY_CONTENT, KEY_VIEWED, KEY_FAVOURITE}, KEY_ROW_ID+" == '"+id+"' and "+ KEY_CATEGORY+" ='"+category+"'", null, null, null,KEY_VIEWED +" ASC","1");
        return cursor;
    }

    public Cursor fetchPreviousFavourite(String _id, String favourite){
        String new_id = "", viewed = "";
        Cursor cursor = myDb.query(TABLE_NAME, new String[]{KEY_ROW_ID, KEY_TITLE, KEY_CONTENT, KEY_VIEWED, KEY_FAVOURITE}, KEY_ROW_ID+" < '"+_id+"' and "+ KEY_FAVOURITE+" ='"+favourite+"'", null, null, null,KEY_VIEWED +" ASC","1");
        return cursor;
    }

    public void markAsRead(String _id){
        ContentValues values = new ContentValues();
        values.put(KEY_VIEWED,"y");
        myDb.update(TABLE_NAME,values, KEY_ROW_ID + " = '"+_id+"' ", null);
        //Log.d("JokesDatabase", "Marking joke as read :markAsRead:");
    }

    public void markAsFavourite(String _id){
        ContentValues values = new ContentValues();
        values.put(KEY_FAVOURITE,"y");
        myDb.update(TABLE_NAME,values, KEY_ROW_ID + " = '"+_id+"' ", null);
        //Log.d("JokesDatabase", "Adding to favourite:markAsFavourite:");
    }

    public void removeFromFavourite(String _id){
        ContentValues values = new ContentValues();
        values.put(KEY_FAVOURITE,"n");
        myDb.update(TABLE_NAME,values, KEY_ROW_ID + " = '"+_id+"' ", null);
       //Log.d("JokesDatabase", "Removing from favourite :removeFromFavourite:");
    }

    public Cursor fetchFavourites(){
        Cursor cursor = myDb.query(TABLE_NAME, new String[]{KEY_ROW_ID, KEY_TITLE, KEY_CONTENT, KEY_VIEWED, KEY_CATEGORY, KEY_FAVOURITE}, KEY_FAVOURITE+" = 'y' ", null, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }

        return cursor;
    }

    public String fetchByTitle(String title){
        String content = "", viewed = "";
        Cursor cursor = myDb.query(TABLE_NAME, new String[]{KEY_CONTENT, KEY_VIEWED, KEY_FAVOURITE}, KEY_TITLE+" = '"+title+"' ", null, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
            content = cursor.getString(cursor.getColumnIndex(KEY_CONTENT));
            viewed = cursor.getString(cursor.getColumnIndex(KEY_VIEWED));
            if(viewed.contentEquals("n"))
                markAsRead(title);
        }

        return content;
    }

    public String fetchById(String _id){
        String content = "", viewed = "";
        Cursor cursor = myDb.query(TABLE_NAME, new String[]{KEY_CONTENT, KEY_VIEWED}, KEY_ROW_ID+" = '"+_id+"' ", null, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
            content = cursor.getString(cursor.getColumnIndex(KEY_CONTENT));
            viewed = cursor.getString(cursor.getColumnIndex(KEY_VIEWED));
            //if(viewed.contentEquals("n"))
            markAsRead(_id);
        }

        return content;
    }

    public void insertForFun(){
        String[] title = {"Pappu Ka Gussa Santa Par", "Punjabi Boliyaan", "Who Is Stronger, Man Or Woman?",
                            "Forgive Me Father For I Have Sinned.."};

        String[] content = {"Santa Ne Pappu Ko Muth Maarte Dekha Aur Bola Santa: \"Oye Harami, Ye Kya Kar Raha Hai Tu?\" Pappu Gusse Se: \"Apna Kaam Khud Karta Hun, Apni Khushi",
                "Daal Mungi Di, Fuddi Goongi Di, Choot Nurse Di, Duniya Taras Di, Loda Sardar Da, Badka Marda, Chut Bhapan Di, Koi Lod Nai Maapn Di, Bhosri Jatti",
                "Who Is Stronger, Man Or Woman? Answer: A Woman Because She Lifts Two Mountains On Her Chest While A Man Lifts Only Two Stones With The Help Of A Cr",
                "Man To Priest: \"Forgive Me Father For I Have Sinned I Usually Read Dirty Jokes And View Pictures Of Girls On My Mobile\" Priest: \"Forward Your Sins"};

        for(int i = 0; i < 2; i++){
            insertEntry(title[i], content[i], "hindi", "n" ,"n");
        }

        for(int i = 2; i < 4; i++){
            insertEntry(title[i], content[i], "english", "n", "n");
        }
    }
}
