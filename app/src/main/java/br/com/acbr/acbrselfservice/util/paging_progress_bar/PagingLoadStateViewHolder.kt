package br.com.acbr.acbrselfservice.util.paging_progress_bar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import br.com.acbr.acbrselfservice.R
import br.com.acbr.acbrselfservice.databinding.MerchantMenuLoadStateFooterViewItemBinding


class PagingLoadStateViewHolder(
    private val binding: MerchantMenuLoadStateFooterViewItemBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            //binding.errorMsg.text = loadState.error.localizedMessage
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.errorMsg.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): PagingLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.merchant_menu_load_state_footer_view_item, parent, false)
            val binding = MerchantMenuLoadStateFooterViewItemBinding.bind(view)
            return PagingLoadStateViewHolder(binding, retry)
        }
    }
}