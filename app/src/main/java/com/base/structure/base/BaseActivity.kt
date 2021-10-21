package com.base.structure.base

import android.Manifest
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.base.structure.R;
import com.base.structure.mPrefs
import com.base.structure.utils.StaticData
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.BuildConfig
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_otp.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*

public open class BaseActivity : AppCompatActivity(), View.OnClickListener {

  //  val TAG = BaseActivity::class.java.simpleName
    val REQ_IMAGE_PERM = 22
    lateinit var progressDialog: ProgressDialog

    //lateinit var mRewardedVideoAd: RewardedVideoAd

    private val permissionList = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    )
    var mDialog: AlertDialog? = null
    lateinit var db:FirebaseFirestore
    lateinit var handler: Handler
    //lateinit var transferUtility : TransferUtility
   // var isAspect : Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Use an activity context to get the rewarded video instance.
       // mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this)
       // initAwsImageUploading()
        db= FirebaseFirestore.getInstance()
        handler = Handler(Looper.getMainLooper())
    }


    fun initAwsImageUploading(){
        // Initializes TransferUtility, always do this before using it.
      //  transferUtility = AWSFileUploading.getTransferUtility(this@BaseActivity)!!
    }

    /** function will be implemented in Child classes whenever required  **/
    override fun onClick(v: View?) { }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        /*if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            val result = CropImage.getActivityResult(data)
//            if (resultCode == Activity.RESULT_OK) {
//                handleFinalCroppedImage(result.uri)
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Toast.makeText(this, "Cropping failed: " + result.error, Toast.LENGTH_LONG).show()
//            }
//        }*/
//    }

    fun withBody(s: String): RequestBody? {
        return RequestBody.create(MultipartBody.FORM, "" + s)
    }

    open fun getBodyFromFile(f: File, paramName:String): MultipartBody.Part? {
        return if (f == null) null else { //File file=new File(uri.getPath());
            val requestFile =
                RequestBody.create("image/*".toMediaTypeOrNull(), f)
            Log.e("ImageFile size", "" + f.length())
            // RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
            //  RequestBody formBody = new FormBody.Builder().add("search", "Jurassic Park").build();
            MultipartBody.Part.createFormData(paramName, f.name, requestFile)
        }
    }

    fun handleProgressLoader(isLoading: Boolean){
        if (progress_layout==null){
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            return
        }

        if (isLoading) {
            progress_layout.visibility = View.VISIBLE
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }else {
            progress_layout.visibility = View.GONE
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
    fun handleError(error: String){
        if(!TextUtils.isEmpty(error)){
            var message = error
            if (error.equals(StaticData.TIMEOUT)) {
                message =getString(R.string.timeout_error)
            } else if (error.equals(StaticData.UNDEFINED)) {
                message =getString(R.string.undefined_error)
            } else if (error.equals(StaticData.NODATA_FOUND))
                message=getString(R.string.no_data_found)
            showAlertDialog(message, "", "", getString(R.string.ok), null)
        }
    }

    /** In case no network is available, alert the user with a message **/
    fun showNetworkIssue() {

        showAlertDialog(
                getString(R.string.no_internet),
                getString(R.string.retry),
                getString(R.string.ok),
                "",
                DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> retryAction()
                        DialogInterface.BUTTON_NEGATIVE -> failureAccepted()
                    }
                })
    }

    fun showAlertDialog(
            message: String?,
            positive: String,
            negative: String,
            neutral: String,
            listener: DialogInterface.OnClickListener?) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.app_name))
        builder.setMessage(message)
        if (!TextUtils.isEmpty(positive)) {
            builder.setPositiveButton(positive, listener)
        }
        if (!TextUtils.isEmpty(negative)) {
            builder.setNegativeButton(negative, listener)
        }
        if (!TextUtils.isEmpty(neutral)) {
            builder.setNeutralButton(neutral, listener)
        }
        if (mDialog != null) {
            mDialog!!.dismiss()
        }
        mDialog = builder.create()

        mDialog!!.setOnShowListener(object : DialogInterface.OnShowListener {
            override fun onShow(dialog: DialogInterface?) {
                mDialog!!.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this@BaseActivity, R.color.green1));
                mDialog!!.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this@BaseActivity, R.color.black));
            }
        })
        mDialog!!.show()
    }

    /** UserData has opted to retry API calling which failed earlier due to Network unavailability **/
    open fun retryAction() {

    }

    /** UserData has accepted API calling failure due to Network unavailability **/
    open fun failureAccepted() {

    }

    fun hideKeyBoard() {
        try {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(findViewById<View>(android.R.id.content).windowToken, 0)
        } catch (e: Exception) {
            printLog("BaseActivity", e.toString())
        }

    }

    fun openKeyBoard() {
        try {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInputFromWindow(findViewById<View>(android.R.id.content).windowToken, InputMethodManager.SHOW_FORCED, 0)
        } catch (e: Exception) {
            printLog("BaseActivity", e.toString())
        }

    }

  /*  fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        val safeClickListener = SafeClickListener {
            onSafeClick(it)
        }
        setOnClickListener(safeClickListener)
    }*/

    fun printLog(tag: String, log: String){
        if(BuildConfig.DEBUG)
            Log.d(tag, log)
    }

    /** Image Selection Tasks **/

  /*  fun verifyImagePermissions(isAspect : Boolean) {
       // this.isAspect=isAspect
        val requiredPermission = ArrayList<String>()
        for (perm in permissionList) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                requiredPermission.add(perm)
            }
        }
        if (!requiredPermission.isEmpty()) {
            showAlertDialog(getString(R.string.image_perm), "", "", getString(R.string.allow),
                DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        DialogInterface.BUTTON_NEUTRAL -> ActivityCompat.requestPermissions(
                            this,
                            requiredPermission.toTypedArray(),
                            REQ_IMAGE_PERM
                        )
                    }
                })
        } else {
           // continueWithImageTasks()
        }
    }*/



    internal var connectionCallbacks: GoogleApiClient.ConnectionCallbacks =
        object : GoogleApiClient.ConnectionCallbacks {
            override fun onConnected(bundle: Bundle?) {

            }

            override fun onConnectionSuspended(i: Int) {

            }
        }
    internal var connectionFailedListener: GoogleApiClient.OnConnectionFailedListener =
        GoogleApiClient.OnConnectionFailedListener { }

    internal var googleApiClient: GoogleApiClient? = null


//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        if (requestCode == SystemUtils.PERMISSIONS_REQUEST_LOCATION) {
//            if (grantResults.size >= 2) {
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
//                //  staartLocationTracking();
//                    requestLocationDialog()
//            } else {
//                showToast("Permission required for Location")
//            }
//        }
//
//        if (requestCode == REQ_IMAGE_PERM) {
//            val permissionResults = HashMap<String, Int>()
//            var deniedCount = 0
//
//            /** Add all denied permissions  */
//            for (i in grantResults.indices) {
//                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
//                    permissionResults[permissions[i]] = grantResults[i]
//                    deniedCount++
//                }
//            }
//
//            /** all permissions granted  */
//            if (deniedCount == 0) {
//               // continueWithImageTasks()
//            } else {
//                for ((permName, permResult) in permissionResults) {
//                    /** permission denied (first time, when 'Dont ask again' has not been checked).
//                     * ask again, explaining the requirement
//                     * shouldShowRequestPermissionRationale wil return true
//                     */
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permName)) {
//                        showAlertDialog(getString(R.string.image_perm), "", "", getString(R.string.allow),
//                            DialogInterface.OnClickListener { _, which ->
//                                when (which) {
//                                  //  DialogInterface.BUTTON_NEUTRAL -> verifyImagePermissions(isAspect)
//                                }
//                            })
//                    }
//                    /** Permission has been denied and Dont ask again checked shouldShowrequestPermissionRationale will return false **/
//                    else {
//                        showAlertDialog(getString(R.string.permission_alert), "", "", getString(R.string.ok),
//                            DialogInterface.OnClickListener { _, which ->
//                                when (which) {
//                                    DialogInterface.BUTTON_NEUTRAL -> {
//                                        val _intent = Intent(
//                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                                            Uri.fromParts("package", packageName, null)
//                                        )
//                                        _intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                        startActivity(_intent)
//                                        this@BaseActivity.finish()
//                                    }
//                                }
//                            })
//                    }
//                }
//            }
//        }
//    }

    open fun handleFinalCroppedImage(imageUri: Uri) {}

    /*
     * Begins to upload the file specified by the file path to AWS
     */

    open fun fileUploadSuccess(imageKey: String) {}

    open fun fileUploadError(){}

    fun showToast(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun replaceFragment(container: Int, fragment: Fragment, enab_backStack: Boolean) {
        var fragmentTransaction = supportFragmentManager.beginTransaction().replace(container, fragment)
        if (enab_backStack) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()
    }

    fun addFragment(container: Int, fragment: Fragment, enab_backStack: Boolean) {
        var fragmentTransaction = supportFragmentManager.beginTransaction().add(container, fragment)
        if (enab_backStack) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()
    }

    open fun openUpQuestionaireScreen(){

    }

    fun setTimeFromPicker(textView: TextView){
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
        val minute = mcurrentTime[Calendar.MINUTE]
        val picker=TimePickerDialog(textView.context, object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

                val hour = hourOfDay % 12
                textView.setText(java.lang.String.format("%02d:%02d %s", if (hour == 0) 12 else hour,
                        minute, if (hourOfDay < 12) "am" else "pm"))

            }

        }, 7, 30, false)

        picker.setTitle("Setect time")
        picker.show()
    }

    fun setDatePicker(textView: TextView){
        val mcurrentTime = Calendar.getInstance()

        val picker=DatePickerDialog(textView.context, object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                textView.setText("" + dayOfMonth + "/" + month + "/" + year)
            }
        }, mcurrentTime.get(Calendar.YEAR), mcurrentTime.get(Calendar.MONTH), mcurrentTime.get(Calendar.DAY_OF_MONTH))

        picker.setTitle("Setect Date")
        picker.show()
    }

    fun setSpannable(
            textView: TextView,
            message: String,
            matched_string: String,
            color: Int,
            underlineStatus: Boolean /*, setOnClickListener: OnClickListener*/
    ) {

        var spannableStr = SpannableStringBuilder(message)


        var start = message.indexOf(matched_string, 1)
        spannableStr.setSpan(
                StyleSpan(Typeface.BOLD),
                start,
                start + matched_string.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val clickableSpan = object : ClickableSpan() {

            override fun onClick(textView: View) {
                // Toast.makeText(textView.context,"clicked",Toast.LENGTH_LONG).show()
                //  setOnClickListener.onClick(textView)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = underlineStatus
            }
        }
        spannableStr.setSpan(clickableSpan, start, start + matched_string.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

        textView.setLinkTextColor(color)
        textView.setText(spannableStr, TextView.BufferType.SPANNABLE)
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.highlightColor = ContextCompat.getColor(textView.context, android.R.color.transparent)
    }

    fun getRangedArray(from: Int, to: Int, plusOneMore: Boolean) : ArrayList<String>{
        var rangList : ArrayList<String> = ArrayList()
        for (i in from..to){
            rangList.add("" + i);
            if (i==to && plusOneMore)
                rangList.add("" + i + "+")
        }
        return rangList
    }

    fun logOutFromApp(){
        showAlertDialog(getString(R.string.are_you_sure_to_logout), getString(R.string.yes), getString(R.string.no), "", DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    hitLogoutAPI()
                }
            }
        })
    }

    open fun hitLogoutAPI() {

        // any api call is here otherwise restart//
        mPrefs.clearUserDetails()
        restartApp()
    }

    fun restartApp(){
        val i = baseContext.packageManager.getLaunchIntentForPackage(baseContext.packageName)
        i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        finish()
        startActivity(i)
    }
    fun enableLoadingBar(enable: Boolean) {
        if (enable) {
            loadProgressBar(null, getString(R.string.Loading), false)
        } else {
            dismissProgressBar()
        }
    }

    fun showSnackBar(root: View?, msg: String) {
        if (root != null)
            Snackbar.make(root, msg, Snackbar.LENGTH_SHORT).show()
    }


    fun loadProgressBar(title: String?, message: String, cancellable: Boolean) {
        if (progressDialog == null && !this.isFinishing) {
            progressDialog = ProgressDialog.show(this, title, message, false, cancellable)
            progressDialog.setContentView(R.layout.custom_progress)
            progressDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    fun dismissProgressBar() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss()
        }
    }
  /*  fun getLoginType(type : SocialLoginType) : String{
        when(type){
            SocialLoginType.FACEBOOK-> return StaticData.LOGINTYPE_FACEBOOK
            SocialLoginType.GOOGLE-> return StaticData.LOGINTYPE_GOOGLE
            SocialLoginType.TRUECALLER-> return StaticData.LOGINTYPE_TRUECALLER
            else-> return StaticData.LOGINTYPE_MOBILE
        }
    }
*/


    fun setDrawableBackground(layout : View,drawable : Int){
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            layout.setBackgroundDrawable(ContextCompat.getDrawable(this, drawable) )
        } else {
            layout.setBackground(ContextCompat.getDrawable(this, drawable))
        }
    }
}
