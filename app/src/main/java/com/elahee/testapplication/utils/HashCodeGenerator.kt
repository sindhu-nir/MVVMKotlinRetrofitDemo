package com.btracsolutions.yesparking.utils

import android.R
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Base64
import android.util.Log
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class HashCodeGenerator(base: Context?) : ContextWrapper(base) {

    private val TAG = "AppSignatureHelper"
    private val HASH_TYPE = "SHA-256"
    val NUM_HASHED_BYTES = 9
    val NUM_BASE64_CHAR = 11


    /**
     * Get all the app signatures for the current package
     */
    fun getAppSignatures(): String? {
        val appCodes = ArrayList<String>()
        var appSig = ""
        try {
            // Get all package signatures for the current package
            val packageName: String = getPackageName()
            val packageManager: PackageManager = getPackageManager()
            val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNATURES
                ).signatures
            } else {
                TODO("VERSION.SDK_INT < P")
            }

            // For each signature create a compatible hash
            for (signature in signatures) {
                val hash = hash(packageName, signature.toCharsString())
                if (hash != null) {
                    appCodes.add(String.format("%s", hash))
                    appSig = String.format("%s", hash)
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "Unable to find package to obtain hash.", e)
        }
        return appSig
    }

    private fun hash(packageName: String, signature: String): String? {
        val appInfo = "$packageName $signature"
        try {
            val messageDigest = MessageDigest.getInstance(HASH_TYPE)
            messageDigest.update(appInfo.toByteArray(StandardCharsets.UTF_8))
            var hashSignature = messageDigest.digest()

            // truncated into NUM_HASHED_BYTES
            hashSignature = Arrays.copyOfRange(hashSignature, 0, NUM_HASHED_BYTES)
            // encode into Base64
            var base64Hash =
                Base64.encodeToString(hashSignature, Base64.NO_PADDING or Base64.NO_WRAP)
            base64Hash = base64Hash.substring(0, NUM_BASE64_CHAR)
            Log.d(TAG, String.format("pkg: %s -- hash: %s", packageName, base64Hash))
            return base64Hash
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "hash:NoSuchAlgorithm", e)
        }
        return null
    }


    fun getSystemSignature(): ByteArray? {
        return getSignature(getPackageName())
    }
    fun getSystemSignatureString(): String? {
        val appCodes = ArrayList<String>()
        var appSig = ""

        val signatures: ByteArray?= getSignature(getPackageName())
        for (signature in signatures!!) {
            val hash = hash(packageName, signature.toString())
            if (hash != null) {
                appCodes.add(String.format("%s", hash))
                appSig = String.format("%s", hash)
            }
        }

        return appSig
    }

    private fun getPackageInfo(clientPackageName: String): PackageInfo? {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                val flags = PackageManager.GET_SIGNING_CERTIFICATES or PackageManager.GET_PERMISSIONS
                packageManager.getPackageInfo(
                    clientPackageName,
                    PackageManager.PackageInfoFlags.of(flags.toLong()),
                )
            }

            else -> {
                val flags = PackageManager.GET_SIGNATURES or PackageManager.GET_PERMISSIONS
                packageManager.getPackageInfo(
                    clientPackageName,
                    flags,
                )
            }
        }
    }

    private fun getSignature(packageName: String): ByteArray? {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> {
                getPackageInfo(packageName)
                    ?.signingInfo?.signingCertificateHistory?.get(0)?.toByteArray()
            }

            else -> {
                getPackageInfo(packageName)?.signatures?.get(0)?.toByteArray()
            }
        }
    }
}