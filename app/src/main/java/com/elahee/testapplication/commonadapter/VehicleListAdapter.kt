package com.btracsolutions.yesparking.commonadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.btracsolutions.yesparking.R
import com.btracsolutions.yesparking.databinding.RowVehicleSelectBinding
import com.btracsolutions.yesparking.model.Vehicle

class VehicleListAdapter(private val vehicleList: ArrayList<Vehicle>,val firstTimeIndex:Int) : RecyclerView.Adapter<VehicleListAdapter.ViewHolder>() {

    var index: Int = firstTimeIndex
    private lateinit var context: Context

    private lateinit var onItemSelectionListener: ItemSelectionListener
    fun setOnItemClickListener(onItemSelectionListener: ItemSelectionListener) {
        this.onItemSelectionListener = onItemSelectionListener
    }

    interface ItemSelectionListener {
        fun onSelection(
            //view: View,
            data: Vehicle
        )
    }

    // inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    inner class ViewHolder(val binding: RowVehicleSelectBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val datax= vehicleList.get(position)
            // holder.setIsRecyclable(false)
            with(holder) {
                if (index == position) {
                    holder.binding.clMain.setBackground(context?.resources?.getDrawable(R.drawable.vehicle_bg_select,null))
                    holder.binding.tvVehicleName.setTextColor(context?.resources?.getColor(R.color.white,null)!!)
                }else{
                    holder.binding.clMain.setBackground(context?.resources?.getDrawable(R.drawable.vehicle_bg_unselect,null))
                    holder.binding.tvVehicleName.setTextColor(context?.resources?.getColor(R.color.colorPrimary,null)!!)

                }
                holder.binding.tvVehicleName.text = datax?.vehicleModel
                holder.binding.clMain.setOnClickListener(View.OnClickListener {
                    index = position
                    notifyDataSetChanged()
                    onItemSelectionListener.onSelection(
                        datax!!
                    )
                })
            }
        } catch (e: Exception) {
            System.out.println(e.message)
        }

//        holder.itemView.textType.text ="${datax?.modeltest?.name} ${datax?.modeltest?.exam_type}"
//        holder.itemView.textViewEmail.text = datax?.modeltest?.exam_in_minutes.toString()

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context=parent.context
        val binding = RowVehicleSelectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return vehicleList.size
    }






}