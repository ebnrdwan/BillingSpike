package ebnrdwan.lib.slider

import ebnrdwan.lib.slider.helper.ViewHelper
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.util.*


abstract class SliderAdapter : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    companion object {
        const val REMOVE = 1
        const val ADD = 2
    }

    private lateinit var recyclerView: RecyclerView
    private var enableSlider = false
    private var items: MutableList<ISliderModel> = ArrayList()

    private fun imageOption(view: View) {
        view.layoutParams.width = Math.round(ViewHelper.getScreenWidth().toDouble()).toInt()
        view.layoutParams.height = Math.round(view.layoutParams.width * 0.60).toInt()
        view.requestLayout()
    }

    override fun getItemCount(): Int {
        return items.size
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

    fun operation(item: ISliderModel, operation: Int) {
        when (operation) {
            ADD -> add(item)
            REMOVE -> remove(item)
        }
    }

    fun addAll(items: MutableList<ISliderModel>) {
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

    private fun isEnableSlider(): Boolean {
        return enableSlider
    }

    fun enableSlider(enableSlider: Boolean) {
        this.enableSlider = enableSlider
    }

    open inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            if (isEnableSlider()) imageOption(itemView)
        }
    }
}