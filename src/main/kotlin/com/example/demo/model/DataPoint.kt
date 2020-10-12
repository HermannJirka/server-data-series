package com.example.demo.model

import java.time.LocalDateTime

data class DataPoint(val value: Int, val device: String, val user: String, val timestamp: LocalDateTime)