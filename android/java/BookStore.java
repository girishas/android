package gisllps.healthystartkids.healthystart.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.orthotain.healthystart.R;

/**
 * Created by Team01 on 6/6/2017.
 */

public class BookStore extends Activity {

    ImageView image_book_main;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_store);
        image_book_main=(ImageView)findViewById(R.id.image_book_main);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        View cView = getLayoutInflater().inflate(R.layout.header_custom, null);



        getActionBar().setCustomView(cView);

        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundbar));
        ImageView drawable_menu_back=(ImageView)cView.findViewById(R.id.drawable_menu_back);

        drawable_menu_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        image_book_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BookStore.this,BookStoreInterior.class);
                startActivity(intent);
                overridePendingTransition(R.animator.slide_in_left,R.animator.slide_in_right);
            }
        });
        System.gc();
    }


    @Override
    public void onPause() {
        super.onPause();
        System.gc();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

}
