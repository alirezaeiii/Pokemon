package se.appshack.android.refactoring.ui

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import se.appshack.android.refactoring.R

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
