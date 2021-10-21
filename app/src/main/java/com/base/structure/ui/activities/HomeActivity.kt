package com.base.structure.ui.activities

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.base.structure.MyApplication.Companion.mPrefs
import com.base.structure.R
import com.base.structure.base.BaseActivity
import com.base.structure.viewmodel.UserDetailsViewModel
import com.base.structure.viewmodel.ViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.navigation.NavigationView
import dagger.android.AndroidInjection
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_drawer.*
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    val bottom_drawerSet =setOf(
        R.id.navigation_home,
        R.id.nav_editProfile,
        R.id.nav_changePassword,
        R.id.nav_aboutUs,
        R.id.nav_privacyPolicy,
        R.id.nav_logOut
    )

//    private lateinit var binding: ActivityBottomNavBinding

    private val PERMISSION_REQUEST_CODE = 100

    lateinit var navView: BottomNavigationView
    lateinit var navController: NavController
    lateinit var drawernavView: NavigationView


    @Inject
    lateinit var userdetailFactory: ViewModelFactory
    lateinit var mUserViewModel: UserDetailsViewModel

    val requestoption= RequestOptions()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        mUserViewModel = ViewModelProvider(viewModelStore,userdetailFactory).get(UserDetailsViewModel::class.java)



        hideKeyBoard()
        setObservers()

        setSupportActionBar(home_toolbar)



        navView = findViewById(R.id.nav_view)

//        binding = ActivityBottomNavBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//         navView= binding.navView

        drawernavView = findViewById(R.id.drawer_nav_view)

         navController = findNavController(R.id.nav_host_fragment_activity_bottom_nav)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_store,  R.id.navigation_sell,R.id.navigation_notifications, R.id.navigation_sms
//            )
//        )

        val appBarConfiguration = AppBarConfiguration(bottom_drawerSet)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        drawernavView.getHeaderView(0).findViewById<ImageView>(R.id.nav_header_changeImage).setOnClickListener(
            View.OnClickListener {

                if (checkPermission()) {
                    showImage()
                } else {
                    requestPermission()
                }

            })

        requestoption.error(R.drawable.user)
        requestoption.placeholder(R.drawable.user)
        //// Drawer///
        val toggle =object : ActionBarDrawerToggle(this, drawer_layout, home_toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)


                drawernavView.getHeaderView(0).findViewById<TextView>(R.id.nav_header_name).text = mPrefs?.prefUserDetails?.name
                drawernavView.getHeaderView(0).findViewById<TextView>(R.id.nav_header_email).text = mPrefs?.prefUserDetails?.email

                val imageView= drawernavView.getHeaderView(0).findViewById<CircleImageView>(R.id.nav_header_img)
                Glide.with(this@HomeActivity).load(""+mPrefs?.prefUserDetails?.profileImage).apply(requestoption).into(imageView)
            }
        }

        drawernavView.getHeaderView(0).findViewById<LinearLayout>(R.id.headerParent).setOnClickListener {
            closeDrawer()
        }


        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        toggle.isDrawerIndicatorEnabled = true
        drawernavView.setupWithNavController(navController)
        //setupActionBarWithNavController(navController,drawer_layout)
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)
        drawernavView.setNavigationItemSelectedListener(this@HomeActivity)
        /*  drawernavView.menu.findItem(R.id.nav_logout).actionView.findViewById<Button>(R.id.nav_logoutBtn).setOnClickListener{
              logOutFromApp()
              drawer_layout.closeDrawer(GravityCompat.START)
          }*/

        home_toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                if (navController.graph.startDestination == navController.currentDestination?.id){

                    if (!drawer_layout.isDrawerOpen(GravityCompat.START)) {
                        drawer_layout.openDrawer(GravityCompat.START)
                    }

                }else {
                    onBackPressed()
                }
            }
        })

        fab.setOnClickListener {
           // navController.navigate(R.id.navigation_sell)
        }

    }

    fun closeDrawer(){
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }



    fun observeNavDrawer(){
        mUserViewModel?.getNavDrawerLiveData()?.observe(this, Observer { resource ->



//            setTicker(resource.nfcount!!)
//            setSmsCount(resource.smsCount!!)

            drawernavView.getHeaderView(0).findViewById<TextView>(R.id.nav_header_email).text = mPrefs?.prefUserDetails?.mobile.toString()

            val imageView= drawernavView.getHeaderView(0).findViewById<CircleImageView>(R.id.nav_header_img)
            Glide.with(this@HomeActivity).load(""+mPrefs?.prefUserDetails?.profileImage).apply(requestoption).into(imageView)

        })

    }


    fun setTicker(number: Int) {
        if (number>0) {
            val notificationBadge: BadgeDrawable =
                navView.getOrCreateBadge(R.id.navigation_notifications)
            if (number <= 0) {
                notificationBadge.isVisible = false
                notificationBadge.clearNumber()
            } else {
                notificationBadge.isVisible = true
                notificationBadge.backgroundColor = Color.BLUE
                notificationBadge.badgeTextColor = Color.YELLOW
                notificationBadge.number = number
            }
        }
    }

    fun setSmsCount(number: Int) {
//        if (number>0) {
//            val notificationBadge: BadgeDrawable =
//                navView.getOrCreateBadge(R.id.navigation_sms)
//            if (number <= 0) {
//                notificationBadge.isVisible = false
//                notificationBadge.clearNumber()
//            } else {
//                notificationBadge.isVisible = true
//                notificationBadge.backgroundColor = Color.BLUE
//                notificationBadge.badgeTextColor = Color.YELLOW
//                notificationBadge.number = number
//            }
//        }
    }


    private fun setObservers(){
        observeExtras()
        observeNavDrawer()
        observeEditProfilePicture()
    }

    private fun observeEditProfilePicture() {
//        mUserViewModel?.getEditProfileImageLiveData()?.observe(this, Observer {
//
//
//            val imageView= drawernavView.getHeaderView(0).findViewById<CircleImageView>(R.id.nav_header_img)
//            Glide.with(this@BottomNavActivity).load(it.profileImage).into(imageView)
//
//
//        })
    }

    private fun showImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                val imageurl = data!!.data

                //                    pictureUri.add(imageurl);
                val filePath = arrayOf(MediaStore.Images.Media.DATA)
                val c: Cursor? = getContentResolver()?.query(imageurl!!, filePath, null, null, null)
                c?.moveToFirst()
                val columnIndex = c?.getColumnIndex(filePath[0])
                val picturePath = c?.getString(columnIndex!!)
                c?.close()


            val imageView= drawernavView.getHeaderView(0).findViewById<CircleImageView>(R.id.nav_header_img)
            Glide.with(this@HomeActivity).load(picturePath).apply(requestoption).into(imageView)
                callEditProfilePic(picturePath.toString())

                Log.w("path of image", picturePath + "")

            }
        }
    }

    private fun callEditProfilePic(picturePath: String?) {

        var file: File = File(picturePath)
        var image:MultipartBody.Part = getBodyFromFile(file,"image")!!

        var userId = withBody(mPrefs?.prefUserDetails?.id.toString())!!

//        mUserViewModel.editProfileImageCall(userId,image)

    }


    fun observeExtras() {
        mUserViewModel.loading.observe(this, Observer { isLoading ->
            handleProgressLoader(isLoading)
        })
        mUserViewModel.dataLoadError.observe(this, Observer { error ->
            handleError(error)
        })
    }




    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){


            R.id.nav_editProfile->{
                navController.navigate(R.id.navigation_edit_profile)
                closeDrawer()
            }

            R.id.nav_aboutUs->{
                navController.navigate(R.id.navigation_aboutUs)
                closeDrawer()
            }

            R.id.nav_changePassword->{
                navController.navigate(R.id.navigation_change_password)
                closeDrawer()
            }

            R.id.nav_privacyPolicy->{
                navController.navigate(R.id.navigation_privacyPolicy)
                closeDrawer()
            }

            R.id.nav_logOut->{logOutFromApp()}
        }
        return true
    }

    private fun checkPermission():Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            return false
        }
        return true
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT)
                    .show()
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)
                // main logic
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT)
                    .show()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            )
                        ) {
                            showAlertDialog("You need to allow access permissions", "OK", "Cancel",
                                "", DialogInterface.OnClickListener() { dialog, which ->
                                    when (which) {
                                        DialogInterface.BUTTON_POSITIVE -> {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission()
                                                dialog.dismiss()
                                            }
                                        }
                                        DialogInterface.BUTTON_NEGATIVE -> {
                                            dialog.dismiss()
                                        }
                                    }
                                })
                        } else {
                            // requestPermission();
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", getPackageName(), null)
                            intent.data = uri
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}
