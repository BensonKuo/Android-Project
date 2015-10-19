package net.lionfree.bensonkuo.bmi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    private TextView bmiText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent it =  getIntent();
        String ws = it.getStringExtra("weight");
        String hs = it.getStringExtra("height");

        bmiText = (TextView)findViewById(R.id.bmiText);
        Double w = Double.parseDouble(ws);
        Double h = Double.parseDouble(hs);
        Double bmi = w/(h*h);

        bmiText.setText(bmi.toString());

        finish();

    }


}
