package com.example.openpay.ui.checkout

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import au.com.openpay.sdkandroid.CheckoutWithOpenpay
import au.com.openpay.sdkandroid.OpenpayCheckoutResult.Status.CANCELLED
import au.com.openpay.sdkandroid.OpenpayCheckoutResult.Status.FAILURE
import au.com.openpay.sdkandroid.OpenpayCheckoutResult.Status.LODGED
import au.com.openpay.sdkandroid.OpenpayCheckoutResult.Status.REQUEST_ERROR
import au.com.openpay.sdkandroid.OpenpayCheckoutResult.Status.SUCCESS
import au.com.openpay.sdkandroid.checkoutWithOpenpay
import com.example.openpay.R
import com.example.openpay.data.AcmeCorpRepository
import com.example.openpay.databinding.FragmentCheckoutBinding
import com.example.openpay.ui.receipt.ReceiptFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
import java.math.BigDecimal
import kotlinx.coroutines.flow.collectLatest

class CheckoutFragment : Fragment() {

    private val viewModel: CheckoutViewModel by viewModels {
        CheckoutViewModel.factory(
            repository = AcmeCorpRepository(),
            totalPrice = BigDecimal.valueOf(50)
        )
    }

    private lateinit var checkoutWithOpenpay: CheckoutWithOpenpay

    private var binding: FragmentCheckoutBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkoutWithOpenpay = checkoutWithOpenpay {
            when (it.status) {
                SUCCESS, LODGED -> viewModel.onCheckoutSuccess(requireNotNull(it.planId))
                CANCELLED -> getString(R.string.checkout_resultMessage_cancelled).asSnack()
                FAILURE -> getString(R.string.checkout_resultMessage_failed).asSnack()
                REQUEST_ERROR -> getString(R.string.checkout_resultMessage_couldNotProceed).asSnack()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCheckoutBinding.inflate(inflater, container, false)
            .apply {
                checkoutEditTextEmailAddress.addTextChangedListener { text ->
                    checkoutButtonPayWithOpenpay.isEnabled =
                        Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()
                }

                checkoutButtonPayWithOpenpay.setOnClickListener {
                    viewModel.checkout(
                        firstName = checkoutEditTextFirstName.text.toString(),
                        familyName = checkoutEditTextLastName.text.toString(),
                        email = checkoutEditTextEmailAddress.text.toString(),
                        phoneNumber = checkoutEditTextPhone.text.toString(),
                        line1 = checkoutEditTextAddress.text.toString(),
                        suburb = checkoutEditTextCity.text.toString(),
                        state = checkoutTextViewState.text.toString(),
                        postCode = checkoutEditTextPostCode.text.toString()
                    )
                }

                viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                    progressOverlay.progressTitleTextView.text =
                        getString(R.string.checkout_progressMessage_orderCreation)

                    viewModel.state().collectLatest { state ->
                        progressOverlay.root.isVisible = state.loading

                        checkoutTextViewTotal.text = state.totalPrice

                        checkoutTextViewState.setAdapter(
                            ArrayAdapter(requireContext(), R.layout.list_item_state, state.states)
                        )
                    }
                }

                viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                    viewModel.commands().collectLatest { command ->
                        when (command) {
                            is CheckoutViewModel.Command.CommenceCheckout ->
                                checkoutWithOpenpay(command.handoverUrl, command.transactionToken)

                            is CheckoutViewModel.Command.ShowReceipt -> parentFragmentManager.commit {
                                setReorderingAllowed(true)
                                replace(
                                    R.id.mainContent,
                                    ReceiptFragment.newInstance(command.orderID, command.totalPrice)
                                )
                            }

                            is CheckoutViewModel.Command.DisplayError -> command.message.asSnack()
                        }
                    }
                }
            }
            .also { binding = it }
            .root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun String.asSnack() {
        Snackbar.make(requireView(), this, LENGTH_SHORT).show()
    }
}
