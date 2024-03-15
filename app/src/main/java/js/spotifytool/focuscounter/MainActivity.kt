package js.spotifytool.focuscounter

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import js.spotifytool.focuscounter.spotify.SpotifyApi
import js.spotifytool.focuscounter.spotify.SpotifyTokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.max
import kotlin.math.min

class MainActivity : AppCompatActivity() {


    private var volume: Int = 15
    private lateinit var tokenManager: SpotifyTokenManager
    private val api : SpotifyApi = SpotifyApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        tokenManager = SpotifyTokenManager.getOrCreateInstance(this.applicationContext)
    }



    suspend fun refresh(view: View){
        val t = SpotifyTokenManager.getOrCreateInstance(this.applicationContext)
        t.loadAccessToken()
        val api = SpotifyApi()
        CoroutineScope(Dispatchers.IO).launch {
            val x = tokenManager.loadAccessToken()
            var z = x
        }
    }

    fun volumeDown(view:View){
        CoroutineScope(Dispatchers.IO).launch {
            val vol = max(0, volume - 5)
            val accessToken = tokenManager.loadAccessToken()
            val result = api.changeVolume(accessToken, vol)
            volume = if (result) vol else volume

        }
    }

    fun volumeUp(view:View){
        CoroutineScope(Dispatchers.IO).launch {
            val vol = min(100, volume + 5)
            val accessToken = tokenManager.loadAccessToken()
            val result = api.changeVolume(accessToken, vol)
            volume = if (result) vol else volume
        }
    }

    fun gainAcessToken(view: View){

        /*val t = SpotifyTokenManager.getOrCreateInstance(this.applicationContext)
        val textView = this.findViewById<TextView>(R.id.accessToken)
        launch(Dispatchers.Default) {

        }

        var ts = GlobalScope.launch {
            val token = t.requestNewAccessToken()
            textView.text = token
            val a = 0
        }


        ts.join()
        run(Dispatchers.Default) {}

         */
    }

    fun accToken(view:View){
        CoroutineScope(Dispatchers.IO).launch {
            val x = tokenManager.loadAccessToken()
            var z = x
        }

    }

    fun request(){
        CoroutineScope(Dispatchers.IO).launch {

        }
        kotlinx.coroutines.MainScope()
        lifecycleScope.launch{ }
        lifecycleScope.launch {  }
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {

        }




    }

}