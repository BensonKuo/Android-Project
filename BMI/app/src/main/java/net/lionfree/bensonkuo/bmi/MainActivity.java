package net.lionfree.bensonkuo.bmi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText weightText;
    private EditText heightText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void calc(View view){
        heightText = (EditText)findViewById(R.id.height);
        weightText = (EditText)findViewById(R.id.weight);
        //Log.i("click","CLICK~~~");
        Intent intent = new Intent(this, ResultActivity.class);
        //intent.setClass();
        intent.putExtra("weight", weightText.getText().toString());
        intent.putExtra("height", heightText.getText().toString());
        // 單向的Intent
        startActivity(intent);
    }

    @Override
    protected void onRestart(){
        super.onRestart();

        heightText = (EditText)findViewById(R.id.height);
        weightText = (EditText)findViewById(R.id.weight);

        heightText.setText("");
        weightText.setText("");

    }
}
