package net.lionfree.bensonkuo.benui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

public class OrderDetailActivity extends AppCompatActivity {

    int drink=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);


        String store = getIntent().getStringExtra("storeInfo");
        String address = store.split(",")[1];
        Log.d("address", address);

        try {
            JSONArray ja = new JSONArray(getIntent().getStringExtra("drink"));

            Log.d("debug","123");
            for (int i = 0; i < 2; i++) {
                    int l = ja.getJSONObject(i).getInt("Large");
                    int m = ja.getJSONObject(i).getInt("Medium");
                    drink +=  (l + m);
            }
        }catch(JSONException e){
            e.toString();
        }
        Log.d("count", String.valueOf(drink));

    }
}
