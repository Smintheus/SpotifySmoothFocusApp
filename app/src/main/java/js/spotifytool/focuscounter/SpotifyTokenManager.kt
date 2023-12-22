package js.spotifytool.focuscounter

import android.R.attr.value
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
//import androidx.security.crypto.MasterKey
import java.io.IOException
import java.security.GeneralSecurityException


class SpotifyTokenManager private constructor(private val appContext: Context) {

    companion object {
        @Volatile
        private var instance: SpotifyTokenManager? = null

        fun getOrCreateInstance(appContext: Context?) =
            instance ?: synchronized(this) {
                //TODO global get app context impl
                instance ?: SpotifyTokenManager(appContext!!).also { instance = it }
            }

        private const val  REFRESH_TOKEN_KEY:String = "RefreshToken"
        private const val  PREF_FILE:String = "secret_shared_prefs"
    }

    //privateSetter

    private  val sharedPreferences:SharedPreferences


    init {

        val masterKey = MasterKey.Builder(appContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        sharedPreferences = EncryptedSharedPreferences.create(
            appContext,
            PREF_FILE,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )


    }

    var accessToken: String? = null

    private var refreshToken:String? = null

    fun storeRefreshToken(refreshToken:String){

        val editor = sharedPreferences.edit()
        editor.putString(REFRESH_TOKEN_KEY, refreshToken)
        editor.apply()
    }

    private fun loadRefreshToken(){
        refreshToken = sharedPreferences.getString(REFRESH_TOKEN_KEY, null)
        //TODO what if not Found
    }

    fun test(){
        accessToken = "sdf"
    }





//StoreREfreshTokenm
    //Load RefreshToken
    //Store expire time with offset of 5%
    //Load/Store AccessToken with ExpireTime


    //Fallback AccessToken Expoired
    //Fallback RefreshToken Expired
    //Fallback no RefreshToken









}