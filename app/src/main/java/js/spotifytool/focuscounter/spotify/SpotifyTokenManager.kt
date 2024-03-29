package js.spotifytool.focuscounter.spotify

//import androidx.security.crypto.MasterKey
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import js.spotifytool.focuscounter.MainActivity
import js.spotifytool.focuscounter.NoValidRefreshToken
import java.time.Instant


class SpotifyTokenManager private constructor(private val appContext: Context) {

    // TODO invalid refresh token
    //TODO Locking für die meisten wichtigen dinge -> eger bucgt
    //Context irgendwie global machen das das einfacher wird

    companion object {
        @Volatile
        private var instance: SpotifyTokenManager? = null

        fun getOrCreateInstance(appContext: Context) =
            instance ?: synchronized(this) {
                instance ?: js.spotifytool.focuscounter.spotify.SpotifyTokenManager(appContext)
                    .also { instance = it }
            }

        private const val  REFRESH_TOKEN_KEY:String = "RefreshToken"
        private const val  ACCESS_TOKEN_KEY:String = "AccessToken"
        private const val  ACCESS_TOKEN_EXPIRE_TIME_KEY:String = "ExpireTime"
        private const val  PREF_FILE:String = "secret_shared_prefs"
    }

    //privateSetter

    private val sharedPreferences:SharedPreferences
    private val spotifyApi: SpotifyApi = SpotifyApi()

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

    fun hasRefreshToken():Boolean{
        try{
            loadRefreshToken()
        } catch (e:NoValidRefreshToken){
            return false
        }
        return true;
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

    suspend fun loadAccessToken():String{
        var accessToken = sharedPreferences.getString(ACCESS_TOKEN_KEY, "") ?: ""
        val expireTime = sharedPreferences.getLong(ACCESS_TOKEN_EXPIRE_TIME_KEY, 0)


        if (accessToken == "" || !isAccessTokenValid(accessToken, expireTime)){

            val refreshToken = loadRefreshToken()
            val api : SpotifyApi = SpotifyApi()
            val token = api.issueAccessToken(refreshToken)
            if(token != null){
                accessToken = token
            }
        }
        return accessToken
    }

    private fun loadRefreshToken():String{
        return sharedPreferences.getString(REFRESH_TOKEN_KEY, null) ?: throw NoValidRefreshToken()
    }

   /* suspend fun requestNewAccessToken(): String? {

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


        val call = client.newCall(request)


        val response = call.execute()
        if(!response.isSuccessful || response.body == null) {
            Log.e("access token requested", "${response.code}")
            return null
        }

        val body =  JSONObject(response.body!!.string())
        storeRefreshToken(body.getString("refresh_token"))
        storeAccessToken(body.getString("access_token"), body.getLong("expires_in"))
        return body.getString("access_token")


        //throw Exception if no refreshToken is found

    }

    */

    private fun isAccessTokenValid(accessToken:String, expireTime: Long): Boolean{

        val currentTimestampInSeconds = Instant.now().epochSecond
        return currentTimestampInSeconds < expireTime

    }







//StoreREfreshTokenm
    //Load RefreshToken
    //Store expire time with offset of 5%
    //Load/Store AccessToken with ExpireTime


    //Fallback AccessToken Expoired
    //Fallback RefreshToken Expired
    //Fallback no RefreshToken









}