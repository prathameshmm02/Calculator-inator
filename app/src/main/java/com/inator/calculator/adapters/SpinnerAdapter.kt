package com.inator.calculator.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.inator.calculator.R
import com.inator.calculator.model.Rate

class SpinnerAdapter(context: Context, resource: Int, private var objects: List<Rate>) :
    ArrayAdapter<Rate>(context, resource, objects) {



    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent, 0)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent, 1)
    }

    override fun getItem(position: Int): Rate {
        return objects[position]
    }


    fun getPosition(name: String): Int {
        return objects.indexOf(
            objects.find {
                it.code == name
            }
        )
    }

    private fun getCustomView(
        position: Int,
        convertView: View?,
        parent: ViewGroup,
        viewType: Int
    ): View {
        var v = convertView
        val holder: ViewHolder

        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.item_spinner, parent, false)

            holder = ViewHolder()
            holder.name = v.findViewById(R.id.spinner_text)
            holder.flag = v.findViewById(R.id.flag)

            v?.tag = holder
        } else {
            holder = v.tag as ViewHolder
        }

        val item = getItem(position)

        holder.name?.text = when (viewType) {
            0 -> item.code
            1 -> "${item.code} - ${item.getName(context)}"
            else -> null
        }
        holder.flag?.setImageDrawable(item.getFlag(context))

        return v!!
    }

    internal class ViewHolder {
        var name: TextView? = null
        var flag: ImageView? = null
    }

}