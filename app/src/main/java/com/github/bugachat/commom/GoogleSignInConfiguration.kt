package com.github.bugachat.commom

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.runBlocking
import java.security.MessageDigest
import java.util.UUID

object GoogleSignInConfiguration {

    fun configCredentialManager(context: Context) {

        val credentialManager = CredentialManager.create(context)

        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

        val googleIdOptions = GetGoogleIdOption.Builder()
            .setServerClientId("")
            .setNonce(hashedNonce)
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOptions)
            .build()

        runBlocking {
            try {
                val result = credentialManager.getCredential(context, request)
                val credential = result.credential
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val googleIdToken = googleIdTokenCredential.idToken

                Log.d("GoogleSignInButton", "Google ID Token: $googleIdToken")

                Toast.makeText(context, "Google ID Token: $googleIdToken", Toast.LENGTH_SHORT)
                    .show()
            } catch (e: GetCredentialException) {
                Log.e("GoogleSignInButton", "Error getting Google ID token", e)
                Toast.makeText(context, "Error getting Google ID token", Toast.LENGTH_SHORT).show()
            } catch (e: GoogleIdTokenParsingException) {
                Log.e("GoogleSignInButton", "Error parsing Google ID token", e)
                Toast.makeText(context, "Error parsing Google ID token", Toast.LENGTH_SHORT).show()
            }
        }

    }

}