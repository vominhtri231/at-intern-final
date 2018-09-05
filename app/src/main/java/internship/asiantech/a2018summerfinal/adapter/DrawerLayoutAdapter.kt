package internship.asiantech.a2018summerfinal.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.model.MenuItem
import internship.asiantech.a2018summerfinal.model.User

internal class DrawerLayoutAdapter(private val menuItemList: List<MenuItem>, private val user: List<User>, private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return menuItemList.size + user.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return if (viewType == 0) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.header_item, parent, false)
            HeaderItemHolder(view)
        } else {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.list_item_menu, parent, false)
            MenuItemHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0) {
            Glide.with(context).load(user[0].avatar).into((holder as HeaderItemHolder).imgAvatar)
            holder.tvName.text = user[0].name
        } else {
            (holder as MenuItemHolder).imgItem.setImageResource(menuItemList[position - 1].image)
            holder.tvMenuTitle.text = menuItemList[position - 1].title
        }
    }

    override fun getItemViewType(position: Int): Int {
        var viewType = 1
        if (position == 0) {
            viewType = 0
        }
        return viewType
    }

    inner class MenuItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgItem: ImageView = itemView.findViewById(R.id.imgMenu)
        val tvMenuTitle: TextView = itemView.findViewById(R.id.tvMenuTitle)
    }

    inner class HeaderItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgAvatar: CircleImageView = itemView.findViewById(R.id.imgAvatar)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val btnLogout: CircleImageView = itemView.findViewById(R.id.btnLogout)
    }
}
