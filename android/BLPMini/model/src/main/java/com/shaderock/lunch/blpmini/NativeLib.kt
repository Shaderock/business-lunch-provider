package com.shaderock.lunch.blpmini

class NativeLib {

    /**
     * A native method that is implemented by the 'blpmini' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'blpmini' library on application startup.
        init {
            System.loadLibrary("blpmini")
        }
    }
}