package nl.dannylamarti.airq;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import timber.log.Timber;

/**
 * Created by danny.lamarti on 10/12/16.
 */

public class App extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());
    }


}
