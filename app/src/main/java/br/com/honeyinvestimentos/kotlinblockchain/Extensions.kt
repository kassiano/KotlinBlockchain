package br.com.honeyinvestimentos.kotlinblockchain

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText


fun EditText.afterTextChange( f:(it: EditText)-> Unit  ){

    this.addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            f(this@afterTextChange)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    })
}

