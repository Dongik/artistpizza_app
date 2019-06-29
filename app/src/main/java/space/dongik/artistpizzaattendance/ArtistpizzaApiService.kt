package space.dongik.artistpizzaattendance

import io.reactivex.Observable
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.http.*


interface ArtistpizzaApiService {

    @GET("ask/{pin}")
    fun ask(@Path("pin")memberId: String): Observable<AskResult>

    @POST("attend")
    fun attend(@Body attendRequest:AttendRequest):Observable<RequestResult>

    @POST("extend")
    fun extend(@Body attendRequest:ExtendRequest):Observable<RequestResult>

    @POST("register")
    fun register(@Body registerRequest: RegisterRequest):Observable<RequestResult>

    /**
     * Companion object to create the GithubApiService
     */
    companion object Factory {
        fun create(): ArtistpizzaApiService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://dongik.space:5000")
                .build()

            return retrofit.create(ArtistpizzaApiService::class.java);
        }
    }
}
