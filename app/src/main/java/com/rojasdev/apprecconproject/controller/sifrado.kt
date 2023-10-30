package com.rojasdev.apprecconproject.controller

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class sifrado {

    fun generateKey() : SecretKeySpec {
        val random = SecureRandom()
        val key = ByteArray(128)
        random.nextBytes(key)

        return SecretKeySpec(key, "AES")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun encryptResources(key: SecretKeySpec, resourcesPath: String) {
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, key)

        val resources = Files.list(File(resourcesPath).toPath())
        for (resource in resources) {
            val fileName = resource.fileName.toString()
            val filePath = resourcesPath + "/" + fileName

            val resourceData = Files.readAllBytes(resource)
            val encryptedResource = cipher.doFinal(resourceData)

            FileOutputStream(filePath).write(encryptedResource)
        }
    }

}