package com.boxofm.crazytiles.score

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.boxofm.crazytiles.database.Games
import timber.log.Timber

@BindingAdapter("scoresHistoryString")
fun TextView.setScoresHistoryString(item: List<Games?>) {
    Timber.v("Count of all games played is " + item.count())
    val builder = StringBuilder()
    builder.append("Count is ")
    item.let {
        builder.append(item.listIterator().next()?.wins).append(" ")
    }
    text = builder.toString()
}