package net.lionfree.bensonkuo.dodolist;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FocusActivity extends Fragment {

    private String text = " ";

    @Override
    public void onAttach(Context context){

        super.onAttach(context);

        MainActivity mainActivity = new MainActivity();
        text = mainActivity.getFocus();
        Toast.makeText(getContext(),"1",Toast.LENGTH_SHORT).show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle){
        Toast.makeText(getContext(),"2",Toast.LENGTH_SHORT).show();

        return inflater.inflate(R.layout.activity_focus, container, false);

    }

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);

        TextView textView = (TextView)getView().findViewById(R.id.focusTextView);
        textView.setText(text);

        Toast.makeText(getContext(),"3",Toast.LENGTH_SHORT).show();

        ImageView imageView = (ImageView)getView().findViewById(R.id.focusImageView);
        imageView.setImageResource(R.drawable.one);
    }
}