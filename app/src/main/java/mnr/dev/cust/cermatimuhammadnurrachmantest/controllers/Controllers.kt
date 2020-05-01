package mnr.dev.cust.cermatimuhammadnurrachmantest.controllers

import android.util.Log
import mnr.dev.cust.cermatimuhammadnurrachmantest.model.Users
import mnr.dev.cust.cermatimuhammadnurrachmantest.network.ResponseObjects
import mnr.dev.cust.cermatimuhammadnurrachmantest.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun getGithubAccount(
    data: HashMap<String, String>,
    onSuccess: (ResponseObjects<ArrayList<Users>>) -> Unit,
    onFail: (HashMap<String, String>) -> Unit
) {
    val call = RetrofitClient().apiServices.getGitHubUsers(data)
    call.enqueue(
        object : Callback<ResponseObjects<ArrayList<Users>>> {
            override fun onFailure(
                call: Call<ResponseObjects<ArrayList<Users>>>,
                t: Throwable
            ) {
                if (call.isCanceled) return
                Log.d("onFailure", t.message);
            }

            override fun onResponse(
                call: Call<ResponseObjects<ArrayList<Users>>>,
                response: Response<ResponseObjects<ArrayList<Users>>>
            ) {
                if (call.isCanceled) return
                if (response.code() == 200) {
                    onSuccess(response.body()!!)
                } else {
                    val map = RetrofitClient().gson.fromJson(
                        response.errorBody()!!.string(),
                        HashMap::class.java
                    )
                    onFail(map as HashMap<String, String>)
                }
            }
        }
    )
}