package com.devZ.tagworks

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window

class Utils (val context: Context) {


    private var dialog= Dialog(context)

    fun startLoadingAnimation() {
        dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_loading)
        dialog.setCancelable(false)
        dialog.show()
    }

    fun endLoadingAnimation() {
        dialog.dismiss()
    }





}