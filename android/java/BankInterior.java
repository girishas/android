package gisllps.healthystartkids.healthystart.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.orthotain.healthystart.R;
import gisllps.healthystartkids.healthystart.serverurl.ServerUrl;

public class BankInterior extends Activity {

    String total_coins,user_id,id,redeemed_coins,status,deposited;
    ProgressDialog progress;
    SharedPreferences user_info_SharedPreferences;
    String  type_String,data_String,id_String;
    TextView tv_earned_coins,tv_redeemed_coins;
    Button btn_deposit_coin;

    ImageView drawable_menu_layout;
    View cView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_detail);
        tv_earned_coins=(TextView)findViewById(R.id.tv_earned_coins);
        tv_redeemed_coins=(TextView)findViewById(R.id.tv_redeemed_coins);
        btn_deposit_coin=(Button)findViewById(R.id.btn_deposit_coin);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

         cView = getLayoutInflater().inflate(R.layout.header, null);

        drawable_menu_layout=(ImageView)cView.findViewById(R.id.drawable_menu_layout);

        getActionBar().setCustomView(cView);

        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.backgroundbar));

        ImageView drawable_menu_back=(ImageView)cView.findViewById(R.id.drawable_menu_back);

        drawable_menu_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        user_info_SharedPreferences=getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        user_id=user_info_SharedPreferences.getString("user_id","");

        new SendPostRequest().execute();

        System.gc();

        btn_deposit_coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DepositCoin().execute();
            }
        });

       drawable_menu_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlert();
            }
        });
    }

    private void openAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BankInterior.this);



        alertDialogBuilder.setTitle("Information Dialog");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BankInterior.this);
        String visitor_string= preferences.getString("bank","");

        alertDialogBuilder.setMessage(visitor_string);

        // set positive button: Yes message

        alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog,int id) {

                // go to a new activity of the app


            }

        });






        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();

    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }


    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){

            progress= new ProgressDialog(BankInterior.this);

            progress.setMessage("please wait...");

            progress.setProgressStyle(ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);

            progress.setIndeterminate(true);

            progress.setCancelable(false);

            progress.show();
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL(ServerUrl.child_coins_url); // here is your URL path

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("user_id", user_id);


                Log.e("params",jsonObject.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonObject.toString());

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {


            try{
                progress.dismiss();
                JSONObject jsonObject = new JSONObject(result);

                type_String =jsonObject.getString("type");
                if(type_String.matches("error")){

                    data_String=jsonObject.getString("data");
                    Toast.makeText(getBaseContext(), data_String, Toast.LENGTH_SHORT).show();

                    tv_earned_coins.setText("Total Coins: 0");
                    tv_redeemed_coins.setText("Redeemed Coins: 0");

                    btn_deposit_coin.setText("Deposit Coin: 0");

                }else{

                    data_String=jsonObject.getString("data");
                    JSONObject jsonData=new JSONObject(data_String);

                    id_String=jsonData.getString("id");
                    user_id=jsonData.getString("user_id");
                    total_coins=jsonData.getString("total_coins");
                    redeemed_coins=jsonData.getString("redeemed_coins");
                    deposited=jsonData.getString("deposited");
                    status=jsonData.getString("status");

                    int total= Integer.parseInt(total_coins);
                    int redeemed=Integer.parseInt(redeemed_coins);

                    int total_coins=total-redeemed;



                    tv_earned_coins.setText("Total Coins: "+total_coins);
                    tv_redeemed_coins.setText("Redeemed Coins: "+redeemed_coins);
                    btn_deposit_coin.setText("Deposit Coin: "+deposited);

                }

            } catch (JSONException e1) {
                // TODO Auto-generated catch block

                progress.dismiss();

                Toast.makeText(getBaseContext(), "Connection error", Toast.LENGTH_SHORT).show();

                e1.printStackTrace();
            } catch (OutOfMemoryError ex){

                ex.printStackTrace();

                Toast.makeText(getBaseContext(),"Please wait for response",Toast.LENGTH_SHORT).show();

            }
        }
    }

    public class DepositCoin extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){

            progress= new ProgressDialog(BankInterior.this);

            progress.setMessage("please wait...");

            progress.setProgressStyle(ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);

            progress.setIndeterminate(true);

            progress.setCancelable(false);

            progress.show();
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL(ServerUrl.deposit_child_coin); // here is your URL path

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("user_id", user_id);


                Log.e("params",jsonObject.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonObject.toString());

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {


            try{
                progress.dismiss();
                JSONObject jsonObject = new JSONObject(result);

                type_String =jsonObject.getString("type");
                if(type_String.matches("error")){

                    data_String=jsonObject.getString("data");
                    Toast.makeText(getBaseContext(), data_String, Toast.LENGTH_SHORT).show();

                    tv_earned_coins.setText("Total Earned Coins: 0");
                    tv_redeemed_coins.setText("Redeemed Coins: 0");

                }else{

                    data_String=jsonObject.getString("data");
                    JSONObject jsonData=new JSONObject(data_String);

                    id_String=jsonData.getString("id");
                    user_id=jsonData.getString("user_id");
                    total_coins=jsonData.getString("total_coins");
                    redeemed_coins=jsonData.getString("redeemed_coins");
                    deposited=jsonData.getString("deposited");
                    status=jsonData.getString("status");

                    int total= Integer.parseInt(total_coins);
                    int redeemed=Integer.parseInt(redeemed_coins);

                    int total_coins=total-redeemed;



                    tv_earned_coins.setText("Total Coins: "+total_coins);




                    tv_redeemed_coins.setText("Redeemed Coins: "+redeemed_coins);
                    btn_deposit_coin.setText("Deposit Coin: "+deposited);

                }

            } catch (JSONException e1) {
                // TODO Auto-generated catch block

                progress.dismiss();

                Toast.makeText(getBaseContext(), "Connection error", Toast.LENGTH_SHORT).show();

                e1.printStackTrace();
            } catch (OutOfMemoryError ex){

                ex.printStackTrace();

                Toast.makeText(getBaseContext(),"Please wait for response",Toast.LENGTH_SHORT).show();

            }
        }
    }


    @Override
    public void onPause() {



        super.onPause();
        System.gc();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        System.gc();
    }


}
