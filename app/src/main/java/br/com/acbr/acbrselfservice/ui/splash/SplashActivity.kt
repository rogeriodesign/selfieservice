package br.com.acbr.acbrselfservice.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.acbr.acbrselfservice.databinding.ActivitySplashBinding
import br.com.acbr.acbrselfservice.ui.home.HomeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class SplashActivity : AppCompatActivity() {
    private val viewModel: SplashViewModel by viewModel()
    private var endAnimation: Boolean = false
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivLaunchScreen.alpha = 0F
        binding.ivLaunchScreen.animate().setDuration(1500).alpha(1f).withEndAction {
            endAnimation = true
            nextScreen()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        viewModel.saveDeviceUniqueId(this.contentResolver)
    }

    private fun nextScreen() {
        startActivity(HomeActivity.getIntent(this))
        finish()
    }
}