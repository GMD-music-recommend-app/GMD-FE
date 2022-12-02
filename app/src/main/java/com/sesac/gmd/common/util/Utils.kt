/*
* Created by gabriel
* date : 22/11/21
* */

package com.sesac.gmd.common.util

import android.widget.Toast
import com.sesac.gmd.application.GMDApplication

const val TAB_CHART = 0
const val TAB_HOME = 1
const val TAB_SETTING = 2

class Utils {
    companion object {
        private val TAG = "Utils"

        // Toast 출력 함수
        fun toastMessage(message: String) {
            Toast.makeText(GMDApplication.getAppInstance(), message, Toast.LENGTH_SHORT).show()
        }
    }
}