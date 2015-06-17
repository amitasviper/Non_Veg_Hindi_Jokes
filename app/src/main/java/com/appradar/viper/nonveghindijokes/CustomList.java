package com.appradar.viper.nonveghindijokes;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by viper on 27/4/15.
 */
public class CustomList extends SimpleCursorAdapter {

    private final Context context;
    private Cursor cursor;

    public CustomList(Context context, int layout, Cursor cursor, String[] columns, int[] to, int flags) {
        super(context, layout, cursor, columns, to, flags);
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = null;

        cursor.moveToPosition(position);

        if(row == null) {
            LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflator.inflate(R.layout.single_joke, parent, false);

            holder = new ViewHolder(row);
            row.setTag(holder);
        }
        else{
            holder = (ViewHolder) row.getTag();
        }

        String read = cursor.getString(cursor.getColumnIndex(JokesDatabase.KEY_VIEWED));
        String favourite = cursor.getString(cursor.getColumnIndex(JokesDatabase.KEY_FAVOURITE));

        holder._id.setText(cursor.getString(cursor.getColumnIndex(JokesDatabase.KEY_ROW_ID)));

        holder.jokeTitle.setText(cursor.getString(cursor.getColumnIndex(JokesDatabase.KEY_TITLE)));

        holder.jokeContent.setText(cursor.getString(cursor.getColumnIndex(JokesDatabase.KEY_CONTENT)));

        holder.jokeTitle.setTextColor(Color.WHITE);

        if(read.contentEquals("n")){
            holder.read_unread.setText("Unread");
            holder.read_unread.setTextColor(Color.RED);
            //Log.d("CustomList", "If statement executed :getView:");
        }
        else{
            holder.read_unread.setText("Read");
            holder.read_unread.setTextColor(Color.GREEN);
            //Log.e("CustomList", "Color Value: " + context.getResources().getColor(R.color.visited));
        }

        Random random = new Random();
        int alpha = 127;
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);

        int color = Color.argb(alpha,r,g,b);

        //Log.e("CustomList", "R:G:B:Value " +r+":"+g+":"+b+":"+color );

        row.setBackgroundColor(color);

        if(favourite.contentEquals("n")){
            holder.favouriteInList.setActivated(false);
        }
        else{
            holder.favouriteInList.setActivated(true);
        }

        //Log.d("CustomList", ":getView: Position "+position);
        return row;
    }


    public class ViewHolder{
        TextView jokeTitle, jokeContent, _id, read_unread;
        ImageView favouriteInList;
        public ViewHolder(View v){
            jokeTitle = (TextView) v.findViewById(R.id.jokeTitle);
            jokeContent = (TextView) v.findViewById(R.id.jokeHolder);
            _id = (TextView) v.findViewById(R.id._id);
            favouriteInList = (ImageView) v.findViewById(R.id.favouriteInListView);
            read_unread = (TextView) v.findViewById(R.id.read_unread);
        }
    }
}