package com.example.openpay.ui.receipt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.example.openpay.R
import com.example.openpay.ui.checkout.CheckoutFragment
import com.example.openpay.databinding.FragmentReceiptBinding
import java.math.BigDecimal

private const val ARG_ORDER_ID = "OPENPAY_ORDER_ID"
private const val ARG_ORDER_TOTAL = "OPENPAY_ORDER_TOTAL"

class ReceiptFragment : Fragment() {

    private val viewModel: ReceiptViewModel by viewModels {
        requireArguments().run {
            ReceiptViewModel.factory(
                orderID = requireNotNull(getString(ARG_ORDER_ID)),
                orderTotal = requireNotNull(get(ARG_ORDER_TOTAL) as? BigDecimal)
            )
        }
    }

    private var binding: FragmentReceiptBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().onBackPressedDispatcher.addCallback(this) { navigateToCheckout() }

        return FragmentReceiptBinding.inflate(inflater, container, false)
            .apply {
                receiptTextViewMessage.text = viewModel.orderDetails
                    .run { getString(messageResID, orderID, orderTotal) }

                receiptButtonDone.setOnClickListener { navigateToCheckout() }
            }
            .also { binding = it }
            .root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun navigateToCheckout() {
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.mainContent, CheckoutFragment())
        }
    }

    companion object {

        fun newInstance(
            orderID: String,
            orderTotal: BigDecimal
        ) = ReceiptFragment().apply {
            arguments = bundleOf(
                ARG_ORDER_ID to orderID,
                ARG_ORDER_TOTAL to orderTotal
            )
        }
    }
}
