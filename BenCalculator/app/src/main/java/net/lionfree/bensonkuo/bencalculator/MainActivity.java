package net.lionfree.bensonkuo.bencalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private TextView CalcTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // when pressing number btn
    public void inputNumber(View view){
        switch (view.getId()) {
            case R.id.one:
                Toast.makeText(this,"one",Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this,"null",Toast.LENGTH_SHORT).show();
        }
    }
}
