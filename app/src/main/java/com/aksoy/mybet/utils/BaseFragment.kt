package com.aksoy.mybet.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.aksoy.mybet.R
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.alertdialog_generic_choose.*
import kotlinx.android.synthetic.main.alertdialog_generic_choose.view.*
import kotlinx.android.synthetic.main.alertdialog_info.*
import kotlinx.android.synthetic.main.alertdialog_info.view.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

open class BaseFragment : Fragment() {
    private lateinit var progressDialog: ProgressDialogView
    lateinit var mAlertDialog: AlertDialog
    var chooseAlertDialogOpened = false
    var blurryBackgroundColor = Color.argb(200, 248, 248, 255)
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
    fun View.hideKeyboard() {
        val im = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        im.hideSoftInputFromWindow(windowToken, 0)
    }
    fun openChooseAlertDialog(
        message: String?,
        message2: String?,
        listener: SelectListener<Boolean>? = null,
    ) {
        if (chooseAlertDialogOpened)
            return
        chooseAlertDialogOpened = true
        lifecycleScope.launch {
            startProgress()
            delay(250L)

            if (::mAlertDialog.isInitialized && mAlertDialog.isNotNull() && mAlertDialog.isShowing) {
                return@launch
            }

            view?.hideKeyboard()
            val mDialogView =
                LayoutInflater.from(activity).inflate(R.layout.alertdialog_generic_choose, null)
            val mBuilder = AlertDialog.Builder(activity)
                .setView(mDialogView)
            mAlertDialog = mBuilder.show()

            val lp: WindowManager.LayoutParams = mAlertDialog.window!!.attributes
            lp.dimAmount = 0.0f
            mAlertDialog.window?.attributes = lp
            mAlertDialog.setCanceledOnTouchOutside(false)
            mAlertDialog.setCancelable(false)

            mDialogView.tv_message.text = message
            if (message2.isNotNull())
                mDialogView.tv_message_2.text = message2

            Blurry.with(context).radius(25).color(blurryBackgroundColor)
                .sampling(1)
                .onto(view?.rootView as ViewGroup?)
            mAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mDialogView.rl_cancel_alert.setOnClickListener {
                dismissProgress()
                chooseAlertDialogOpened = false
                Blurry.delete(view?.rootView as ViewGroup?)
                mAlertDialog.dismiss()
                listener?.onClickListener(false)
            }

            mAlertDialog.btn_yes.setOnClickListener {
                dismissProgress()
                chooseAlertDialogOpened = false
                Blurry.delete(view?.rootView as ViewGroup?)
                mAlertDialog.dismiss()
                listener?.onClickListener(true)
            }

            mAlertDialog.btn_no.setOnClickListener {
                dismissProgress()
                chooseAlertDialogOpened = false
                Blurry.delete(view?.rootView as ViewGroup?)
                mAlertDialog.dismiss()
                listener?.onClickListener(false)
            }
        }
    }
    fun Any?.isNotNull(): Boolean {
        if (this != null && this != "")
            return true
        return false
    }
    fun openAlertDialog(
        alertType: AlertDialogType,
        message: String,
        listener: SelectListener<Boolean>? = null,
    ) {
        try {
            lifecycleScope.launch {
                delay(250L)

                if (::mAlertDialog.isInitialized && mAlertDialog.isNotNull() && mAlertDialog.isShowing)
                    return@launch

                view?.hideKeyboard()
                val mDialogView =
                    LayoutInflater.from(activity).inflate(R.layout.alertdialog_info, null)
                val mBuilder = AlertDialog.Builder(activity)
                    .setView(mDialogView)
                mAlertDialog = mBuilder.show()

                val lp: WindowManager.LayoutParams = mAlertDialog.window!!.attributes
                lp.dimAmount = 0.0f
                mAlertDialog.window?.attributes = lp
                mAlertDialog.setCanceledOnTouchOutside(false)
                mAlertDialog.setCancelable(false)

                Blurry.with(context).radius(25).color(Color.argb(200, 248, 248, 255)).sampling(1)
                    .onto(view?.rootView as ViewGroup?)
                mAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mAlertDialog.tv_info_message.text = message

                mDialogView.btn_close.setOnClickListener {
                    chooseAlertDialogOpened = false
                    Blurry.delete(view?.rootView as ViewGroup?)
                    mAlertDialog.dismiss()
                }

                mAlertDialog.btn_ok.setOnClickListener {
                    Blurry.delete(view?.rootView as ViewGroup?)
                    mAlertDialog.dismiss()

                    listener?.onClickListener(true)
                }
            }
        } catch (e: Exception) {
            println(e.message.toString())
        }
    }
}