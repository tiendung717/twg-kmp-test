package nz.co.warehouseandroidtest.data.di

import io.ktor.client.HttpClient
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import nz.co.warehouseandroidtest.data.remote.AUTH_CLIENT
import nz.co.warehouseandroidtest.data.remote.Authenticator
import nz.co.warehouseandroidtest.data.remote.LOGIN_CLIENT
import nz.co.warehouseandroidtest.data.remote.installAuth
import nz.co.warehouseandroidtest.data.remote.installDefaults

@Module
@ComponentScan("nz.co.warehouseandroidtest.data")
class DataModule {

    @OptIn(ExperimentalSerializationApi::class)
    @Single
    fun provideJsonParser(): Json {
        return Json {
            allowTrailingComma = true
            ignoreUnknownKeys = true
            prettyPrint = true
        }
    }

    @Single
    @Named(AUTH_CLIENT)
    fun provideAuthHttpClient(jsonParser: Json): HttpClient = HttpClient {
        installDefaults(jsonParser)
    }

    @Single
    @Named(LOGIN_CLIENT)
    fun provideLoginHttpClient(
        jsonParser: Json,
        authenticator: Authenticator,
    ): HttpClient = HttpClient {
        installDefaults(jsonParser)
    }.apply {
        installAuth(authenticator)
    }
}
