package net.lionfree.bensonkuo.benui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

public class OrderDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        String store = getIntent().getStringExtra("storeInfo");
        String address = store.split(",")[1];
        Log.d("address", address);


    }
}
