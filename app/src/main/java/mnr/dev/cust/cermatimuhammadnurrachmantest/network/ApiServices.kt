package mnr.dev.cust.cermatimuhammadnurrachmantest.network

import mnr.dev.cust.cermatimuhammadnurrachmantest.model.Users
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

@JvmSuppressWildcards
interface ApiServices {
    @GET("search/users")
    fun getGitHubUsers(@QueryMap data: HashMap<String, String>): Call<ResponseObjects<ArrayList<Users>>>
}
