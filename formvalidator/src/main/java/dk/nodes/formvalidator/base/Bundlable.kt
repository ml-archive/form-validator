package dk.nodes.formvalidator.base

import android.os.Bundle

/**
 * Interface to handle bundling FormLayout fields
 */
interface Bundlable  {

    /**
     * Wraps field value into Bundle
     * @return bundle with saved state of this field
     */
    fun storeToBundle() : Bundle

    /**
     * Restores field's state from the bundle
     * @param bundle with the saved state
     */
    fun restoreFromBundle(bundle: Bundle)
}