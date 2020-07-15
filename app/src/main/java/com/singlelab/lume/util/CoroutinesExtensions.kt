package com.singlelab.lume.util

import com.singlelab.data.net.CoroutineContextProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
//
//fun runOnMainThread(coroutineScope: CoroutineScope, block: () -> Unit) {
//    coroutineScope.launch(CoroutineContextProvider().main) {
//        block.invoke()
//    }
//}