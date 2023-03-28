package com.sesac.gmd.common.util

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.sesac.gmd.R

// Custom Dialog 정의
object AlertDialogUtil {

    // 확인 버튼만 존재하는 Dialog
    @SuppressLint("MissingInflatedId")
    fun displayDefaultDialog(context: Context, title: String? = null, message: String) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null)

        val dialogTitle = view.findViewById<TextView>(R.id.txt_confirm_dialog_title)
        val dialogMessage = view.findViewById<TextView>(R.id.txt_confirm_dialog_message)
        val confirmButton = view.findViewById<Button>(R.id.btn_confirm)

        val builder = AlertDialog.Builder(context)
            .setView(view)

        // Dialog Title set
        if (title != null) {
            dialogTitle.text = title
        } else {
            dialogTitle.visibility = View.GONE
        }

        // Dialog Message set
        dialogMessage.text = message

        val dialog = builder.create()

        // Dialog 모서리 둥글게 만들기
        val background = ContextCompat.getDrawable(context, R.drawable.bg_dialog_default)
        dialog.window?.setBackgroundDrawable(background)

        // Button Click
        confirmButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    // 예, 아니오 버튼이 있으며 각 버튼을 누를 때 특정 기능을 수행하는 Dialog
    @SuppressLint("MissingInflatedId")
    fun displayYesNoDialog(context: Context,
                           title: String? = null,
                           message: String,
                           posFunc: () -> Unit,
                           negFunc: () -> Unit) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_yes_no, null)

        val dialogTitle = view.findViewById<TextView>(R.id.txt_yn_dialog_title)
        val dialogMessage = view.findViewById<TextView>(R.id.txt_yn_dialog_message)
        val posButton = view.findViewById<Button>(R.id.btn_yes)
        val negButton = view.findViewById<Button>(R.id.btn_no)

        val builder = AlertDialog.Builder(context)
            .setView(view)

        // Dialog Title set
        if (title != null) {
            dialogTitle.text = title
        } else {
            dialogTitle.visibility = View.GONE
        }

        // Dialog Message set
        dialogMessage.text = message

        val dialog = builder.create()

        // Dialog 모서리 둥글게 만들기
        val background = ContextCompat.getDrawable(context, R.drawable.bg_dialog_default)
        dialog.window?.setBackgroundDrawable(background)

        // Button Click
        posButton.setOnClickListener {
            posFunc()
            dialog.dismiss()
        }
        negButton.setOnClickListener {
            negFunc()
            dialog.dismiss()
        }

        dialog.show()
    }
}