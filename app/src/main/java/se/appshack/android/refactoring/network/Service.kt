package se.appshack.android.refactoring.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import io.reactivex.Single
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import se.appshack.android.refactoring.BuildConfig
import timber.log.Timber
import java.util.*
import javax.inject.Singleton


/**
 * A retrofit service to fetch items.
 */
interface PokemonService {

    @GET("pokemon/")
    fun getPokemonList(@Query("limit") limit: Int): Single<PokemonListResponse>

    @GET("pokemon/{id}")
    fun getPokemonDetails(@Path("id") id: Int): Single<PokemonDetailsResponse>

    @GET("pokemon-species/{id}")
    fun getPokemonSpecies(@Path("id") id: Int): Single<PokemonSpeciesResponse>
}

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private fun getLoggerInterceptor(): Interceptor {
    val logger = HttpLoggingInterceptor {
        Timber.d(it)
    }
    logger.level = HttpLoggingInterceptor.Level.BASIC
    return logger
}

private val spec = ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
    .supportsTlsExtensions(true)
    .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
    .cipherSuites(
        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,
        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
        CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
        CipherSuite.TLS_ECDHE_ECDSA_WITH_RC4_128_SHA,
        CipherSuite.TLS_ECDHE_RSA_WITH_RC4_128_SHA,
        CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA,
        CipherSuite.TLS_DHE_DSS_WITH_AES_128_CBC_SHA,
        CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA
    )
    .build()

/**
 * Main entry point for network access.
 */
@Module
class Network {

    // Configure retrofit to parse JSON and use rxJava
    @Singleton
    @Provides
    fun retrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.POKEMON_BASE_URL)
        .client(
            OkHttpClient.Builder()
                .connectionSpecs(Collections.singletonList(spec))
                .addInterceptor(getLoggerInterceptor())
                .build()
        )
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    @Singleton
    @Provides
    fun itemApi(retrofit: Retrofit): PokemonService = retrofit.create(PokemonService::class.java)
}