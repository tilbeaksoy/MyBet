package com.aksoy.mybet.utils

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {
    private lateinit var progressDialog: ProgressDialogView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initValue()
        dismissProgress()
    }
    fun startProgress() {
        if (progressDialog.dialog?.isShowing != true)
            progressDialog.startDialog()
    }

    fun dismissProgress() {
        progressDialog.dismissDialog()
    }
    private fun initValue() {
        progressDialog = ProgressDialogView(requireActivity())
    }

}