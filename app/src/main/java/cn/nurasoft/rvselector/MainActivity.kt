package cn.nurasoft.rvselector

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.selection.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var tracker: SelectionTracker<Long>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myList = listOf(
                Person("小李", "555-0111"),
                Person("小苏", "555-0119"),
                Person("小娜", "555-0141"),
                Person("旺旺", "555-0155"),
                Person("悟空", "555-0180"),
                Person("八戒", "555-0145")
        )

        my_rv.layoutManager = LinearLayoutManager(this)
        my_rv.setHasFixedSize(true)

        val adapter = MyAdapter(myList, this)
        my_rv.adapter = adapter

        tracker = SelectionTracker.Builder<Long>(
                "selection-1",
                my_rv,
                StableIdKeyProvider(my_rv),
                MyLookup(my_rv),
                StorageStrategy.createLongStorage()
        ).withSelectionPredicate(SelectionPredicates.createSelectAnything())
         .build()

        if(savedInstanceState != null)
            tracker?.onRestoreInstanceState(savedInstanceState)

        adapter.setTracker(tracker!!)

        tracker?.addObserver(object: SelectionTracker.SelectionObserver<Long>() {
            override fun onSelectionChanged() {
                val nItems:Int? = tracker?.selection?.size()

                if(nItems!=null && nItems > 0) {
                    title = "$nItems 个item被选中"
                    supportActionBar?.setBackgroundDrawable(
                            ColorDrawable(Color.parseColor("#2b2b2b")))
                } else {
                    title = "RV 选择器"
                    supportActionBar?.setBackgroundDrawable(
                            ColorDrawable(getColor(R.color.colorPrimary)))
                }
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if(outState != null)
            tracker?.onSaveInstanceState(outState)
    }
}
