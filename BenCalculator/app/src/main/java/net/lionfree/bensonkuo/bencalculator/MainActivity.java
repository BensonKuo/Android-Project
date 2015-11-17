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

    private boolean flag = false;
    String record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calcTextView = (TextView) findViewById(R.id.calcProcessTextView);
    }

    // when pressing btn
    public void btnPress(View view) {


        btn = (Button) findViewById(view.getId());
        // get current input content
        currentInput = btn.getText().toString();

        if (flag) {
            calcTextView.setText("");
            flag = false;
        }
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
            case R.id.two:
                tmpNumber += currentInput;
                Log.d("tmpNumber", tmpNumber);

                calcTextView.setText(record + currentInput);
                break;
            case R.id.three:
                tmpNumber += currentInput;
                Log.d("tmpNumber", tmpNumber);

                calcTextView.setText(record + currentInput);
                break;
            case R.id.four:
                tmpNumber += currentInput;
                Log.d("tmpNumber", tmpNumber);

                calcTextView.setText(record + currentInput);
                break;
            case R.id.five:
                tmpNumber += currentInput;
                Log.d("tmpNumber", tmpNumber);

                calcTextView.setText(record + currentInput);
                break;
            case R.id.six:
                tmpNumber += currentInput;
                Log.d("tmpNumber", tmpNumber);

                calcTextView.setText(record + currentInput);
                break;
            case R.id.seven:
                tmpNumber += currentInput;
                Log.d("tmpNumber", tmpNumber);
                calcTextView.setText(record + currentInput);
                break;
            case R.id.eight:
                tmpNumber += currentInput;
                Log.d("tmpNumber", tmpNumber);

                calcTextView.setText(record + currentInput);
                break;
            case R.id.nine:
                tmpNumber += currentInput;
                Log.d("tmpNumber", tmpNumber);

                calcTextView.setText(record + currentInput);
                break;
            case R.id.zero:
                tmpNumber += currentInput;
                Log.d("tmpNumber", tmpNumber);

                calcTextView.setText(record + currentInput);
                break;


            case R.id.plus:
                if (tmpNumber != "") { // to ensure user inputNumber first
                    inputNumber(tmpNumber); // send tmpNumber
                    inputOperator(currentInput);// send "+"
                    calcTextView.setText(record + currentInput);
                } else {
                    Toast.makeText(this, "Invalid input!", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.minus:
                if (tmpNumber != "") {
                    inputNumber(tmpNumber); // send tmpNumber
                    inputOperator(currentInput);// send "+"
                    calcTextView.setText(record + currentInput);
                } else {
                    Toast.makeText(this, "Invalid input!", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.times:
                if (tmpNumber != "") {
                    inputNumber(tmpNumber); // send tmpNumber
                    inputOperator(currentInput);// send "+"
                    calcTextView.setText(record + currentInput);
                } else {
                    Toast.makeText(this, "Invalid input!", Toast.LENGTH_SHORT).show();
                }
                break;

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

        // clear tmpNumber
        tmpNumber = "";
        i++;
    }

    // when press Equal btn
    public void getResult(View v) {

        inputNumber(tmpNumber);

        int result = 0;
        // check for "x"
        for (int m = 0; m < j; m++) {
            switch (operator[m]) {
                case "x":
                    if (m != 0) {
                        number[m] = number[m] * number[m + 1];
                    }
                    break;
            }
        }

        // math logic
        for (int c = 0; c < j; c++) {
            switch (operator[c]) {
                case "x":
                    if (c == 0) {
                        result += (number[0] * number[1]);
                    }
                    break;
                case "+":
                    if(operator[c+1] != "x") {
                        if (c == 0) {
                            result += (number[0] + number[1]);
                        } else {
                            result += number[c + 1];
                        }
                    }
                    break;
                case "-":
                    if(operator[c+1] != "x") {
                        if (c == 0) {
                            result += (number[0] - number[1]);
                        } else {
                            result -= number[c + 1];
                        }
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
        flag = true;

        // 之後再按下數字按鈕時 可以：
        // 1. 讓user 繼續使用ans運算
        // 2. clear textview  重新開始計算 ～～先做這個～～
    }


}
