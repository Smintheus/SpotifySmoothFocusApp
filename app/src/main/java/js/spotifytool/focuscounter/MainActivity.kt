package js.spotifytool.focuscounter

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import js.spotifytool.focuscounter.spotify.SpotifyTokenManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }


    fun refresh(view: View){
        val t = SpotifyTokenManager.getOrCreateInstance(this.applicationContext)
        t.loadAccessToken()
    }
}