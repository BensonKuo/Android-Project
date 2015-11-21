package net.lionfree.bensonkuo.dodolist;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class UnsolvedFragment extends Fragment {

    private String text = " ";

    @Override
    public void onAttach(Context context){

        super.onAttach(context);

        MainActivity mainActivity = new MainActivity();
        text = mainActivity.getUnsolved();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle){

        return inflater.inflate(R.layout.fragment_unsolved, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);

        TextView textView = (TextView)getView().findViewById(R.id.UnsolveTextView);
        textView.setText(text);

//        ImageView imageView = (ImageView)getView().findViewById(R.id.FgimageView);
//        imageView.setImageResource(R.drawable.one);
    }
}
