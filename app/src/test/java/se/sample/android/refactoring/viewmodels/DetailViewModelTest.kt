package se.sample.android.refactoring.viewmodels

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
import se.sample.android.refactoring.domain.DetailWrapper
import se.sample.android.refactoring.domain.repository.DetailRepository
import se.sample.android.refactoring.network.NamedResponseModel
import se.sample.android.refactoring.network.PokemonDetailsResponse
import se.sample.android.refactoring.network.PokemonSpritesModel
import se.sample.android.refactoring.util.Resource
import se.sample.android.refactoring.util.schedulars.BaseSchedulerProvider
import se.sample.android.refactoring.util.schedulars.ImmediateSchedulerProvider

class DetailViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: DetailRepository

    private lateinit var schedulerProvider: BaseSchedulerProvider

    private lateinit var detailWrapper: DetailWrapper

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        // Make the sure that all schedulers are immediate.
        schedulerProvider = ImmediateSchedulerProvider()

        detailWrapper = DetailWrapper(
            PokemonDetailsResponse(
                1, "pokemon", 10, 225, NamedResponseModel(
                    "", ""
                ), emptyList(), PokemonSpritesModel("", "")
            ), "", "genus"
        )
    }

    @Test
    fun loadPokemonDetails() {
        `when`(repository.getDetails(anyInt())).thenReturn(Single.just(detailWrapper))

        val viewModel = DetailViewModel(repository, schedulerProvider, anyInt())

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
        val pokemonDetailsResponse = Single.error<DetailWrapper>(Exception("error"))
        `when`(repository.getDetails(anyInt())).thenReturn(pokemonDetailsResponse)

        val viewModel = DetailViewModel(repository, schedulerProvider, anyInt())

        with(viewModel.liveData.value) {
            assertThat(this, `is`(notNullValue()))
            if (this is Resource.Failure) {
                assertThat(cause, `is`(notNullValue()))
                assertThat(cause, `is`("error"))
            }
        }
    }
}