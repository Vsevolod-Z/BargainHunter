package com.example.bargainhunter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ReviewLayoutManager: LinearLayoutManager {
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout
    ) {
    }
    override fun onMeasure(
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State,
        widthSpec: Int,
        heightSpec: Int
    ) {
        var height = 0
        for (i in 0 until itemCount) {
            val view = recycler.getViewForPosition(i)
            measureChild(view, widthSpec, heightSpec)
            height += getDecoratedMeasuredHeight(view)
            recycler?.recycleView(view)
        }
        setMeasuredDimension(View.MeasureSpec.getSize(widthSpec), height)
        super.onMeasure(recycler, state, widthSpec, heightSpec)
    }
}







