package com.olaelectric.dataparserengine.data

import com.olaelectric.dataparserengine.domain.Signal

object CanSignalSnapshot {
    val messageMap: HashMap<Int, List<Signal>> = hashMapOf()
    val sharedVarMap: HashMap<String, Double> = hashMapOf()
}
