package com.martafoderaro.bookingapp.domain.model

import java.util.UUID

data class Booking(
    val id: String = UUID.randomUUID().toString(),
    val startsAt: String,
    val endsAt: String,
    val spaceName: String,
    val spaceTimezone: String,
    val spaceImage: String,
)