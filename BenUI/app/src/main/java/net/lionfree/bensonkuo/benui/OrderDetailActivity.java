package net.lionfree.bensonkuo.benui;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView addressTextView;
    private String store;
    private ProgressDialog progressDialog;

    private WebView webView;
    private ImageView imageView;

    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        addressTextView = (TextView) findViewById(R.id.address);

        store = getIntent().getStringExtra("storeInfo");
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


        webView = (WebView) findViewById(R.id.webView);
        imageView = (ImageView) findViewById(R.id.imageView);

        mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.googleMap);
        googleMap = mapFragment.getMap();
    }

    private void setupGoogleMap(String latlngStr){
        Double lat = Double.valueOf(latlngStr.split(",")[0]);
        Double lng = Double.valueOf(latlngStr.split(",")[1]);

        LatLng latLng = new LatLng(lat,lng);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));

        String name = store.split(",")[0];
        String address = store.split(",")[1];

        MarkerOptions markerOpts = new MarkerOptions().title(name).snippet(address).flat(true).position(latLng);
        googleMap.addMarker(markerOpts);
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(OrderDetailActivity.this, marker.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }


    private class GeoCodingTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            progressDialog.setTitle("Locating...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = Utils.getGEOUrl(params[0]);
            String jsonStr = new String(Utils.urlToBytes(url)); // cannot use toString()
            Log.d("new String", jsonStr);
            // Log.d("toString()",jsonStr1);// output: [B@2801ffa4

            String latLng = Utils.getLatLngFromJSON(jsonStr);
            Log.d("latlng", latLng);
            return latLng;
        }

//            @Override
//            protected Void onProgressUpdate(){
//
//            }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();

            progressDialog.setTitle("Rendering...");
            progressDialog.show();

            addressTextView.setText(result);

            // web view pic
            String staticMap1 = Utils.getStaticMapUrl(result, "16", "600x400");
            webView.loadUrl(staticMap1);

            // google map
            setupGoogleMap(result);

            StaticMapTask smt = new StaticMapTask();
            smt.execute(result);
        }
    }


    private class StaticMapTask extends AsyncTask<String, Void, byte[]> {
        @Override
        protected byte[] doInBackground(String...params) {
            String mapUrl = Utils.getStaticMapUrl(params[0], "16", "800x600");
            return Utils.urlToBytes(mapUrl);
        }

        @Override
        protected void onPostExecute(byte[] bytes){
            progressDialog.dismiss();

            Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageView.setImageBitmap(bm);
        }

    }


}
