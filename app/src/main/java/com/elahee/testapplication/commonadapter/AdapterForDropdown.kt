package com.btracsolutions.yesparking.commonadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.btracsolutions.yesparking.activity.vehicleadd.DropDownType
import com.btracsolutions.yesparking.databinding.RowDorpdownChooserBinding
import com.btracsolutions.yesparking.model.GenericModelForDropdown

class AdapterForDropdown(
    var listener: DropDownListener, val datas: ArrayList<GenericModelForDropdown>, var selectedTable: Int,
    var dropDownType: DropDownType
)
    : RecyclerView.Adapter<AdapterForDropdown.ViewHolder>() {

    interface DropDownListener {
        fun onClickDropDown(position: Int,dropDowntype:String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //context=parent.context
        val binding = RowDorpdownChooserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            val data : GenericModelForDropdown = datas[position]
            binding.textType.text=data._name
            binding.radioButton.setChecked(position == selectedTable)
            binding.llMain.setOnClickListener(View.OnClickListener {
                selectedTable = absoluteAdapterPosition
                notifyDataSetChanged()
                listener.onClickDropDown(selectedTable,dropDownType.name)
            })
            if (position==0){
                binding.view2.visibility=View.INVISIBLE
            }

        }
    }

    override fun getItemCount(): Int {
        return if (datas == null) 0 else datas.size

    }

    inner class ViewHolder(val binding: RowDorpdownChooserBinding) : RecyclerView.ViewHolder(binding.root)


}