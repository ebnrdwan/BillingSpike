package ebnrdwan.slider.vfGraph

import alirezat775.sliderview.R
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import ebnrdwan.lib.slider.BaseSliderAdapter
import ebnrdwan.lib.slider.ISliderModel

import kotlinx.android.synthetic.main.item_slider.view.*
import kotlin.math.abs
import kotlin.math.roundToInt


class SampleAdapter(var onItemClickListener: OnItemClickListener?,val baseLineMargin: Int) :
    BaseSliderAdapter() {

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
            GraphViewHolder(v, isRefinedDimensions(),baseLineMargin)

        } else {
            val v = inflater.inflate(R.layout.item_empty_slider, parent, false)
            EmptyViewHolder(v)
        }
    }

    override fun onBindViewHolder(holderBase: BaseSliderViewHolder, position: Int) {
        when (holderBase) {
            is GraphViewHolder -> {
                (holderBase as? GraphViewHolder)?.bind(getItems()[position], sliderPosition)
            }
        }
    }

    inner class GraphViewHolder(itemView: View, isRefinedDimensions: Boolean,val margin:Int) :
        BaseSliderViewHolder(itemView, isRefinedDimensions) {

        var bar: ImageView = itemView.imgContinent
        var indicator: View = itemView.selectionIndicator
        var month: TextView = itemView.txtMonth

        override fun bind(model: ISliderModel, sliderPosition: Int) {
            itemView.barBaseLine.setMargins(bottom = margin)
            if (model is GraphModel) {
                setItemData(model)
                setItemListener(model)
            }
            setIndicatorState(sliderPosition)
        }

        private fun setIndicatorState(sliderPosition: Int) {
            if (adapterPosition == sliderPosition) {
                month.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorAccent))
                indicator?.visibility = View.VISIBLE
            } else {
                month.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorPrimary))

                indicator?.visibility = View.GONE
            }
        }

        private fun setItemData(model: GraphModel) {
           bar.setHeight(model.height)
            Log.d("setItemData", "setItemData: ${model.height}")
            bar?.setImageResource(model.imageId())
            month?.text = model.month
        }

        private fun setItemListener(model: ISliderModel) {
            itemView.setOnClickListener {
                onItemClickListener?.onSliderItemClick(
                    adapterPosition,
                    model as GraphModel
                )
            }
        }


    }

    inner class EmptyViewHolder(itemView: View) : BaseSliderViewHolder(itemView, false) {
        override fun bind(model: ISliderModel, sliderPosition: Int) {

        }
    }

    interface OnItemClickListener {
        fun onSliderItemClick(position: Int, model: GraphModel)
    }
}