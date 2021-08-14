package se.sample.android.refactoring.ui

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import se.sample.android.refactoring.R

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
