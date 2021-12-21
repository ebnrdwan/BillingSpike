package ebnrdwan.lib.slider

import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import ebnrdwan.lib.slider.helper.ViewHelper
import java.util.*
import kotlin.math.roundToInt


abstract class BaseSliderAdapter : RecyclerView.Adapter<BaseSliderAdapter.BaseSliderViewHolder>() {

    companion object {
        const val REMOVE = 1
        const val ADD = 2
    }

    private lateinit var recyclerView: RecyclerView
    private var refineDimensions = false
    var circularSlider = false
    private var items: MutableList<ISliderModel> = ArrayList()

    private fun refineViewWidth(view: View) {
        view.layoutParams.width = (ViewHelper.getScreenWidth().toDouble() / 2.8).roundToInt()
        view.requestLayout()
    }

    override fun getItemCount(): Int {
        return if (circularSlider) Int.MAX_VALUE
        else items.size
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    /**
     * @return list items
     */
    fun getItems(): MutableList<ISliderModel> {
        return items
    }

    fun getItemAtPosition(position: Int): ISliderModel {
        return if (circularSlider && position >= items.size) {
            items[(position) % items.size]
        } else {
            items[position]
        }
    }

    fun operation(item: ISliderModel, operation: Int) {
        when (operation) {
            ADD -> add(item)
            REMOVE -> remove(item)
        }
    }

    @CallSuper
    open fun addAll(items: MutableList<ISliderModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    private fun add(item: ISliderModel) {
        recyclerView.post {
            notifyItemInserted(itemCount - 1)
            getItems().add(item)
        }
    }

    private fun remove(item: ISliderModel) {
        notifyItemRemoved(getItems().indexOf(item))
        getItems().remove(item)
    }


     fun isRefinedDimensions(): Boolean {
        return refineDimensions
    }

    fun enableRefineDimensions(enableSlider: Boolean) {
        this.refineDimensions = enableSlider
    }

    abstract inner class BaseSliderViewHolder(itemView: View,isRefinedDimensions :Boolean) : RecyclerView.ViewHolder(itemView) {
        private val itemWidthRatio = 2.8;

        init {
            if (isRefinedDimensions) refineViewWidth(itemView)
        }

        private fun refineViewWidth(view: View) {
            view.layoutParams.width =
                (ViewHelper.getScreenWidth().toDouble() / itemWidthRatio).roundToInt()
            view.requestLayout()
        }

        abstract fun bind(model: ISliderModel, sliderPosition:Int)


    }
}