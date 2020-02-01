package com.boxofm.crazytiles.score

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.boxofm.crazytiles.database.Games

@BindingAdapter("scoresHistoryString")
fun TextView.setScoresHistoryString(item: List<Games?>) {
    val builder = StringBuilder()
    builder.append("Count is ")
    item.let {
        builder.append(item.listIterator().next()?.wins).append(" ")
    }
    text = builder.toString()
}