package han.kunpeng.navigator;

import android.app.Application;

import han.kunpeng.navigator.util.Trace;

public class NavigatorApp extends Application {
    @Override public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Trace.plant(new Trace.DebugTree());
        } else {
            Trace.plant(new Trace.ReleaseTree());
        }
    }

}
