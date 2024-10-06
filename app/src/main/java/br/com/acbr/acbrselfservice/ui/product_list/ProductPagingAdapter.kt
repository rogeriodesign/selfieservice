package br.com.acbr.acbrselfservice.ui.product_list

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.acbr.acbrselfservice.R
import br.com.acbr.acbrselfservice.entity.Category
import br.com.acbr.acbrselfservice.entity.Product
import br.com.acbr.acbrselfservice.util.Utils
import com.squareup.picasso.Picasso


class ProductPagingAdapter (private val context: Context): PagingDataAdapter<ProductUiListModel, RecyclerView.ViewHolder>(diffCallback) {
    private var onClickListenerGoto: (Product)-> Unit = {}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == R.layout.merchant_menu_item) {
            val viewCreate: View = LayoutInflater.from(context).inflate(R.layout.merchant_menu_item, parent, false)
            ProductViewHolder(context, viewCreate, onClickListenerGoto)
        } else {
            val viewCreate: View = LayoutInflater.from(context).inflate(R.layout.category_title, parent, false)
            CategoryViewHolder(viewCreate)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ProductUiListModel.ProductItem -> R.layout.merchant_menu_item
            is ProductUiListModel.CategoryItem -> R.layout.category_title
            null -> throw UnsupportedOperationException("Unknown view")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            when (it) {
                is ProductUiListModel.ProductItem -> (holder as ProductViewHolder).bind(it.product)
                is ProductUiListModel.CategoryItem -> (holder as CategoryViewHolder).bind(it.category)
            }
        }
    }

    fun setOnClickListener(onClickListenerGoto: (Product)-> Unit){
        this.onClickListenerGoto = onClickListenerGoto
    }


    class ProductViewHolder(private val context:Context, itemView: View,
                            private val onClickListenerGoto: (Product)-> Unit) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle = itemView.findViewById<TextView>(R.id.tv_item_title)
        private val tvDescription = itemView.findViewById<TextView>(R.id.tv_item_description)
        private val tvPrice = itemView.findViewById<TextView>(R.id.tv_item_price_value)
        private val ivProductImage = itemView.findViewById<ImageView>(R.id.iv_item_image)

        fun bind(product: Product?){
            fillField(product)
        }

        private fun fillField(product: Product?){
            if(product != null){
                tvTitle.text = product.name
                tvDescription.text = product.description
                tvPrice.text = Utils.toMonetaryFormat(product.value)

                if(!product.imagePath.isNullOrBlank() && product.imagePath.indexOf("http", 0, true) > -1){
                    ivProductImage.background = ColorDrawable(Color.parseColor("#FFFFFF"))
                    Picasso.get()
                        .load(product.imagePath)
                        .placeholder(R.drawable.ic_default_background)
                        .error(R.drawable.ic_default_background)
                        .resize((50 * context.resources.displayMetrics.density).toInt(), (50 * context.resources.displayMetrics.density).toInt())
                        .centerInside()
                        //.fit()
                        .into(ivProductImage)
                } else {
                    ivProductImage.background = context.getDrawable(R.drawable.ic_default_background)
                    ivProductImage.scaleType = ImageView.ScaleType.CENTER_CROP
                }

                itemView.setOnClickListener {
                    onClickListenerGoto(product)
                }
            }
        }
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCategoryTitle: TextView = itemView.findViewById(R.id.tv_category_title)

        fun bind(category: Category?){
            fillField(category)
        }

        private fun fillField(category: Category?){
            if(category != null){
                tvCategoryTitle.text = category.name
            }
        }
    }


    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ProductUiListModel>(){
            override fun areItemsTheSame(oldItem: ProductUiListModel, newItem: ProductUiListModel): Boolean {
                return (oldItem is ProductUiListModel.ProductItem && newItem is ProductUiListModel.ProductItem &&
                        oldItem.product.uuid == newItem.product.uuid) ||
                        (oldItem is ProductUiListModel.CategoryItem && newItem is ProductUiListModel.CategoryItem &&
                                oldItem.category.uuid == newItem.category.uuid)
            }

            override fun areContentsTheSame(oldItem: ProductUiListModel, newItem: ProductUiListModel): Boolean {
                return oldItem == newItem
            }
        }
    }

}