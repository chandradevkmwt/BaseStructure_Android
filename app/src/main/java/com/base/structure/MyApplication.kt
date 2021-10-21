package com.base.structure

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.fragment.app.Fragment
import com.base.structure.di.component.DaggerAppComponent
import com.base.structure.utils.AppPrefs
import com.base.structure.di.module.ApiModule
import com.base.structure.utils.LocaleHelper
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**  Shared preference Instance to be used throughout the app directly **/
val mPrefs: AppPrefs by lazy {
    MyApplication.mPrefs!!
}

//val appUpdateManager: AppUpdateManager by lazy {
//    MyApplication.appUpdateManager
//}
/*
 * we use our AppComponent (now prefixed with Dagger)
 * to inject our Application class.
 * This way a DispatchingAndroidInjector is injected which is
 * then returned when an injector for an activity is requested.
 * */

class MyApplication : Application(), HasActivityInjector , HasSupportFragmentInjector {

    companion object {
        var mPrefs: AppPrefs? = null
//        lateinit var appUpdateManager: AppUpdateManager
        lateinit var mInstance: MyApplication
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun activityInjector(): DispatchingAndroidInjector<Activity>? {
        return dispatchingAndroidInjector
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector;
    }

    override fun onCreate() {
        mPrefs = AppPrefs(this)
        super.onCreate()
        mInstance=this
//        appUpdateManager = AppUpdateManagerFactory.create(this)

DaggerAppComponent
            .builder()
            .application(this)
            .apiModule(ApiModule())
            .build()
            .inject(this)

        /*try{
            applicationContext.startService(Intent(applicationContext, TransferService::class.java))
        }catch (ex : Exception){
            Log.d(MyApplication::class.java.name, "Error = " + ex.toString())
        }*/

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleHelper.setLocale(this, newConfig.locale.getLanguage())
        Log.d(MyApplication::class.simpleName, "onConfigurationChanged: " + newConfig.locale.getLanguage())
    }



}