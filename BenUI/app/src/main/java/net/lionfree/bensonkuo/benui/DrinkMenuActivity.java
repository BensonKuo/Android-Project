package net.lionfree.bensonkuo.benui;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DrinkMenuActivity extends AppCompatActivity {

    private TextView storeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_menu);

        storeInfo = (TextView)findViewById(R.id.storeInfo);
        String storeStr = getIntent().getStringExtra("store_info");
        storeInfo.setText(storeStr);
    }

    public void add(View view){
        // 實體化被選取的按鈕
        Button btn = (Button)view;
        int n = Integer.parseInt(btn.getText().toString());
        btn.setText(String.valueOf(n+1));
    }

    public void done(View view){
        Intent data = new Intent();
        data.putExtra("order", getValue().toString());

        setResult(RESULT_OK, data);
        //Call this to set the result that your activity will return to its caller
        finish();
        //Call this when your activity is done and should be closed.
        //The ActivityResult is propagated back to whoever launched you via onActivityResult().
    }

    // 寫入訂購內容 並 回傳
    public JSONArray getValue(){

        JSONArray order = new JSONArray();
        RelativeLayout root = (RelativeLayout)findViewById(R.id.root);

        // 計算子元素數目：4
        int len = root.getChildCount();

        for (int i=1; i<len-1; i++){

            LinearLayout ll = (LinearLayout)root.getChildAt(i);

            String name = ((TextView)ll.getChildAt(0)).getText().toString();
            int l = Integer.parseInt(((Button)ll.getChildAt(1)).getText().toString());
            int m = Integer.parseInt(((Button)ll.getChildAt(2)).getText().toString());

            JSONObject obj = new JSONObject();
            try{
                // 寫入 json object
                obj.put("drinkName", name);
                obj.put("Large", l);
                obj.put("Medium", m);
                // 寫入 json array
                order.put(obj);

            }catch(JSONException e){
                e.toString();
            }
        }
        return order;
    }

    public void cancel(View view){

        finish();
    }

}
