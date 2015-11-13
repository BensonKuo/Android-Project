package net.lionfree.bensonkuo.bencalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private TextView calcTextView;
    private int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calcTextView = (TextView) findViewById(R.id.calcProcessTextView);

    }

    // when pressing number btn
    public void inputNumber(View view) {

        btn = (Button) findViewById(view.getId());

        // 取得現在的計算過程
        String record = calcTextView.getText().toString();


        switch (view.getId()) {
            case R.id.one:
                // 無法直接從textview 取得string to int後運算
                // 那就分開紀 顯示 跟 運算 ！！

                //result = Integer.parseInt("3");// "3+3" is not doable
                //Log.d("debug", String.valueOf(result));

                Toast.makeText(this, btn.getText(), Toast.LENGTH_SHORT).show();
                calcTextView.setText(record + btn.getText());

                break;


            default:
                Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }
    }
}
