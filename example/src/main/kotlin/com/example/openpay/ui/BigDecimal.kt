package com.example.openpay.ui

import java.math.BigDecimal
import java.text.NumberFormat

fun BigDecimal.toCurrencyString(): String = NumberFormat.getCurrencyInstance().format(this)
