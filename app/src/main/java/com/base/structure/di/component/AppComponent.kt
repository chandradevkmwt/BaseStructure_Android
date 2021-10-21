package com.base.structure.di.component

import android.app.Application
import com.base.structure.MyApplication
import com.base.structure.di.module.ActivityModule
import com.base.structure.di.module.ApiModule
import com.base.structure.di.module.FragmentModule
import com.base.structure.di.module.ViewModelModule


import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/*
 * We mark this interface with the @Component annotation.
 * And we define all the modules that can be injected.
 * Note that we provide AndroidSupportInjectionModule.class
 * here. This class was not created by us.
 * It is an internal class in Dagger 2.10.
 * Provides our activities and fragments with given module.
 * */

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class, ApiModule::class, ViewModelModule::class, ActivityModule::class, FragmentModule::class]
)
interface   AppComponent {

    /*
  * We will call this builder interface from our custom Application class -- MyApplication.
  * This will set our application object to the AppComponent.
  * So inside the AppComponent the application instance is available.
  * So this application instance can be accessed by our modules
  * such as ApiModule when needed
  *
  * */

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun apiModule(apiModule: ApiModule): Builder


        fun build(): AppComponent
    }

    /*
     * This is our custom Application class
     * */
    fun inject(appController: MyApplication)

}