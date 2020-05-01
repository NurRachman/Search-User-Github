package mnr.dev.cust.cermatimuhammadnurrachmantest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.items.view.*
import mnr.dev.cust.cermatimuhammadnurrachmantest.R
import mnr.dev.cust.cermatimuhammadnurrachmantest.model.Users


class UserAdapter(
    private val context: Context
) : RecyclerView.Adapter<UserAdapter.Holder>() {

    private var data = ArrayList<Users>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.items, parent, false)
        return Holder(
            view
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val view = holder.itemView
        val user = data[position]

        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)

        Glide.with(context).load(user.avatar_url).apply(options)
            .into(view.items_images)

        view.item_name.text = user.login

    }

    class Holder(view: View) : RecyclerView.ViewHolder(view)

    fun add(user: Users) {
        data.add(user)
        notifyItemInserted(data.size - 1)
    }

    fun addItemsOnPaging(users: List<Users>) {
        for (user in users)
            add(user)
    }

    fun addItems(users: ArrayList<Users>) {
        data = users
        notifyDataSetChanged()
    }

}
