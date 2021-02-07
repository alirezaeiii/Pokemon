package se.appshack.android.refactoring

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.Observable
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import se.appshack.android.refactoring.network.NamedResponseModel
import se.appshack.android.refactoring.network.PokemonService
import se.appshack.android.refactoring.network.PokemonListResponse
import se.appshack.android.refactoring.util.Resource
import se.appshack.android.refactoring.util.schedulars.BaseSchedulerProvider
import se.appshack.android.refactoring.util.schedulars.ImmediateSchedulerProvider
import se.appshack.android.refactoring.viewmodels.MainViewModel

class MainViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var api: PokemonService

    private lateinit var schedulerProvider: BaseSchedulerProvider

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        // Make the sure that all schedulers are immediate.
        schedulerProvider = ImmediateSchedulerProvider()
    }

    @Test
    fun loadPokemon() {
        val pokemonListResponse = PokemonListResponse(
            1, "next", null,
            listOf(NamedResponseModel("pokemon", "https://pokeapi.co/api/v2/pokemon/1"))
        )
        `when`(api.getPokemonList(anyInt())).thenReturn(Observable.just(pokemonListResponse))

        val viewModel = MainViewModel(api, schedulerProvider)

        viewModel.liveData.value.let {
            assertThat(it, `is`(notNullValue()))
            if (it is Resource.Success) {
                it.data?.let { data -> assertTrue(data.isNotEmpty()) }
                assertThat(it.data?.size, `is`(1))
            }
        }
    }

    @Test
    fun errorLoadingPokemon() {
        val observableResponse = Observable.error<PokemonListResponse>(Exception("error"))
        `when`(api.getPokemonList(anyInt())).thenReturn(observableResponse)

        val viewModel = MainViewModel(api, schedulerProvider)

        viewModel.liveData.value.let {
            assertThat(it, `is`(notNullValue()))
            if (it is Resource.Failure) {
                assertThat(it.cause, `is`(notNullValue()))
                assertThat(it.cause, `is`("error"))
            }
        }
    }
}
