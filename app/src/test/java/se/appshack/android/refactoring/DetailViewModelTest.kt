package se.appshack.android.refactoring

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
import se.appshack.android.refactoring.network.*
import se.appshack.android.refactoring.util.Resource
import se.appshack.android.refactoring.util.schedulars.BaseSchedulerProvider
import se.appshack.android.refactoring.util.schedulars.ImmediateSchedulerProvider
import se.appshack.android.refactoring.viewmodels.DetailViewModel

class DetailViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var api: PokemonService

    private lateinit var schedulerProvider: BaseSchedulerProvider

    private lateinit var pokemonSpecies: PokemonSpeciesResponse
    private lateinit var pokemonDetailsResponse: PokemonDetailsResponse

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        // Make the sure that all schedulers are immediate.
        schedulerProvider = ImmediateSchedulerProvider()

        pokemonDetailsResponse = PokemonDetailsResponse(
            1, "pokemon", 10, 225,
            NamedResponseModel("pokemon", "https://pokeapi.co/api/v2/pokemon/1"),
            listOf(PokemonTypeModel(1, NamedResponseModel("name", "url"))),
            PokemonSpritesModel("", "")
        )
        pokemonSpecies = PokemonSpeciesResponse(
            1, "Species", listOf(
                GenusResponseModel(
                    "genus", NamedResponseModel("en", "url")
                )
            )
        )
    }

    @Test
    fun loadPokemonDetails() {
        `when`(api.getPokemonDetails(anyInt())).thenReturn(Single.just(pokemonDetailsResponse))
        `when`(api.getPokemonSpecies(anyInt())).thenReturn(Single.just(pokemonSpecies))

        val viewModel = DetailViewModel(api, schedulerProvider, anyInt())

        with(viewModel.liveData.value) {
            assertThat(this, `is`(notNullValue()))
            if (this is Resource.Success) {
                assertTrue(data?.pokemonDetail?.name == "pokemon")
                assertThat(data?.pokemonDetail?.height, `is`(10))
                assertThat(data?.pokemonDetail?.weight, `is`(225))
                assertThat(data?.genus, `is`(notNullValue()))
                assertTrue(data?.genus == "genus")
            }
        }
    }

    @Test
    fun errorLoadingPokemonDetails() {
        val pokemonDetailsResponse = Single.error<PokemonDetailsResponse>(Exception("error"))
        `when`(api.getPokemonDetails(anyInt())).thenReturn(pokemonDetailsResponse)
        `when`(api.getPokemonSpecies(anyInt())).thenReturn(Single.just(pokemonSpecies))

        val viewModel = DetailViewModel(api, schedulerProvider, anyInt())

        with(viewModel.liveData.value) {
            assertThat(this, `is`(notNullValue()))
            if (this is Resource.Failure) {
                assertThat(cause, `is`(notNullValue()))
                assertThat(cause, `is`("error"))
            }
        }
    }
}