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
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import se.sample.android.refactoring.domain.Pokemon
import se.sample.android.refactoring.domain.repository.MainRepository
import se.sample.android.refactoring.util.Resource
import se.sample.android.refactoring.util.schedulars.BaseSchedulerProvider
import se.sample.android.refactoring.util.schedulars.ImmediateSchedulerProvider

class MainViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: MainRepository

    private lateinit var schedulerProvider: BaseSchedulerProvider

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        // Make the sure that all schedulers are immediate.
        schedulerProvider = ImmediateSchedulerProvider()
    }

    @Test
    fun loadPokemon() {
        `when`(repository.getPokemonList()).thenReturn(Single.just(emptyList()))

        val viewModel = MainViewModel(repository, schedulerProvider)

        viewModel.liveData.value.let {
            assertThat(it, `is`(notNullValue()))
            if (it is Resource.Success) {
                it.data?.let { data -> assertTrue(data.isEmpty()) }
            }
        }
    }

    @Test
    fun errorLoadingPokemon() {
        val observableResponse = Single.error<List<Pokemon>>(Exception("error"))
        `when`(repository.getPokemonList()).thenReturn(observableResponse)

        val viewModel = MainViewModel(repository, schedulerProvider)

        viewModel.liveData.value.let {
            assertThat(it, `is`(notNullValue()))
            if (it is Resource.Failure) {
                assertThat(it.cause, `is`(notNullValue()))
                assertThat(it.cause, `is`("error"))
            }
        }
    }
}
