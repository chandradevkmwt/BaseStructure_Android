package com.base.structure.ui.adapter

import android.graphics.Paint
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.annotation.NonNull
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.base.structure.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import de.hdodenhof.circleimageview.CircleImageView

class DataBindingAdapters {

    companion object {

//        @JvmStatic
//        @BindingAdapter("bind:loadCompressimage")
//        fun loadCompressimage(view: ImageView, url: String?) {
//            val requestOptions:RequestOptions = RequestOptions()
//            requestOptions.placeholder(R.drawable.logo)
//            requestOptions.error(R.drawable.logo)
//            requestOptions.dontTransform()
//            requestOptions.override(480,300)
//            requestOptions.override(300)
//
//            Glide.with(view.context.applicationContext).asBitmap().load("" + url)
//                    .apply(requestOptions)
//                .into(view)
//        }

        @JvmStatic
        @BindingAdapter("bind:loadimage")
        fun loadimage(view: ImageView, url: String?) {

            val requestOptions:RequestOptions = RequestOptions()
            requestOptions.centerCrop();
            requestOptions.placeholder(R.drawable.pvcsplash)
            requestOptions.error(R.drawable.pvcsplash)
//            requestOptions.dontTransform()
            Log.e("image url", "loadimage: "+url )
            Glide.with(view.context.applicationContext).load("" + url)
                .apply(requestOptions)
                .into(view)
        }

        @JvmStatic
        @BindingAdapter("bind:loadProfileimage")
        fun loadProfileimage(view: CircleImageView, url: String?) {

            val requestOptions:RequestOptions = RequestOptions()
            requestOptions.placeholder(R.drawable.user)
            requestOptions.error(R.drawable.user)
            requestOptions.dontTransform()
            Glide.with(view.context.applicationContext).load("" + url)
                .apply(requestOptions)
                .into(view)
        }

        @BindingAdapter("bind:setCircularImage")
        fun setCircularImage(thumbnails: ImageView, url: String) {

            val requestOptions = RequestOptions()
//            requestOptions.placeholder(R.drawable.logo)
//            requestOptions.error(R.drawable.logo)
            // requestOptions.transforms(RequestOptions.circleCropTransform());

            Glide.with(thumbnails.context.applicationContext)
                .setDefaultRequestOptions(requestOptions).load(url)
                .apply(RequestOptions.circleCropTransform()).into(thumbnails)
        }

        @JvmStatic
        @BindingAdapter("bind:setStrikeText")
        fun setStrikeText(textView: TextView, data: String) {
            textView.setText(data)
            textView.setPaintFlags(textView.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
        }

        @JvmStatic
        @BindingAdapter("bind:load_ResourceImage")
        public fun load_ResourceImage(@NonNull imageView : ImageView, @NonNull res : Int?){
            if (res!=-1)
                Glide.with(imageView.context).load(res).into(imageView)
        }

        @JvmStatic
        @BindingAdapter("bind:setHtmlText")
        fun setHtmlText(textView: TextView, data: String?) {
            if (!TextUtils.isEmpty(data)) {
                textView.text = HtmlCompat.fromHtml(data!!, HtmlCompat.FROM_HTML_MODE_COMPACT)
            }
        }


        @JvmStatic
        @BindingAdapter("bind:setStringSpinner")
        fun setStringSpinner(spinner: Spinner, data: List<String>) {
            if (!data.isNullOrEmpty()) {
                spinner.adapter=object : ArrayAdapter<String>(spinner.context,R.layout.support_simple_spinner_dropdown_item,data){

                }
            }
        }

//        @JvmStatic
//        @BindingAdapter("bind:setVarientSpinner")
//        fun setVarientSpinner(spinner: Spinner, data: List<VarientItem?>?) {
//            if (!data.isNullOrEmpty()) {
//
//                val layoutInflater=LayoutInflater.from(spinner.context)
//                val dataAdapter: ArrayAdapter<VarientItem> = object : ArrayAdapter<VarientItem>(spinner.context,
//                    android.R.layout.simple_spinner_dropdown_item, data) {
//                    override fun getView(
//                        position: Int,
//                        convertView: View?,
//                        parent: ViewGroup
//                    ): View {
//                        // return super.getView(position, convertView, parent);
//                        var convertView = convertView
//                        if (convertView == null) {
//                            convertView = layoutInflater.inflate(R.layout.item_dropdown, parent, false)
//                        }
//                        val rowItem: VarientItem? = getItem(position)
//                        val txtTitle =
//                            convertView!!.findViewById<View>(R.id.text1) as TextView
//                        txtTitle.setText(rowItem?.size!!)
//                        return convertView!!
//                    }
//
//                    override fun setDropDownViewResource(resource: Int) {
//                        super.setDropDownViewResource(resource)
//                    }
//
//                    override fun getDropDownView(
//                        position: Int,
//                        convertView: View?,
//                        parent: ViewGroup
//                    ): View {
//                        var convertView = convertView
//                        if (convertView == null) {
//                            convertView = layoutInflater.inflate(R.layout.item_dropdown, parent, false)
//                        }
//                        val rowItem: VarientItem? = getItem(position)
//                        val txtTitle =
//                            convertView!!.findViewById<View>(R.id.text1) as TextView
//                        txtTitle.setText(rowItem?.size)
//                        return convertView!!
//                        // return super.getDropDownView(position, convertView, parent);
//                    }
//                }
//
//                dataAdapter.setDropDownViewResource(R.layout.item_dropdown)
//                spinner.adapter = dataAdapter
//
//                }
//            }
//
//
//        @BindingAdapter("bind:setLinnearHight")
//        fun setLinnearHight(linearLayout: LinearLayout, isset: Boolean) {
//
//            if (isset) {
//                val displayMetrics: DisplayMetrics
//                val screenHeight: Double
//                val screenWidth: Double
//                val lhi: Int
//                val lwi: Int
//
//                displayMetrics = linearLayout.context.resources.displayMetrics
//                screenHeight = displayMetrics.heightPixels.toDouble()
//                //  screenWidth = displayMetrics.widthPixels;
//
//                lhi = (screenHeight * 0.08).toInt()
//                // lwi = (int) (screenWidth * 0.02);
//
//                linearLayout.layoutParams.height = lhi
//                //   toolbar.getLayoutParams().width = lwi;
//                linearLayout.requestLayout()
//
//            }
//        }
//
//        @BindingAdapter("bind:setRelativeHight")
//        fun setRelativeHight(relativeLayout: RelativeLayout, isset: Boolean) {
//
//            if (isset) {
//                val displayMetrics: DisplayMetrics
//                val screenHeight: Double
//                val screenWidth: Double
//                val lhi: Int
//                val lwi: Int
//
//                displayMetrics = relativeLayout.context.resources.displayMetrics
//                screenHeight = displayMetrics.heightPixels.toDouble()
//                //  screenWidth = displayMetrics.widthPixels;
//
//                lhi = (screenHeight * 0.12).toInt()
//                // lwi = (int) (screenWidth * 0.02);
//
//                relativeLayout.layoutParams.height = lhi
//                //   toolbar.getLayoutParams().width = lwi;
//                relativeLayout.requestLayout()
//
//            }
//        }
//
//
//       /* @JvmStatic
//        @BindingAdapter("bind:setSubjectGroup")
//        fun setSubjectGroup(view: RadioGroup, list:List<SubjectOperation>) {
//            if (!list.isNullOrEmpty()){
//                list.forEach(){
//                    // can also inflat by xml file // radio button should be parent view
////RadioButton radioButtonView = (RadioButton) getLayoutInflater().inflate(R.layout.radio_button, null, false);
//                    val rb = RadioButton(view.context)
//                    val params = RadioGroup.LayoutParams(view.context, null)
//                    params.setMargins(10, 2, 10, 2)
//                    rb.layoutParams = params
//                    rb.setPadding(35, 2, 35, 2)
//                    rb.id = it.subjectId.toInt()
//                    rb.setText(it.subjects)
//                    rb.gravity = Gravity.CENTER
//                    rb.background = ContextCompat.getDrawable(view.context,R.drawable.exam_radio_selector)
//
//                    val colorStateList = ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)), intArrayOf(
//                            Color.BLACK //disabled
//                            , Color.BLUE //enabled
//                    ))
//
//                    rb.setTextColor(colorStateList)
//                    rb.buttonDrawable = null
//
//                    view.addView(rb)
//                }
//
//               val rb= view.getChildAt(0) as RadioButton
//                rb.isChecked=true
//            }
//        }*/


    }

}
