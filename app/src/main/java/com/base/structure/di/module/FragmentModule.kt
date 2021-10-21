package com.base.structure.di.module

import com.base.structure.base.BaseFragment
import com.base.structure.ui.fragments.*

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

   /* @ContributesAndroidInjector
    abstract fun getBaseFragment(): BaseFragment*/

    @ContributesAndroidInjector
    abstract fun getBaseFragment(): BaseFragment

    @ContributesAndroidInjector
    abstract fun getHomeFragment(): HomeFragment



    @ContributesAndroidInjector
    abstract fun getChangePasswordFragment(): ChangePasswordFragment

    @ContributesAndroidInjector
    abstract fun getEditProfileFragment(): EditProfileFragment

    @ContributesAndroidInjector
    abstract fun getAboutUsFragment(): AboutUsFragment

    @ContributesAndroidInjector
    abstract fun getPrivacyPolicyFragment(): PrivacyPolicyFragment








}