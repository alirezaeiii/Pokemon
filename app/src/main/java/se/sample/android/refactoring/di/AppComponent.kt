package se.sample.android.refactoring.di

import android.app.Application
import javax.inject.Singleton
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import se.sample.android.refactoring.PokemonApplication
import se.sample.android.refactoring.network.Network

@Singleton
@Component(
    modules = [ActivityBindingModule::class,
        AndroidSupportInjectionModule::class,
        Network::class,
        AppModule::class]
)
interface AppComponent : AndroidInjector<PokemonApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}