package ebnrdwan.slider

import alirezat775.sliderview.R
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ebnrdwan.lib.slider.BaseSliderAdapter
import ebnrdwan.lib.slider.ISliderModel
import kotlinx.android.synthetic.main.item_slider.view.*


class SampleAdapter(var onItemClickListener: OnItemClickListener?) : BaseSliderAdapter() {

    private val _emptyItem = 0
    private val _normalItem = 1
    private var sliderPosition = -1

    private var vh: BaseSliderViewHolder? = null

    init {
        enableRefineDimensions(false)
    }

    fun setOnClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItemAtPosition(position)) {
            is EmptySliderModel -> _emptyItem
            else -> _normalItem
        }
    }

    override fun addAll(items: MutableList<ISliderModel>) {
        if (!isRefinedDimensions()) {
            items.add(0, EmptySliderModel())
            items.add(EmptySliderModel())
        }
        super.addAll(items)
    }

    fun setSliderPosition(position: Int) {
        this.sliderPosition = position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseSliderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == _normalItem) {
            val v = inflater.inflate(R.layout.item_slider, parent, false)
            vh = SliderViewHolder(v)
            vh as SliderViewHolder
        } else {
            val v = inflater.inflate(R.layout.item_empty_slider, parent, false)
            vh = EmptyViewHolder(v)
            vh as EmptyViewHolder
        }
    }

    override fun onBindViewHolder(holderBase: BaseSliderViewHolder, position: Int) {
        when (holderBase) {
            is SliderViewHolder -> {
                vh = holderBase
                val model = getItems()[position] as SampleModel
                (vh as SampleAdapter.SliderViewHolder).icon.setImageResource(model.imageId())
                (vh as SampleAdapter.SliderViewHolder).month.text= model.month
                Log.d("onBindViewHolder", "position ${position}|| $sliderPosition refined ${isRefinedDimensions()} ")
                if (position == sliderPosition) {
                    (vh as SampleAdapter.SliderViewHolder).indicator.visibility = View.VISIBLE
                } else (vh as SampleAdapter.SliderViewHolder).indicator.visibility = View.GONE
            }
        }
    }

    inner class SliderViewHolder(itemView: View) : BaseSliderViewHolder(itemView) {

        var icon: ImageView = itemView.imgContinent
        var indicator: View = itemView.selectionIndicator
        var month: TextView = itemView.txtMonth


        init {
            itemView.setOnClickListener {
                onItemClickListener?.onSliderItemClick(
                    adapterPosition,
                    getItemAtPosition(adapterPosition) as SampleModel
                )
            }
        }

    }

    inner class EmptyViewHolder(itemView: View) : BaseSliderViewHolder(itemView) {
    }

    interface OnItemClickListener {
        fun onSliderItemClick(position: Int, model: SampleModel)
    }
}