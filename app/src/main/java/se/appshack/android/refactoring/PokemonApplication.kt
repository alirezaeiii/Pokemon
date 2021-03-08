package se.appshack.android.refactoring

import android.content.Context
import androidx.multidex.MultiDex
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import se.appshack.android.refactoring.di.DaggerAppComponent
import timber.log.Timber

class PokemonApplication : DaggerApplication() {

    /**
     * onCreate is called before the first screen is shown to the user.
     */
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}