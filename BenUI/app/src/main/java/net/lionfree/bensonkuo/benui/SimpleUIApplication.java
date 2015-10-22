package net.lionfree.bensonkuo.benui;

/**
 * Created by bensonkuo on 2015/10/23.
 */
import android.app.Application;

import com.parse.Parse;

public class SimpleUIApplication extends Application{

    @Override
    public void onCreate(){
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "l8K3M7UPfig6B3Zb5ASooVhxdTtZ54Q1SvLDwWat", "gCINJwGSQDb1DVPOSglGHX20HUUKIOHPBHqpe2Bi");

    }

}