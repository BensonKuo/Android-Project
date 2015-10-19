package net.lionfree.bensonkuo.bmi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    private TextView bmiText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        bmiText = (TextView)findViewById(R.id.bmiText);
        Double w = Double.parseDouble(getIntent().getStringExtra("weight"));


    }


}
