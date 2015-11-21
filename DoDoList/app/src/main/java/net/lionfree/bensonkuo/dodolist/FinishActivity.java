package net.lionfree.bensonkuo.dodolist;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FinishActivity extends Fragment {

    private String text = " ";

    @Override
    public void onAttach(Context context){

        super.onAttach(context);

        MainActivity mainActivity = new MainActivity();
        text = mainActivity.getFinish();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle){

        return inflater.inflate(R.layout.activity_finish, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);

        TextView textView = (TextView)getView().findViewById(R.id.finishTextView);
        textView.setText(text);

//        ImageView imageView = (ImageView)getView().findViewById(R.id.FgimageView);
//        imageView.setImageResource(R.drawable.one);
    }
}