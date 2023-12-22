package js.spotifytool.focuscounter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationResponse


class MainActivity : AppCompatActivity() {


    var token:String?=null
    val auth = SpotifyOAuth(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        // We will start writing our code here.
    }




    public fun Auth() {
        auth.authUser()
    }


    //TODo Code Challenge Implementation


    override fun onNewIntent(intent:Intent) {
        super.onNewIntent(intent);

        var uri: Uri? = intent.data;
        if (uri != null) {
            val response: AuthorizationResponse = AuthorizationResponse.fromUri(uri);

            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    response.state

                    var t = response.accessToken
                    val d: Int = 0
                }

                AuthorizationResponse.Type.CODE -> {
                    var rt = response.state
                    var t = response.accessToken
                    auth.oauthIssueTokens(response.code)
                    val d: Int = 0

                }

                AuthorizationResponse.Type.ERROR -> {}
                else -> {
                    var t = response
                }
            }

        }
    }




    private fun connected() {
        // Then we will write some more code here.
    }

    override fun onStop() {
        super.onStop()
        // Aaand we will finish off here.
    }

    fun Auth(view: View) {
        val t = AuthorizationClient.getResponse(-1, intent)
        Auth()
    }

    fun ReduceVolume(view: View) {
        //auth.oauthIssueTokens("sdf")
    }

    fun EnhanceVolume(view: View) {
        Auth()
    }


}