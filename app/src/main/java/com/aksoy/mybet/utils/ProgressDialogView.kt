package com.aksoy.mybet.utils

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.aksoy.mybet.R

class ProgressDialogView(activity: Activity) {
    var activity = activity
    var dialog: AlertDialog? = null

    fun startDialog() {
        val mDialogView =
            LayoutInflater.from(activity).inflate(R.layout.progress_view, null)
        val mBuilder = AlertDialog.Builder(activity)
            .setView(mDialogView)
        dialog = mBuilder.show()
        dialog?.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(activity.applicationContext,
                R.drawable.background_transparent))
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)
    }

    fun dismissDialog() {
        if (dialog.isNotNull())
            dialog?.dismiss()
    }

    private fun Any?.isNotNull(): Boolean {
        if (this != null && this != "")
            return true
        return false
    }
}