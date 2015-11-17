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

    String currentInput;
    String tmpNumber = "";

    int[] number = new int[1024];
    int i = 0;

    String[] operator = new String[1024];
    int j = 0;

    String record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calcTextView = (TextView) findViewById(R.id.calcProcessTextView);
    }

    // when pressing btn
    public void updateRecord(View view) {

        // 把 id 傳給負責計算的function
        // toCalculate(view.getId());

        btn = (Button) findViewById(view.getId());
        // get current input content
        currentInput = btn.getText().toString();

        // 取得目前的 顯示內容
        record = calcTextView.getText().toString();


        switch (view.getId()) {

            case R.id.one:
                // 無法直接從text view 取得string to int後運算 那就分開 顯示＆運算 ！！
                //result = Integer.parseInt("3");// "3+3" is not doable

                // 銜接數字字串 輸入多位數時
                tmpNumber += currentInput;
                Log.d("tmpNumber", tmpNumber);

                //Toast.makeText(this, currentInput, Toast.LENGTH_SHORT).show();
                calcTextView.setText(record + currentInput);
                break;

            case R.id.plus:
                if (tmpNumber != "") {
                    inputNumber(tmpNumber); // send tmpNumber
                    inputOperator(currentInput);// send "+"
                }else{
                    Toast.makeText(this,"Invalid input!",Toast.LENGTH_SHORT).show();
                }

                calcTextView.setText(record + currentInput);
                break;

            case R.id.minus:





            default:
                Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }
    }

    private void inputOperator(String input) {
        operator[j] = input;
        j++;
    }

    private void inputNumber(String input) {
        number[i] = Integer.parseInt(input);

        Log.d("inow", String.valueOf(i));
        Log.d("array0", String.valueOf(number[0]));
        Log.d("array1", String.valueOf(number[1]));
        Log.d("array2", String.valueOf(number[2]));
        Log.d("array3", String.valueOf(number[3]));

        // clear tmpNumber
        tmpNumber = "";
        i++;
    }

    // when press Equal btn
    public void getResult(View v) {

        inputNumber(tmpNumber);

        int result = 0;
        Log.d("c<",String.valueOf(j));

        for (int c = 0; c < j; c++) {
            switch (operator[c]) {
                case "+":
                    if (c == 0) {
                        result += (number[0] + number[1]);
                    } else {
                        result += number[c+1];
                    }
                    break;
            }
        }

        Toast.makeText(this, String.valueOf(result), Toast.LENGTH_SHORT).show();
        calcTextView.setText(String.valueOf(result));

        resetCalc();
    }

    private void resetCalc() {
        // reset index of number and operator to over write
        j = 0;
        i = 0;

        // clear view
        calcTextView.setText("");

        // 之後再按下數字按鈕時 可以：
        // 1. 讓user 繼續使用ans運算
        // 2. clear textview  重新開始計算 ～～先做這個～～
    }


}
