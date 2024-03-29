package com.example.cryptoapp.modual.payment

data class StripeConfirmationResponse(
    val amount: Int,
    val amount_capturable: Int,
    val amount_details: AmountDetailsX,
    val amount_received: Int,
    val application: Any,
    val application_fee_amount: Any,
    val automatic_payment_methods: AutomaticPaymentMethodsX,
    val canceled_at: Any,
    val cancellation_reason: Any,
    val capture_method: String,
    val client_secret: String,
    val confirmation_method: String,
    val created: Int,
    val currency: String,
    val customer: String,
    val description: Any,
    val id: String,
    val invoice: Any,
    val last_payment_error: Any,
    val latest_charge: String,
    val livemode: Boolean,
    val metadata: MetadataXX,
    val next_action: Any,
    val `object`: String,
    val on_behalf_of: Any,
    val payment_method: String,
    val payment_method_options: PaymentMethodOptionsX,
    val payment_method_types: List<String>,
    val processing: Any,
    val receipt_email: Any,
    val review: Any,
    val setup_future_usage: Any,
    val shipping: Any,
    val source: Any,
    val statement_descriptor: Any,
    val statement_descriptor_suffix: Any,
    val status: String,
    val transfer_data: Any,
    val transfer_group: Any
)