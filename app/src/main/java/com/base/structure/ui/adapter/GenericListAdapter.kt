package com.base.structure.ui.adapter

import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.base.structure.BR
import java.util.*

class GenericListAdapter<T : Any>(
    var activity: AppCompatActivity,
    var itemList: ArrayList<T>,
    val layout_id: Int,
    val listItemClickListener: OnListItemClickListener<T>) :
                        RecyclerView.Adapter<GenericListAdapter.GenericViewHolder>(), Filterable {

    var valueFilter: ValueFilter? = null
    var filteredItemList = itemList
   // val viewBinderHelper = ViewBinderHelper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder {
        var binding: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent?.context), layout_id, parent, false)
        return GenericViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return filteredItemList.size
    }

   /* fun setSwipeRevealOpenOnlyOne() {
        viewBinderHelper.setOpenOnlyOne(true)
    }*/

    override fun onBindViewHolder(holder: GenericViewHolder, position: Int) {
        val listItem = filteredItemList.get(position)
        holder.itemView.setTag(position)
        holder?.binding?.setVariable(BR.itemClickListener, listItemClickListener)
        holder?.binding?.setVariable(BR.listitem, listItem)
        holder?.binding?.setVariable(BR.position, position)
      /*  if(listItem is CourseItem){
            val colorResource = if (listItem.isSelected) R.color.blue6 else R.color.grey6
            holder.itemView.txt_course_name.setTextColor(ContextCompat.getColor(holder.itemView.context, colorResource))
            holder.itemView.img_check.visibility = if (listItem.isSelected) View.VISIBLE else View.INVISIBLE
        } else{

        }*/
        holder.binding.executePendingBindings()
    }

    interface OnListItemClickListener<T> {
        fun onListItemClicked(selItem: T, extra:Any?,position: Int)
    }

    override fun getFilter(): Filter {
        if (valueFilter == null) {
            valueFilter = ValueFilter()
        }
        return valueFilter as ValueFilter
    }

    class GenericViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ViewDataBinding
        init {
            this.binding = binding
            this.binding.executePendingBindings()
        }
    }

    inner class ValueFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
            val results = Filter.FilterResults()
            if (constraint != null && constraint.length > 0) {
                val filterList = ArrayList<T>()
                for (i in 0 until filteredItemList.size) {
                    var currentItem = filteredItemList.get(i)
                   /* if(currentItem is ItemCategoryModel)
                    if (currentItem.type.toUpperCase().contains(constraint.toString().toUpperCase())) {
                        filterList.add(filteredItemList.get(i))
                    }*/
                }
                results.count = filterList.size
                results.values = filterList
            } else {
                results.count = filteredItemList.size
                results.values = filteredItemList
            }
            return results
        }

        override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
            filteredItemList = results.values as ArrayList<T>
            notifyDataSetChanged()
        }

    }


    override fun onViewAttachedToWindow(holder: GenericViewHolder) {
        super.onViewAttachedToWindow(holder)

//    if (holder.binding is ItemSearchUserBinding){
//
//       /* (holder.binding as ItemSearchUserBinding).listitem?.observableFollow?.
//        set((holder.binding as ItemSearchUserBinding).listitem?.is_follow!!)
//
//        (holder.binding as ItemSearchUserBinding).listitem?.observableFollower?.
//        set((holder.binding as ItemSearchUserBinding).listitem?.follow_up!!)
//
//        (holder.binding as ItemSearchUserBinding).listitem?.observableFollowing?.
//        set((holder.binding as ItemSearchUserBinding).listitem?.following!!)*/
//
//        }
    }


    fun setLinnearHight(linearLayout: LinearLayout, ratio: Float) {

        val displayMetrics: DisplayMetrics
        val screenHeight: Double
        val screenWidth: Double
        val lhi: Int
        val lwi: Int

        displayMetrics = activity.resources.displayMetrics
        screenHeight = displayMetrics.heightPixels.toDouble()
          screenWidth = displayMetrics.widthPixels.toDouble();

        lhi = (screenHeight * ratio).toInt()
         lwi = (screenWidth * ratio).toInt();

        linearLayout.layoutParams.height = lhi
        linearLayout.layoutParams.width = lwi;
        linearLayout.requestLayout()
    }


}