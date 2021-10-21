package com.base.structure.di.module

import com.base.structure.ui.activities.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun getSplashActivity(): SplashActivity

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun getLoginActivity(): SignInActivity

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun getOtpActivity(): OtpActivity

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun getSignUpActivity(): SignUpActivity

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun getForgotPasswordActivity(): ForgotPasswordActivity

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun getBottomNavActivity(): HomeActivity


}