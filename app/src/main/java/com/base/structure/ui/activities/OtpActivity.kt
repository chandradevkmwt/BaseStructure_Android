package com.base.structure.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.base.structure.R
import com.base.structure.databinding.OtpBinding
import com.base.structure.viewmodel.UserDetailsViewModel
import com.base.structure.viewmodel.ViewModelFactory
import dagger.android.AndroidInjection
import javax.inject.Inject

class OtpActivity : AppCompatActivity() {

    lateinit var otpBinding:OtpBinding

    @Inject
    lateinit var userdetailFactory: ViewModelFactory
    lateinit var mUserViewModel: UserDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        otpBinding = DataBindingUtil.setContentView(this, R.layout.activity_otp)

        otpBinding.otpVerify.setOnClickListener(View.OnClickListener {

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()

        })
    }
}