package com.nodesagency.formvalidator.base

import android.os.Bundle

interface Bundlable  {

    fun storeToBundle() : Bundle

    fun restoreFromBundle(bundle: Bundle)
}