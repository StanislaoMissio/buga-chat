package com.github.bugachat.commom

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.github.bugachat.BuildConfig
import com.github.bugachat.commom.Constants.SHA_STRING
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.runBlocking
import java.security.MessageDigest
import java.util.UUID

object GoogleSignInConfiguration {

    fun configCredentialManager(context: Context) {

        val credentialManager = CredentialManager.create(context)

        val digest =
            MessageDigest.getInstance(SHA_STRING).digest(UUID.randomUUID().toString().toByteArray())
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

        val googleIdOptions = GetGoogleIdOption.Builder()
            .setServerClientId(BuildConfig.OAUTH_CLIENT_ID)
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
            } catch (e: GetCredentialException) {
                Log.e("GoogleSignInButton", "Error getting Google ID token", e)
            } catch (e: GoogleIdTokenParsingException) {
                Log.e("GoogleSignInButton", "Error parsing Google ID token", e)
            }
        }

    }

}