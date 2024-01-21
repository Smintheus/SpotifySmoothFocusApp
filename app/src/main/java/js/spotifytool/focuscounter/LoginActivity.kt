package js.spotifytool.focuscounter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.spotify.sdk.android.auth.AuthorizationResponse
import js.spotifytool.focuscounter.spotify.SpotifyOAuth

//TODO rückkehr zu login sollte nicht mehr möglich sein

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: SpotifyOAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        auth = SpotifyOAuth(this)
    }

    // Handles the authentication callback from spotify
    override fun onNewIntent(intent:Intent) {
        super.onNewIntent(intent);

        val uri: Uri? = intent.data;
        if (uri != null) {
            val response: AuthorizationResponse = AuthorizationResponse.fromUri(uri);

            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                }

                AuthorizationResponse.Type.CODE -> {
                    auth.oauthIssueRefreshToken(response.code)
                }
                else -> {
                    Log.i("Login","Got Unexpected intent from $uri")
                }
            }

        }
    }

    override fun onStop() {
        super.onStop()
        // Aaand we will finish off here.
    }

    fun auth(view: View) {
        //val t = AuthorizationClient.getResponse(-1, intent)
        auth.oauthIssueCode()
        //val intent = Intent(this, MainActivity::class.java)
        //startActivity(intent)
    }

    fun authFinished() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }



}