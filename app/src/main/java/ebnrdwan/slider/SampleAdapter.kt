package ebnrdwan.slider

import alirezat775.sliderview.R
import ebnrdwan.lib.slider.SliderAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.item_slider.view.*
import kotlinx.android.synthetic.main.item_empty_slider.view.*

class SampleAdapter : SliderAdapter() {

    private val EMPTY_ITEM = 0
    private val NORMAL_ITEM = 1

    private var vh: SliderViewHolder? = null
    var onClick: OnClick? = null

    fun setOnClickListener(onClick: OnClick?) {
        this.onClick = onClick
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItems()[position]) {
            is EmptySampleModel -> EMPTY_ITEM
            else -> NORMAL_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == NORMAL_ITEM) {
            val v = inflater.inflate(R.layout.item_slider, parent, false)
            vh = MyViewHolder(v)
            vh as MyViewHolder
        } else {
            val v = inflater.inflate(R.layout.item_empty_slider, parent, false)
            vh = EmptyMyViewHolder(v)
            vh as EmptyMyViewHolder
        }
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        when (holder) {
            is MyViewHolder -> {
                vh = holder
                val model = getItems()[position] as SampleModel
                (vh as MyViewHolder).icon.setImageResource(model.imageId())
            }
            else -> {
                vh = holder
                val model = getItems()[position] as EmptySampleModel
                (vh as EmptyMyViewHolder).titleEmpty.text = model.getText()
            }
        }
    }

    inner class MyViewHolder(itemView: View) : SliderViewHolder(itemView) {

        var icon: ImageView = itemView.imgContinent

        init {
            icon.setOnClickListener { onClick?.click(getItems()[adapterPosition] as SampleModel) }
        }

    }

    inner class EmptyMyViewHolder(itemView: View) : SliderViewHolder(itemView) {
        var titleEmpty: TextView = itemView.item_empty_text
    }

    interface OnClick {
        fun click(model: SampleModel)
    }
}