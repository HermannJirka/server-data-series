package com.example.demo.model

import java.math.BigDecimal

data class AverageResponse(val bucketStart: Long, val bucketEnd: Long, val average: BigDecimal)