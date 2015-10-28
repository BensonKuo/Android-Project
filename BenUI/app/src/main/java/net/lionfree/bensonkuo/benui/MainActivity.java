package net.lionfree.bensonkuo.benui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {

    private EditText inputText;
    private CheckBox hideCheckBox;

    private ListView recordListView;
    private Spinner storeSpinner;

    SharedPreferences sp;//讀取用
    SharedPreferences.Editor editor; //編輯用

    private int REQUEST_DRINK_MENU = 1;
    private String drinkMenuResult;

    private int REQUEST_TAKE_PHOTO = 2;
    private ImageView photoImageView;

    private ProgressBar progressBar;
    private ProgressDialog progressDialog;  // 不需要layout

    private Boolean hasPhoto = false;

    private List<ParseObject> queryResult;


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
        recordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToOrderDetail(position);
            }
        });

        storeSpinner = (Spinner) findViewById(R.id.storeSpinner);
        setStoreInfo();

        progressDialog = new ProgressDialog(this);

        photoImageView = (ImageView) findViewById(R.id.photo);
    }


    public void submit(View view) {
        progressDialog.setTitle("Saving to DB...");
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
            if (drinkMenuResult != null) {
                orderInfo.put("order", new JSONArray(drinkMenuResult));//還原成原本的型別
            }
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

            if (hasPhoto) {
                Uri uri = Utils.getPhotoURI();
                ParseFile pfile = new ParseFile("photo.png", Utils.uriToBytes(this, uri));
                orderInfoObj.put("files", pfile);
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
                    queryResult = objects;

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
//        //order.length() == 2;
//        int drinkNumber = 0;
//        //Log.d("oolllll", String.valueOf(order.length()));
//        try {
//            for (int i = 0; i < 2; i++) {
//                    int l = order.getJSONObject(i).getInt("Large");
//                    int m = order.getJSONObject(i).getInt("Medium");
//                    drinkNumber +=  (l + m);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return String.valueOf(drinkNumber);
        return "23";
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


    public void goToOrderDetail(int position) {

        ParseObject pobj = queryResult.get(position);
        String storeInfo = pobj.getString("storeInfo");

        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra("storeInfo", storeInfo);

        intent.putExtra("drink",drinkMenuResult);

        startActivity(intent);
    }

    // 接收回來的intent 跟startActivityForResult(), setResult()是一起的
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == REQUEST_DRINK_MENU) && (resultCode == RESULT_OK)) {
            drinkMenuResult = data.getStringExtra("order");
        } else if ((requestCode == REQUEST_TAKE_PHOTO) && (resultCode == RESULT_OK)) {
            //Bitmap bm = data.getParcelableExtra("data");
            //photoImageView.setImageBitmap(bm);
            Uri uri = Utils.getPhotoURI();
            photoImageView.setImageURI(uri);
            hasPhoto = true;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_take_photo) {
            goToCamera();
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToCamera() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        // why exists?
        // 跟camera activity說拍成功的照片要存的路徑 不然不會存檔
        // 但是在onactivityresult的地方是無法存取的！
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Utils.getPhotoURI());
        //The name of the Intent-extra used to indicate a content resolver
        // Uri to be used to store the requested image or video
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

}
