package gisllps.healthystartkids.healthystart.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.orthotain.healthystart.R;

public class Bank extends Activity {

    ImageView image_bank;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank);
        image_bank=(ImageView)findViewById(R.id.image_bank);

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

        image_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Bank.this,BankInterior.class);
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
    protected void onResume() {
        super.onResume();
        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.gc();
    }

}
