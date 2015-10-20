package net.lionfree.bensonkuo.bmi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    private TextView bmiText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        //Log.i("123","afaf");

        bmiText = (TextView)findViewById(R.id.bmiText);
        Double w = Double.parseDouble(getIntent().getStringExtra("weight"));
        Double h = Double.parseDouble(getIntent().getStringExtra("height"));
        Double bmi = w/(h*h);

        bmiText.setText(bmi.toString());

        //finish();

    }



    public void reset(View view)(){

    }


}
