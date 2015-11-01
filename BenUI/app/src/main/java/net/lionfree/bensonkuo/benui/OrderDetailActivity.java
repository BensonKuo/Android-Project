package net.lionfree.bensonkuo.benui;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView addressTextView;
    //private String address;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        addressTextView = (TextView) findViewById(R.id.address);

        String store = getIntent().getStringExtra("storeInfo");
        String address = store.split(",")[1];
        //Log.d("address", address);

        addressTextView.setText(address);

        // this part will be replaced with AsyncTask
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String url = Utils.getGEOUrl(address);
//                final String result = new String(Utils.urlToBytes(url));
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // string ->jsonobj ->string
//                        addressTextView.setText(Utils.getLatLngFromJSON(result));
//
//                    }
//                });
//            }
//        }).start();

        // Async task

        progressDialog = new ProgressDialog(this);

        GeoCodingTask task = new GeoCodingTask();

        task.execute(address);
    }


        private class GeoCodingTask extends AsyncTask<String,Void,String>{

            @Override
            protected void onPreExecute(){
                progressDialog.setTitle("Converting...");
                progressDialog.show();
            }

            @Override
            protected String doInBackground(String...params){
                String url = Utils.getGEOUrl(params[0]);
                //Log.d("url",url);
                String jsonStr = new String(Utils.urlToBytes(url)); // cannot use toString()?
                //Log.d("jsonstr",jsonStr);
                String latLng = Utils.getLatLngFromJSON(jsonStr);
                Log.d("latlng",latLng);
                return latLng;
            }

            @Override
            protected void onPostExecute(String result){
                progressDialog.dismiss();

                addressTextView.setText(result);

            }
        }






//


}
