package gisllps.healthystartkids.healthystart.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import com.orthotain.healthystart.R;
import gisllps.healthystartkids.healthystart.adapter.BookListAdapter;
import gisllps.healthystartkids.healthystart.model.BookItemList;
import gisllps.healthystartkids.healthystart.serverurl.ServerUrl;

/**
 * Created by Team01 on 6/19/2017.
 */

public class BookListing extends Activity {

    ProgressDialog progress;
    String data_String,pdf_url,type_String,id,book_category_id,title,author_name,status,pdf_name;

    BookListAdapter bookListAdapter;

    ArrayList<BookItemList> bookItemLists=new ArrayList<>();

    BookItemList bookItemList;

    ListView lv_book_list;
    TextView no_data;

    int category_id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_listing);

        lv_book_list=(ListView)findViewById(R.id.lv_book_list);
        no_data=(TextView) findViewById(R.id.no_data);


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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BookListing.this);
        category_id= preferences.getInt("category_id", 0);

        new SendPostRequest().execute();

    }


    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){

            progress= new ProgressDialog(BookListing.this);

            progress.setMessage("please wait...");

            progress.setProgressStyle(ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);

            progress.setIndeterminate(true);

            progress.setCancelable(false);

            progress.show();
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL(ServerUrl.book_listing_url); // here is your URL path

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("category_id", category_id);


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
                    no_data.setText(data_String);
                    no_data.setVisibility(View.VISIBLE);
                    lv_book_list.setVisibility(View.GONE);

                }else{
                    no_data.setVisibility(View.GONE);
                    lv_book_list.setVisibility(View.VISIBLE);
                    pdf_url =jsonObject.getString("pdf_url");
                    JSONArray Data = jsonObject.getJSONArray("data");

                    for (int i1 = 0; i1 < Data.length(); i1++) {
                        JSONObject jsonObj2 = Data.getJSONObject(i1);
                        bookItemList=new BookItemList();
                        bookItemList.id=jsonObj2.getString("id");
                        bookItemList.book_category_id=jsonObj2.getString("book_category_id");
                        bookItemList.title=jsonObj2.getString("title");
                        bookItemList.author_name=jsonObj2.getString("author_name");
                        bookItemList. status=jsonObj2.getString("status");
                        bookItemList.pdf_name=pdf_url+jsonObj2.getString("pdf_name");

                        bookItemLists.add(bookItemList);

                    }


                    bookListAdapter=new BookListAdapter(BookListing.this,bookItemLists);
                    lv_book_list.setAdapter(bookListAdapter);
                    bookListAdapter.notifyDataSetChanged();

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
