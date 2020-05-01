package mnr.dev.cust.cermatimuhammadnurrachmantest

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import mnr.dev.cust.cermatimuhammadnurrachmantest.adapter.UserAdapter
import mnr.dev.cust.cermatimuhammadnurrachmantest.controllers.EndlessOnScrollListener
import mnr.dev.cust.cermatimuhammadnurrachmantest.controllers.getGithubAccount
import java.util.*
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter
    private var query = ""
    private var page: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "Cermati - Test"

        adapter =
            UserAdapter(this@MainActivity)
        recyclerItems.adapter = adapter
        recyclerItems.layoutManager =
            LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)

        recyclerItems.addOnScrollListener(this.scrollData()!!)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search, menu)
        val searchViewItem: MenuItem = menu.findItem(R.id.app_bar_search)
        val searchView: SearchView = MenuItemCompat.getActionView(searchViewItem) as SearchView
        var timer = Timer()
        val DELAY: Long = 1000 // milliseconds

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                timer.cancel()
                timer = Timer()
                timer.schedule(
                    object : TimerTask() {
                        override fun run() {
                            runOnUiThread {
                                requestUsers(newText!!)
                            }
                        }
                    },
                    DELAY
                )

                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun requestUsers(newText: String) {
        val map = HashMap<String, String>()
        query = newText.toString()
        map["q"] = query
        map["page"] = page.toString()
        map["order"] = "desc"
        map["per_page"] = "10"

        page = 1
        scrollData()!!.mPreviousTotal = 0

        progress_first_load.visibility = View.VISIBLE
        recyclerItems.visibility = View.GONE
        message.visibility = View.GONE

        getGithubAccount(
            data = map,
            onSuccess = { response ->
                page += 1
                progress_first_load.visibility = View.GONE
//                        recyclerItems.visibility = View.GONE
//                        message.visibility = View.GONE
                if (response.message != null) {
                    recyclerItems.visibility = View.GONE
                    message.visibility = View.VISIBLE
                    message.text = response.message
                } else {
                    if (response.items.isNotEmpty()) {
                        recyclerItems.visibility = View.VISIBLE
                        message.visibility = View.GONE
                        adapter.addItems(response.items)
                    } else {
                        recyclerItems.visibility = View.GONE
                        message.visibility = View.VISIBLE
                        message.text = "No Data Found !"
                    }
                }
            },
            onFail = { map ->
                progress_first_load.visibility = View.GONE
                if (map["message"] != null) {
                    Toast.makeText(
                        this@MainActivity,
                        map["message"],
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
        )
    }

    private fun scrollData(): EndlessOnScrollListener? {
        return object : EndlessOnScrollListener() {
            override fun onLoadMore() {
                val map = HashMap<String, String>()
                map["q"] = query
                map["page"] = page.toString()
                map["order"] = "desc"
                map["per_page"] = "10"

                progress_paging_load.visibility = View.VISIBLE

                getGithubAccount(
                    data = map,
                    onSuccess = { response ->
                        progress_paging_load.visibility = View.GONE

                        page += 1

                        if (response.message != null) {
                            recyclerItems.visibility = View.GONE
                            message.visibility = View.VISIBLE
                            message.text = response.message
                        } else {
                            if (response.items.isNotEmpty()) {
                                recyclerItems.visibility = View.VISIBLE
                                message.visibility = View.GONE
                                adapter.addItemsOnPaging(response.items)
                            } else {
                                recyclerItems.visibility = View.GONE
                                message.visibility = View.VISIBLE
                                message.text = "No Data Found !"
                            }
                        }
                    },
                    onFail = { map ->
                        progress_paging_load.visibility = View.GONE

                        mPreviousTotal = 0

                        if (map["message"] != null) {
                            Toast.makeText(this@MainActivity, map["message"], Toast.LENGTH_LONG)
                                .show()
//                            recyclerItems.visibility = View.GONE
//                            message.visibility = View.VISIBLE
//                            message.text = map["message"]
                        }
                    }
                )
            }
        }
    }

}
