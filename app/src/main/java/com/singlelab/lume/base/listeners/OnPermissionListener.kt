package com.singlelab.lume.base.listeners

interface OnPermissionListener {
    fun onLocationPermissionGranted()

    fun onLocationPermissionDenied()

    fun onContactsPermissionGranted()

    fun onContactsPermissionDenied()

    fun onWriteExternalPermissionGranted()

    fun onWriteExternalPermissionDenied()
}