package com.singlelab.lume.base.listeners

interface OnPermissionListener {
    fun onLocationPermissionGranted()

    fun onLocationPermissionDenied()
}