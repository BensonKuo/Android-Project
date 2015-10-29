package net.lionfree.bensonkuo.benui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView addressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        addressTextView = (TextView)findViewById(R.id.address);

        String store = getIntent().getStringExtra("storeInfo");
        final String address = store.split(",")[1];
        //Log.d("address", address);

        addressTextView.setText(address);

        // why new thread?
        // this part will be replaced with AsyncTask
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = Utils.getGEOUrl(address);
                final String result = new String(Utils.urlToBytes(url));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // string ->jsonobj ->string
                        addressTextView.setText(Utils.getLatLngFromJSON(result));

                    }
                });
            }
        }).start();

    }
}
