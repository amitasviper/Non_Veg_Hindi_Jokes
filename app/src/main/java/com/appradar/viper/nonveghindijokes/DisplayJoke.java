package com.appradar.viper.nonveghindijokes;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by viper on 28/4/15.
 */
public class DisplayJoke extends Activity {
  JokesDatabase jDatabase;
  TextView heading, body;
  ImageView favourite;
  Button share, next, previous;
  String _id;
  String category;
  AdView displayJokeAd;

  ScrollView textScroll;

  String source;

  private float locX1, locX2;
  static final int max_displacement = 150;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_display_joke);

    ButtonHandler handler = new ButtonHandler();

    heading = (TextView) findViewById(R.id.jokeHeading);
    body = (TextView) findViewById(R.id.jokeBody);
    body.setMovementMethod(new ScrollingMovementMethod());


    share = (Button) findViewById(R.id.share);
    share.setOnClickListener(handler);

    next = (Button) findViewById(R.id.next);
    next.setOnClickListener(handler);

    previous = (Button) findViewById(R.id.previous);
    previous.setOnClickListener(handler);

    displayJokeAd = (AdView) findViewById(R.id.displayJokeAd);
    AdRequest adRequest = new AdRequest.Builder().build();
    displayJokeAd.loadAd(adRequest);

    favourite = (ImageView) findViewById(R.id.favourite);
    favourite.setOnClickListener(handler);

    textScroll = (ScrollView) findViewById(R.id.textScroll);

    fetchJokeAndPlace();
  }

  @Override
  public void onBackPressed() {
    if(source.equalsIgnoreCase("AlarmReceiver")){
      jDatabase.close();
      Intent intent = new Intent(DisplayJoke.this, MainOptions.class);
      startActivity(intent);
      finish();
    }
    else{
      jDatabase.close();
      finish();
    }
  }

  private void fetchJokeAndPlace(){
    Intent intent = getIntent();
    String title = (String) intent.getSerializableExtra("jokeTitle");
    _id = (String) intent.getSerializableExtra("_id");
    category = (String) intent.getSerializableExtra("category");
    boolean isFavourite = (boolean) intent.getSerializableExtra("isFavourite");
    source = (String) intent.getSerializableExtra("source");
    if(isFavourite){
      favourite.setActivated(true);
    }
    else{
      favourite.setActivated(false);
    }
    jDatabase = new JokesDatabase(this);
    jDatabase.open();
    String content;
    if(source.equalsIgnoreCase("GcmIntentService")){
      Log.e("DISPLAYJOKE", "inside source if");
      content = (String) intent.getSerializableExtra("content");
    }
    else {
      Log.e("DISPLAYJOKE", "inside source else");
      content = jDatabase.fetchById(_id);
    }
    heading.setText(title);
    body.setText(content);
    //Log.d("Display Joke", "Joke title is "+title+" :fetchJokeAndPlace:");
  }

  public void SetJoke(Cursor cursor){
    if(cursor.moveToNext()) {
      cursor.moveToFirst();
      String title = cursor.getString(cursor.getColumnIndex(jDatabase.KEY_TITLE));
      String content = cursor.getString(cursor.getColumnIndex(jDatabase.KEY_CONTENT));
      String viewed = cursor.getString(cursor.getColumnIndex(jDatabase.KEY_VIEWED));
      String starred = cursor.getString(cursor.getColumnIndex(jDatabase.KEY_FAVOURITE));

      _id = cursor.getString(cursor.getColumnIndex(jDatabase.KEY_ROW_ID));
      Log.d("DisplayJoke", "New title is " + title);

      if(viewed.contentEquals("n"))
        jDatabase.markAsRead(_id);

      if(starred.contentEquals("y")){
        favourite.setActivated(true);
      }
      else{
        favourite.setActivated(false);
      }
      heading.setText(title);
      body.setText(content);
    }
    else{
      Toast.makeText(DisplayJoke.this,"Reached at the end", Toast.LENGTH_SHORT).show();
    }
  }

  public void nextJoke(){
    textScroll.scrollTo(0,0);
    Cursor cursor;
    if(category.contentEquals("favourite"))
      cursor = jDatabase.fetchNextFavourite(_id,"y");
    else
      cursor = jDatabase.fetchNext(_id,category);
    SetJoke(cursor);
    cursor.close();
  }

  public void previousJoke(){
    textScroll.scrollTo(0,0);
    Cursor cursor;
    if(category.contentEquals("favourite"))
      cursor = jDatabase.fetchPreviousFavourite(_id,"y");
    else
      cursor = jDatabase.fetchPrevious(_id,category);
    SetJoke(cursor);
    cursor.close();
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent event) {
    switch (event.getAction()){
      case MotionEvent.ACTION_DOWN:
        locX1 = event.getX();
        break;
      case MotionEvent.ACTION_UP:
        locX2 = event.getX();
        float deltaX = locX1 - locX2;
        if(Math.abs(deltaX) > max_displacement && (locX2 < locX1)){
          nextJoke();
          //Toast.makeText(this, "Right to Left Swipe", Toast.LENGTH_SHORT).show();

        }
        else if(Math.abs(deltaX) > max_displacement && (locX2 > locX1)){
          previousJoke();
          //Toast.makeText(this, "Left to Right Swipe", Toast.LENGTH_SHORT).show();
        }
        else{

        }
    }
    return super.dispatchTouchEvent(event);
  }

  private class ButtonHandler implements View.OnClickListener{

    private void setStar(View v){
      if (favourite.isActivated()){
        v.setActivated(false);
        jDatabase.removeFromFavourite(_id);
        Toast.makeText(DisplayJoke.this,"Removed from favourite", Toast.LENGTH_SHORT).show();
      }
      else{
        v.setActivated(true);
        jDatabase.markAsFavourite(_id);
        Toast.makeText(DisplayJoke.this,"Marked as favourite", Toast.LENGTH_SHORT).show();
      }
    }



    @Override
    public void onClick(View v) {
      if(v.getId() == R.id.share){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Jokes");
        shareIntent.putExtra(Intent.EXTRA_TEXT, body.getText().toString());
        startActivity(Intent.createChooser(shareIntent, "Share Via"));
      }

      if(v.getId() == R.id.favourite){
        setStar(v);
      }
      if(v.getId() == R.id.next){
        nextJoke();
      }
      if(v.getId() == R.id.previous){
        previousJoke();
      }
    }
  }
}
