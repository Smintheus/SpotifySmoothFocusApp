package js.spotifytool.focuscounter

import android.content.Intent
import android.graphics.Insets
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.spotify.sdk.android.auth.AuthorizationResponse
import js.spotifytool.focuscounter.spotify.SpotifyOAuth
import kotlin.math.PI
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.math.sin


//TODO rückkehr zu login sollte nicht mehr möglich sein

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: SpotifyOAuth

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        auth = SpotifyOAuth(this)
        createBottomDesignElement()
     }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun createBottomDesignElement(){

        val relativeLayout = findViewById<RelativeLayout>(R.id.bottom_layout)
        val widthBar = dpToPx(10)
        val widthMargin = dpToPx(3)
        val usedWidthPercent = 1.0
        val usedWidth: Int = (windowManager.currentWindowMetrics.bounds.width() * usedWidthPercent).roundToInt()
        val countBars:Int = usedWidth / (widthBar + widthMargin * 2)
        val maxHeightLayout = dpToPx(250)
        var prevViewId = View.NO_ID
        for (i in 1..countBars){
            val view = View(this)
            val viewId = View.generateViewId()
            view.id = viewId
            val heightBar = ((getSinPercent(i.toDouble(), countBars) * maxHeightLayout).toInt() * 2 + maxHeightLayout) / 3
            val layoutParams = RelativeLayout.LayoutParams(
                widthBar,
                heightBar
            )
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            layoutParams.addRule(RelativeLayout.END_OF, prevViewId)
            layoutParams.setMargins(widthMargin,0,widthMargin,0)
            view.layoutParams = layoutParams
            view.setBackgroundResource(R.drawable.rounded_corner_shape)
            relativeLayout.addView(view)
            prevViewId = viewId
        }
    }

    private fun getSinPercent(currentNumber:Double, maxNumber:Int): Double {
        // 3 Spikes
        val countSpikes = 3
        val factor = (2 * PI * countSpikes) / maxNumber
        return (sin(factor * currentNumber - PI/2)  + 1) / 2
    }

    private fun dpToPx(dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
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

    fun auth(view: View) {
        auth.oauthIssueCode()
    }

    fun authFinished() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }



}