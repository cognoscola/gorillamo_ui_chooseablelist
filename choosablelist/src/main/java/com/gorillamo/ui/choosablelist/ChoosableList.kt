package com.gorillamo.ui.choosablelist

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper


class ChoosableList :RecyclerView{

    lateinit var values:IntArray
    lateinit var names:Array<String>
    /**
     * When setting the startingPosition by code, make sure to only place it in the Oncreate method
     * of an Activity or a Fragment. Otherwise this value will
     */
    private var startingPosition:Int = 0

    private lateinit var snapHelper: DetectableLinearSnapHelper
    private var itemClickCallback:((Int)->Unit)? = null
    private var snaphelperCallback:((Int)->Unit) = {
        itemClickCallback?.invoke(values[it])
    }


    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        setupAttributes(attrs)
        setupView()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
        setupAttributes(attrs)
        setupView()
    }

    fun setStartingPosition(startingPosition:Int){
        this.startingPosition = startingPosition
    }

    private fun setupAttributes(attrs: AttributeSet){

        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.ChoosableList,
            0, 0)
        val namesId = typedArray.getResourceId(R.styleable.ChoosableList_names, 0);
        val valuesId = typedArray.getResourceId(R.styleable.ChoosableList_values, 0);

        startingPosition = typedArray.getInt(R.styleable.ChoosableList_startingPosition,0)
        names = resources.getStringArray(namesId)
        values = resources.getIntArray(valuesId)

        if (names.size != values.size) {
            throw IllegalArgumentException("Size of Names Array and Values Array must match!")
        }
        if (names.size ==  0) {
            throw IllegalArgumentException("Did you forget to specify Names attribute?")
        }
        if (values.size ==  0) {
            throw IllegalArgumentException("Did you forget to specify values attribute?")
        }

        typedArray.recycle()

    }

    fun setItemClickcallback(itemClickCallback:((Int)->Unit)){
        this.itemClickCallback= itemClickCallback
        this.itemClickCallback?.invoke(values[startingPosition])

    }

    private fun setupView(){

        snapHelper = DetectableLinearSnapHelper(this,snaphelperCallback)
        layoutManager =  object: GridLayoutManager(context, 1){

            override fun onLayoutCompleted(state: RecyclerView.State?) {
                super.onLayoutCompleted(state)
                //Scroll to the desired position
                scrollToPosition(startingPosition)
                post{ findViewByPosition(startingPosition)?.let { snapToPosition(snapHelper,it) } }
            }
        }.apply {
            orientation = GridLayoutManager.HORIZONTAL
        }

        snapHelper.attachToRecyclerView(this)
        adapter = SimplePickerAdapter(names){view -> snapToPosition(snapHelper,view) }

        addItemDecoration(OffsetItemDecoration(context.getSystemService(Context.WINDOW_SERVICE) as WindowManager))
    }

    private fun snapToPosition(snapHelper: SnapHelper, view: View){
        val snapDistance = snapHelper.calculateDistanceToFinalSnap(layoutManager!!, view)
        if (snapDistance!![0] != 0 || snapDistance[1] != 0) {
            this.smoothScrollBy(snapDistance[0], snapDistance[1])
        }
    }
}

