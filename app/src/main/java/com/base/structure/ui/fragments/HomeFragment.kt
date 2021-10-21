package com.base.structure.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.base.structure.base.BaseFragment
import com.base.structure.databinding.FragmentHomeBinding
import com.base.structure.interceptor.ConnectivityStatus
import com.base.structure.ui.adapter.HomeSliderViewPagerAdapter
import com.base.structure.viewmodel.UserDetailsViewModel
import com.base.structure.viewmodel.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class HomeFragment : BaseFragment() {


    lateinit var fragmentBinding: FragmentHomeBinding

    @Inject
    lateinit var userdetailFactory: ViewModelFactory
    lateinit var mUserViewModel: UserDetailsViewModel



    // This property is only valid between onCreateView and
    // onDestroyView.


    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mUserViewModel = ViewModelProvider(viewModelStore,userdetailFactory).get(UserDetailsViewModel::class.java)

        fragmentBinding = FragmentHomeBinding.inflate(inflater, container, false)

        hideKeyBoard()
        setObservers()

        checkConnection()

        val root: View = fragmentBinding.root
        return root
    }

    private fun checkConnection() {

        if (!ConnectivityStatus.isConnected(activity)) {
            showNetworkIssue()
            return
        }

//        mUserViewModel.getAllPost()
    }


    fun observeHome(){

//        mUserViewModel.getAllPostData().observe(viewLifecycleOwner, Observer {
//            // set Slider
//            setAllPostRecycler(it)
//            Log.e(TAG, "observeHome: "+it )
////            if (it.indexO!=null)
////                setSlider(it.listIterator(ImgArrayItem))
////
//        })


    }

//    fun setAllPostRecycler(res:List<AllPostResponseItem>){
//        fragmentBinding.recyclerViewProduct.adapter= GenericListAdapter<AllPostResponseItem>(
//            activity,
//            res as ArrayList<AllPostResponseItem>,
//            R.layout.items,
//            object : GenericListAdapter.OnListItemClickListener<AllPostResponseItem>{
//                override fun onListItemClicked(
//                    selItem: AllPostResponseItem,
//                    extra: Any?,
//                    position: Int) {
//                    if (extra != null && extra is View) {
//                        when (extra.id) {
//                            R.id.parent -> {
//
////                                val bundle= bundleOf("item" to selItem)
////                                activity.findNavController(R.id.nav_host_fragment_activity_bottom_nav).navigate(R.id.redirect_postdetail,bundle)
//
//                            }
//                        }
//                    }
//                }
//            })
//    }

    fun observeDynamicSlider(){

        if (!ConnectivityStatus.isConnected(activity)) {
            showNetworkIssue()
            return
        }

//        mUserViewModel.getSliderLiveData().observe(viewLifecycleOwner, Observer {
//
//            setSlider(it)
//
////            fragmentBinding.viewPage.adapter=HomeSliderViewPagerAdapter(activity,it)
//
//        })
    }

    fun setSlider(res:List<String>){


        fragmentBinding.viewPage.adapter= HomeSliderViewPagerAdapter(activity,res)
    }

    private fun setObservers(){

        observeExtras()
        observeHome()
        observeDynamicSlider()

    }


    private fun observeExtras() {
        mUserViewModel!!.loading.observe(activity,
            { isLoading -> handleProgressLoader(isLoading!!) })
        mUserViewModel!!.dataLoadError.observe(activity, { s ->
            Log.e(TAG, "onChanged: $s")
            handleError(s)
        })
    }




//    override fun onDestroyView() {
//        super.onDestroyView()
//        fragmentBinding = null
//    }
}