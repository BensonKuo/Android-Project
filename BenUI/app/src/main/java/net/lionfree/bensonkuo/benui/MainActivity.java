package net.lionfree.bensonkuo.benui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText inputText;
    private CheckBox hideCheckBox;

    private ListView recordListView;
    private Spinner storeSpinner;

    SharedPreferences sp;//讀取用
    SharedPreferences.Editor editor; //編輯用

    private int REQUEST_DRINK_MENU = 1;
    private String drinkMenuResult;

    private ProgressBar progressBar;
    private ProgressDialog progressDialog;  // 不需要layout


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 要在oncreate 才可以啊
        sp = getSharedPreferences("setting", MODE_PRIVATE);
        editor = sp.edit();

        inputText = (EditText) findViewById(R.id.inputText);
        // 監控在input text時的按鍵動作
        inputText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                String text = inputText.getText().toString();
                editor.putString("inputText", text);
                editor.commit();

                // 取得動作是 按下 的話  && 是enter
                if ((event.getAction() == event.ACTION_DOWN) && (keyCode == event.KEYCODE_ENTER)) {
                    submit(null);
                    return true;// event terminate
                }
                return false;// continue event
            }
        });


        hideCheckBox = (CheckBox) findViewById(R.id.hideCheckBox);
        //hideCheckBox.setChecked(false);
        hideCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("hideCheckBox", isChecked);
                editor.apply();
            }
        });


        // restore status (key, deVal)
        inputText.setText(sp.getString("inputText", " "));
        hideCheckBox.setChecked(sp.getBoolean("hideCheckBox", false));


        recordListView = (ListView) findViewById(R.id.recordListView);
        showRecord();

        storeSpinner = (Spinner) findViewById(R.id.storeSpinner);
        setStoreInfo();

        progressDialog = new ProgressDialog(this);
    }


    public void submit(View view) {
        progressDialog.setTitle("Saving...");
        progressDialog.show();

        try {
            String text = inputText.getText().toString();
            if (hideCheckBox.isChecked()) {
                text = "*****";
            }

            // 1. 用來儲存所有資料來 寫入檔案
            JSONObject orderInfo = new JSONObject();
            orderInfo.put("name", text);
            orderInfo.put("storeInfo", storeSpinner.getSelectedItem().toString());
            orderInfo.put("order", new JSONArray(drinkMenuResult));//還原成原本的型別

            // 把物件轉成字串
            String orderInfoStr = orderInfo.toString();
            // write files
            Utils.writeFile(this, "record.txt", orderInfoStr + "\n");
            // read files
            // String OrderCnt = Utils.readFile(this,"record.txt");

            // 2. 存入 parse db
            ParseObject orderInfoObj = new ParseObject("OrderInfo"); //table name
            orderInfoObj.put("name", text);
            orderInfoObj.put("storeInfo", storeSpinner.getSelectedItem().toString());
            if (drinkMenuResult != null) {  //不然未選送出menu內容送出會crash
                orderInfoObj.put("order", new JSONArray(drinkMenuResult));
            }
            orderInfoObj.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    progressDialog.dismiss();
                    // update listview after saved
                    showRecord();
                    Toast.makeText(MainActivity.this, "Saved!", Toast.LENGTH_LONG).show();
                }
            });
            // 送出後清空 inputText, sp
            inputText.setText("");
            editor.putString("inputText", " ");
            editor.commit();
        } catch (JSONException e) {
            e.toString();
        }

    }


    private void showRecord() {
        // 1. ListView ver.
        //String[] data = Utils.readFile(this,"record.txt").split("\n");
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, data);

        // 2. List ver.
        /*String[] rawData = Utils.readFile(this,"record.txt").split("\n");
        List<Map<String, String>> data = new ArrayList<>();
        for (int i=0; i<rawData.length; i++ ){
            try {
                // 把字串還原為物件
                JSONObject obj = new JSONObject(rawData[i]);
                // 取得 值
                String name = obj.getString("name"); //Returns the value mapped by name if it exists
                String storeInfo = obj.getString("storeInfo");
                JSONArray order = obj.getJSONArray("order");

                Map<String, String> item = new HashMap<>();
                item.put("name",name);
                item.put("storeInfo",storeInfo);
                item.put("order", getDrinkNumber(order));

                // 把map加到list中
                data.add(item);

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        */
        // 3. parse obj ver.
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("OrderInfo");
        // List<ParseObject> rawData = null;
//        try{
//            rawData = query.find();  // find all rows causing LAG!!
//            // return value type is List, alike Array.
//        } catch (ParseException e){
//            e.printStackTrace();
//        }
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    orderObjectToListView(objects);
                    recordListView.setVisibility(View.VISIBLE);

                    progressBar = (ProgressBar) findViewById(R.id.progressBar);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void orderObjectToListView(List<ParseObject> rawData) {

        List<Map<String, String>> data = new ArrayList<>();

        for (int i = 0; i < rawData.size(); i++) {
            // different part
            ParseObject pobj = rawData.get(i);

            // the rest are the same
            String name = pobj.getString("name");
            String storeInfo = pobj.getString("storeInfo");
            JSONArray order = pobj.getJSONArray("order");

            Map<String, String> item = new HashMap();
            item.put("name", name);
            item.put("storeInfo", storeInfo);
            item.put("order", getDrinkNumber(order));

            data.add(item);
        }

        // 定義map key跟layout id 的關聯性
        String[] from = new String[]{"name", "storeInfo", "order"};
        int[] to = new int[]{R.id.name, R.id.store_info, R.id.drink_number};

        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.listview_item, from, to);

        // as always the same.
        recordListView.setAdapter(adapter);
    }

    private String getDrinkNumber(JSONArray order) {
        return "17";
    }

    private void setStoreInfo() {
        //String[] store = getResources().getStringArray(R.array.storeInfo);
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("StoreInfo");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                String[] store = new String[objects.size()];

                for (int i = 0; i < objects.size(); i++) {
                    ParseObject pobj = objects.get(i);
                    String name = pobj.getString("name");
                    String address = pobj.getString("address");
                    //存入store array
                    store[i] = name + ", " + address;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, store);
                storeSpinner.setAdapter(adapter);
            }
        });


    }

    public void goToDrinkMenu(View view) {
        String storeInfoStr = storeSpinner.getSelectedItem().toString();

        Intent intent = new Intent();
        intent.setClass(this, DrinkMenuActivity.class);
        intent.putExtra("store_info", storeInfoStr);

        startActivityForResult(intent, REQUEST_DRINK_MENU);
    }

    // 接收回來的intent 跟startActivityForResult(), setResult()是一起的
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == REQUEST_DRINK_MENU) && (resultCode == RESULT_OK)) {
            drinkMenuResult = data.getStringExtra("order");
        }
    }

}
