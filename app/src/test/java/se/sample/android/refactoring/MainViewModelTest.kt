package se.sample.android.refactoring

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.Single
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
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
import se.sample.android.refactoring.network.NamedResponseModel
import se.sample.android.refactoring.network.PokemonListResponse
import se.sample.android.refactoring.network.PokemonService
import se.sample.android.refactoring.util.Resource
import se.sample.android.refactoring.util.schedulars.BaseSchedulerProvider
import se.sample.android.refactoring.util.schedulars.ImmediateSchedulerProvider
import se.sample.android.refactoring.viewmodels.MainViewModel

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
            listOf(NamedResponseModel("pokemon", "https://pokeapi.co/api/v2/pokemon/1/"))
        )
        `when`(api.getPokemonList(anyInt())).thenReturn(Single.just(pokemonListResponse))

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
        val observableResponse = Single.error<PokemonListResponse>(Exception("error"))
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
