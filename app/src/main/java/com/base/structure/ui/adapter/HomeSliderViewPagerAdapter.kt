package com.base.structure.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.base.structure.base.BaseActivity
import com.base.structure.databinding.SliderBinding
import com.bumptech.glide.Glide
import com.huanhailiuxin.coolviewpager.CoolViewPager

class HomeSliderViewPagerAdapter(val activity: BaseActivity, val list: List<String>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as ImageView
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding: SliderBinding =
            SliderBinding.inflate(LayoutInflater.from(activity), container, false)
        Glide.with(activity).load(list[position]).into(binding.imageView)
        //binding.setListitem(queList.get(i));

        //binding.setListitem(queList.get(i));
        binding.imageView

        binding.imageView.setOnClickListener {
          /*  val bundel= bundleOf("FeaturedProductItem" to queList[position])
            activity.findNavController(R.id.nav_host_fragment).navigate(R.id.redirect_feature_product,bundel)*/
        }


        (container as CoolViewPager).addView(binding.getRoot(), 0)
        return binding.getRoot()
    }

}