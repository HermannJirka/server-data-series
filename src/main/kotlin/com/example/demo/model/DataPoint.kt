package com.example.demo.model

import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime

data class DataPoint(@NotNull val value: String, @NotNull val device: String, @NotNull val user: String, @NotNull val timestamp: LocalDateTime)