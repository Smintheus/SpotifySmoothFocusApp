package js.spotifytool.focuscounter

import android.app.Activity
import android.util.Base64
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.security.MessageDigest


class SpotifyOAuth(private val contextActivity: Activity) {


    private val spotifyScopes = arrayOf("streaming")
    private val redirectUri = "yourcustomprotocol://callback"
    private val clientId = "1f2599622b5c4bcfaf1c6bdc5cc6f935";

    private val client = OkHttpClient()

    private var randomString = ""

    var accessToken:String = "dsf"



    //private val refreshToken:String = test;




    fun authUser(){
        oauthIssueCode();
    }

    fun oauthIssueTokens(code:String){


        val json = "application/json".toMediaType();



        val formBody = FormBody.Builder()
            .add("client_id", clientId)
            .add("grant_type","authorization_code")
            .add("code", code)
            .add("redirect_uri", redirectUri)
            .add("code_verifier", randomString)
            .build()


        val tsdf = formBody.toString()

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
                        val t = response.body?.string()
                        throw IOException("Unexpected code $response")
                    }

                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }
                    val body =  JSONObject(response.body!!.string())


                    var x = 0

                }
            }
        })

    }



    private fun oauthIssueCode(){
        val builder =
            AuthorizationRequest.Builder(this.clientId, AuthorizationResponse.Type.CODE, this.redirectUri)

        builder.setScopes(this.spotifyScopes)

        this.randomString = this.randomStringByKotlinCollectionRandom(64);
        val hash = hash256(randomString)
        builder.setCustomParam("code_challenge_method","S256")
        builder.setCustomParam("code_challenge",hash)
        val request = builder.build()
        AuthorizationClient.openLoginInBrowser(contextActivity, request);
    }




    private fun storeRefreshToken(){
    }

    private fun loadRefreshToken(){

    }

    private fun hash256(str:String): String {
        val bytes = str.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return Base64.encodeToString(digest,Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP )
    }

    private fun randomStringByKotlinCollectionRandom(strLength:Int): String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return List(strLength) { charPool.random() }.joinToString("")
    }




}