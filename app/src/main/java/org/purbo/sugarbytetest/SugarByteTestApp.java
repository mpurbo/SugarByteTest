package org.purbo.sugarbytetest;

import android.app.Application;

import com.orm.SugarContext;

/**
 * Created by purbo on 11/21/14.
 */
public class SugarByteTestApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }

}
