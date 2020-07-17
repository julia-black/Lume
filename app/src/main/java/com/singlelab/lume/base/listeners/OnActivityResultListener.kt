package com.singlelab.lume.base.listeners

import android.content.Intent

interface OnActivityResultListener {
    fun onActivityResultFragment(requestCode: Int, resultCode: Int, data: Intent?)
}