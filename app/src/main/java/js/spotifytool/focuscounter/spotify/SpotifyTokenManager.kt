package js.spotifytool.focuscounter.spotify

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import js.spotifytool.focuscounter.RefreshTokenNotFoundException
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
//import androidx.security.crypto.MasterKey
import java.time.Instant


class SpotifyTokenManager private constructor(private val appContext: Context) {


    //TODO Locking für die meisten wichtigen dinge

    companion object {
        @Volatile
        private var instance: SpotifyTokenManager? = null

        fun getOrCreateInstance(appContext: Context) =
            instance ?: synchronized(this) {
                //TODO global get app context impl
                instance ?: SpotifyTokenManager(appContext).also { instance = it }
            }

        private const val  REFRESH_TOKEN_KEY:String = "RefreshToken"
        private const val  ACCESS_TOKEN_KEY:String = "AccessToken"
        private const val  ACCESS_TOKEN_EXPIRE_TIME_KEY:String = "ExpireTime"
        private const val  PREF_FILE:String = "secret_shared_prefs"
    }

    //privateSetter

    private val sharedPreferences:SharedPreferences
    private val clientId = "1f2599622b5c4bcfaf1c6bdc5cc6f935";
    private val client = OkHttpClient()

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

    fun storeRefreshToken(refreshToken:String){

        val editor = sharedPreferences.edit()
        editor.putString(REFRESH_TOKEN_KEY, refreshToken)
        editor.apply()
    }

    fun storeAccessToken(accessToken: String, expiresInSeconds: Long){

        val timeBuffer = 60
        val expireTimeSec = Instant.now().epochSecond + expiresInSeconds - timeBuffer

        val editor = sharedPreferences.edit()
        editor.putLong(ACCESS_TOKEN_EXPIRE_TIME_KEY, expireTimeSec)
        editor.putString(ACCESS_TOKEN_KEY, accessToken)
        editor.apply()
    }

    fun loadAccessToken():String{
        var accessToken = sharedPreferences.getString(ACCESS_TOKEN_KEY, "") ?: ""
        val expireTime = sharedPreferences.getLong(ACCESS_TOKEN_EXPIRE_TIME_KEY, 0)


        //TODO
        requestNewAccessToken()

        if (accessToken == "" || !isAccessTokenValid(accessToken, expireTime)){
            accessToken = requestNewAccessToken()
        }
        return accessToken
    }

    private fun loadRefreshToken(){
        val refreshToken = sharedPreferences.getString(REFRESH_TOKEN_KEY, null)
    }

    private fun requestNewAccessToken(): String{

        val refreshToken = sharedPreferences.getString(REFRESH_TOKEN_KEY, "") ?: ""

        if(refreshToken == ""){
            throw RefreshTokenNotFoundException()
        }


        val formBody = FormBody.Builder()
            .add("client_id", clientId)
            .add("grant_type","refresh_token")
            .add("refresh_token", refreshToken)
            .build()

        val request = Request.Builder()
            .url("https://accounts.spotify.com/api/token")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful){
                        throw IOException("Unexpected code $response")
                    }

                    val body =  JSONObject(response.body!!.string())
                    storeRefreshToken(body.getString("refresh_token"))
                    storeAccessToken(body.getString("access_token"), body.getLong("expires_in"))
                    //"expires_in"

                }
            }
        })
        return "yo"





        //throw Exception if no refreshToken is found

    }

    private fun isAccessTokenValid(accessToken:String, expireTime: Long): Boolean{

        val currentTimestampInSeconds = Instant.now().epochSecond
        return currentTimestampInSeconds < expireTime

    }


    private fun issueNewAccessToken(){

    }






//StoreREfreshTokenm
    //Load RefreshToken
    //Store expire time with offset of 5%
    //Load/Store AccessToken with ExpireTime


    //Fallback AccessToken Expoired
    //Fallback RefreshToken Expired
    //Fallback no RefreshToken









}