package js.spotifytool.focuscounter.spotify

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.EMPTY_REQUEST
import org.json.JSONObject

class SpotifyApi
{
    private val clientId = "1f2599622b5c4bcfaf1c6bdc5cc6f935";
    private val client = OkHttpClient()

    // issue new accessToken
    //TODO what if invalid refresh token
    suspend fun issueAccessToken(refreshToken: String): String? =  withContext(Dispatchers.IO){
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
            return@withContext null
        }

        val body =  JSONObject(response.body!!.string())
        body.getString("access_token")
    }

    suspend fun changeVolume(accessToken: String, volume: Int): Boolean = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("https://api.spotify.com/v1/me/player/volume?volume_percent=$volume")
            .addHeader("Authorization", "Bearer $accessToken")
            .put(EMPTY_REQUEST)
            .build()

        val call = client.newCall(request)

        val response = call.execute()
        response.isSuccessful
    }


    // louder/quieter





}