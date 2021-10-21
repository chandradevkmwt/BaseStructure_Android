package com.base.structure.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.base.structure.MyApplication.Companion.mPrefs
import com.base.structure.R
import com.base.structure.base.BaseActivity

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_splash)
        Handler().postDelayed(Runnable { redirect() }, 1000L)

    }

    fun redirect() {

        if(mPrefs?.prefUserDetails != null){
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("userId", mPrefs!!.prefUserDetails!!.id)
            startActivity(intent)
            finish()
        }else {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}