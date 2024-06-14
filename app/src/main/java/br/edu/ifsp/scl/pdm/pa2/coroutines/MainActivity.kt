package br.edu.ifsp.scl.pdm.pa2.coroutines

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.pdm.pa2.coroutines.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        amb.launchCoroutinesBt.setOnClickListener {
            val random = Random(System.currentTimeMillis())
            val SLEEP_LIMIT = 3000L

            GlobalScope.launch(Dispatchers.Main) {
                val upperTextDeffered = async {
                    Log.v(
                        getString(R.string.app_name),
                        "Upper async coroutine thread: ${Thread.currentThread().name}, Job: ${this.coroutineContext[Job]}"
                    )

                    sleep("Upper", random.nextLong(SLEEP_LIMIT))
                }

                val lowerTextDeffered = async {
                    Log.v(
                        getString(R.string.app_name),
                        "Lower async coroutine thread: ${Thread.currentThread().name}, Job: ${this.coroutineContext[Job]}"
                    )
                    sleep("Lower", random.nextLong(SLEEP_LIMIT))
                }

                upperTextDeffered.await().let {
                    amb.upperTv.text = it
                }
                amb.lowerTv.text = lowerTextDeffered.await()

                Log.v(
                    getString(R.string.app_name),
                    "Coroutine thread: ${Thread.currentThread().name}, Job: ${this.coroutineContext[Job]}"
                )

            }

            Log.v(getString(R.string.app_name), "Main thread: ${Thread.currentThread().name}")
        }
    }

    private suspend fun sleep(name: String, time: Long): String {
        delay(time)
        return "$name slept for $time ms"
    }
}