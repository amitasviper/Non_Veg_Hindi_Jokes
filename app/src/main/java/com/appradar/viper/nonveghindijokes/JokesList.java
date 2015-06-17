package com.appradar.viper.nonveghindijokes;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by viper on 28/4/15.
 */
public class JokesList extends Activity {
    ListView list;
    JokesDatabase jDatabase;
    CustomList customCursorListAdapter;
    AdView listViewAd;
    static int id;
    String category;
    InterstitialAd interstitialAd;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jokes_list);

        jDatabase = new JokesDatabase(this);
        jDatabase.open();
        //jDatabase.insertForFun();

        Intent intent = getIntent();
        id = (int) intent.getSerializableExtra("buttonId");

        list = (ListView) findViewById(R.id.listView);

        listViewAd = (AdView) findViewById(R.id.listViewAd);
        AdRequest adRequest = new AdRequest.Builder().build();
        listViewAd.loadAd(adRequest);

        listViewAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                createSpaceForAd();
            }
        });

        CacheLargeAd();
    }

    private void createSpaceForAd(){
        LinearLayout list_ly = (LinearLayout) findViewById(R.id.listLinearLayout);
        LinearLayout list_ad_ly = (LinearLayout) findViewById(R.id.listAdLinearLayout);

        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) list_ly.getLayoutParams();
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) list_ad_ly.getLayoutParams();

        params1.weight = 8.5f;
        params2.weight = 1.5f;

        list_ly.setLayoutParams(params1);
        list_ad_ly.setLayoutParams(params2);
    }

    private void CacheLargeAd() {
        interstitialAd = ManageAds.getInterstitialAd();
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                finish();
            }

            @Override
            public void onAdLoaded() {
                if (MainOptions.DEBUG)
                    Toast.makeText(JokesList.this, "Ad loaded in list View", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayInterstitialAd(){
        if(interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
        else{
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        displayInterstitialAd();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayList(id);
    }

    public void displayList(int buttonId){

        Cursor cursor;

        category = "exclusive";
        if(id == R.id.santa_banta)
            category = "santa_banta";
        else if(id == R.id.husband_wife)
            category = "husband_wife";
        else if(id == R.id.boy_girl)
            category = "boy_girl";
        else if(id == R.id.college)
            category = "college";
        else if(id == R.id.double_meaning)
            category = "double_meaning";
        else if(id == R.id.politics)
            category = "politics";
        else if(id == R.id.miscellaneous)
            category = "miscellaneous";

        else if(id == R.id.english)
            category = "english";

        else if(id == R.id.hindi)
            category = "hindi";


        if(id == R.id.favourite) {
            cursor = jDatabase.fetchFavourites();
            category = "favourite";
        }
        else
            cursor = jDatabase.fetchAllJokes(category);

        if(cursor.getCount() == 0){
            displayEmptyList();
            return;
        }

        String[] columns = {jDatabase.KEY_ROW_ID, jDatabase.KEY_TITLE, jDatabase.KEY_CONTENT, jDatabase.KEY_VIEWED};

        int[] to = new int[]{R.id._id, R.id.jokeTitle, R.id.jokeHolder};

        customCursorListAdapter = new CustomList(this,R.layout.single_joke,cursor,columns,to,0);

        list.setAdapter(customCursorListAdapter);
        list.setOnItemClickListener(new ListViewClickListener());

    }




    private void displayEmptyList(){

        if(count != 0)
            return;
        TextView dynamicTextView = new TextView(this);
        dynamicTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dynamicTextView.setText(" No Jokes In This Category ");
        dynamicTextView.setTextColor(Color.WHITE);

        list.setVisibility(View.GONE);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.listLinearLayout);
        ((LinearLayout.LayoutParams)linearLayout.getLayoutParams()).gravity = Gravity.CENTER;
        linearLayout.addView(dynamicTextView);
        count++;
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public class ListViewClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            LinearLayout lin_lay = (LinearLayout) getViewByPosition(position,list); // outer LinearLayout

            LinearLayout inner_lay = (LinearLayout) lin_lay.getChildAt(0);          // inner LinearLayout

            ImageView favourite = (ImageView) inner_lay.getChildAt(0);              // first element in inner LinearLayout i.e ImageView
            TextView titleTextView = (TextView) inner_lay.getChildAt(1);            // second element in inner LinearLayout i.e TextView(Title)

            TextView idTextView = (TextView) lin_lay.getChildAt(2);                 // third element in inner LinearLayout i.e TextView(_id) id hidden

            String title = titleTextView.getText().toString();
            String _id = idTextView.getText().toString();

            boolean isFavourite = favourite.isActivated();

            //Log.d("ListViewClickListener", "The title is "+title+" with id "+_id);
            Intent intent = new Intent(JokesList.this,DisplayJoke.class);
            intent.putExtra("jokeTitle",title);
            intent.putExtra("_id",_id);
            intent.putExtra("category",category);
            intent.putExtra("isFavourite",isFavourite);
            intent.putExtra("source","jokelist");
            startActivity(intent);
        }
    }
}
