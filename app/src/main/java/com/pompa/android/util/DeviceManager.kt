package com.pompa.android.util

object DeviceManager {

    private const val GOOGLE_SDK = "google_sdk"
    private const val SDK = "sdk"
    private const val GENYMOTION = "Genymotion"
    private const val GENERIC = "generic"
    private const val ANDROID_SDK_BUILT_FOR_X86 = "Android SDK built for x86"
    private const val EMULATOR = "Emulator"
    private const val GOLDFISH = "goldfish"
    private const val RANCHU = "ranchu"


    fun checkIfTheDeviceIsEmulator(): Boolean {
        val buildProduct = android.os.Build.PRODUCT
        val buildManufacturer = android.os.Build.MANUFACTURER
        val buildBrand = android.os.Build.BRAND
        val buildDevice = android.os.Build.DEVICE
        val buildModel = android.os.Build.MODEL
        val buildHardware = android.os.Build.HARDWARE

        return (buildProduct.contains(GOOGLE_SDK) ||
                buildProduct.contains(SDK) ||
                buildManufacturer.contains(GENYMOTION) ||
                buildBrand.startsWith(GENERIC) && buildDevice.startsWith(GENERIC) ||
                buildModel.contains(ANDROID_SDK_BUILT_FOR_X86) ||
                buildModel.contains(EMULATOR) ||
                buildHardware.contains(GOLDFISH) ||
                buildHardware.contains(RANCHU))
    }
}