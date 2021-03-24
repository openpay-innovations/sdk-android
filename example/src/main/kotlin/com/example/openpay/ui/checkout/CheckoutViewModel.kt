package com.example.openpay.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openpay.domain.Address
import com.example.openpay.domain.CustomerDetails
import com.example.openpay.domain.MerchantRepository
import com.example.openpay.ui.checkout.CheckoutViewModel.Command.CommenceCheckout
import com.example.openpay.ui.checkout.CheckoutViewModel.Command.DisplayError
import com.example.openpay.ui.checkout.CheckoutViewModel.Command.ShowReceipt
import com.example.openpay.ui.toCurrencyString
import com.example.openpay.ui.viewModelFactory
import java.math.BigDecimal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CheckoutViewModel(
    private val repository: MerchantRepository,
    private val totalPrice: BigDecimal
) : ViewModel() {

    enum class AustralianState {
        ACT,
        NT,
        NSW,
        QLD,
        SA,
        VIC,
        WA
    }

    data class State(
        val states: List<AustralianState> = AustralianState.values().toList(),
        val loading: Boolean = false,
        val totalPrice: String
    )

    private val viewState = MutableStateFlow(State(totalPrice = totalPrice.toCurrencyString()))

    private val commands = MutableSharedFlow<Command>(extraBufferCapacity = 1)

    fun state(): Flow<State> = viewState

    fun commands(): Flow<Command> = commands

    fun checkout(
        firstName: String,
        familyName: String,
        email: String,
        phoneNumber: String,
        line1: String,
        suburb: String,
        state: String,
        postCode: String
    ) {
        viewModelScope.launch {
            viewState.apply { value = value.copy(loading = true) }

            runCatching {
                withContext(Dispatchers.IO) {
                    repository
                        .createOrder(
                            customerDetails = CustomerDetails(
                                firstName,
                                familyName,
                                email,
                                phoneNumber,
                                deliveryAddress = Address(line1, suburb, state, postCode)
                            ),
                            price = totalPrice
                        )
                }
            }
                .onSuccess {
                    commands.tryEmit(CommenceCheckout(it.handoverUrl, it.transactionToken))
                }
                .onFailure { commands.tryEmit(DisplayError(it.message ?: "Payment failed")) }

            delay(timeMillis = 300)
            viewState.apply { value = value.copy(loading = false) }
        }
    }

    fun onCheckoutSuccess(orderID: String) {
        commands.tryEmit(ShowReceipt(orderID, totalPrice))
    }

    sealed class Command {

        data class CommenceCheckout(
            val handoverUrl: String,
            val transactionToken: String
        ) : Command()

        data class ShowReceipt(
            val orderID: String,
            val totalPrice: BigDecimal
        ) : Command()

        data class DisplayError(val message: String) : Command()
    }

    companion object {

        fun factory(repository: MerchantRepository, totalPrice: BigDecimal) = viewModelFactory {
            CheckoutViewModel(repository, totalPrice)
        }
    }
}
