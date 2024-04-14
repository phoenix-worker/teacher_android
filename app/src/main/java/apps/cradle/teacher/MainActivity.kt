package apps.cradle.teacher

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(this)
        val contentView = inflater.inflate(R.layout.activity_main, null)
        setContentView(contentView)
    }
}