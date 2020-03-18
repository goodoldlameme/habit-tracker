package com.example.habittracker

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.annotation.LayoutRes

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun Array<EditText>.onTextChangeButtonEnabler(button: Button) {
    val watcher = object : TextWatcher{
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

        override fun afterTextChanged(s: Editable?) {
            button.isEnabled = !this@onTextChangeButtonEnabler.any { et -> et.text.isBlank()}
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
            Unit
    }
    forEach { e -> e.addTextChangedListener(watcher) }
}

fun <T> ArrayList<T>.replaceOrAdd(newValue: T, condition: (T) -> Boolean): ArrayList<T> {
    var replaced = false
    val result = ArrayList(map { if (condition(it))     {
        replaced = true
        newValue
    }
    else
        it
    })

    if (!replaced)
        result.add(newValue)

    return result
}
