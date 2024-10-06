package br.com.acbr.acbrselfservice.ui

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

fun FragmentManager.runTransactionSync(
    onTransaction: (transaction: FragmentTransaction) -> Unit
) {
    val transaction = beginTransaction()
    onTransaction(transaction)
    transaction.commit()
}
