package com.example.demo.repository

import com.example.demo.model.DataPoint
import org.springframework.stereotype.Service
import java.util.*

@Service
class DataPointRepository {

    var dataPoints = TreeSet<DataPoint> { o1, o2 -> o1.timestamp.compareTo(o2.timestamp) }

    fun deviceAverageTime(device: String): TreeSet<DataPoint> {
        return dataPoints.filter { dataSeries -> device == dataSeries.device }.toCollection(sortedSetOf(kotlin.Comparator { d1: DataPoint, d2: DataPoint -> d1.timestamp.compareTo(d2.timestamp) }))
    }

    fun userAverageTime(user: String): Set<DataPoint> {
        return dataPoints.filter { dataSeries -> user == dataSeries.device }.toCollection(sortedSetOf(kotlin.Comparator { d1: DataPoint, d2: DataPoint -> d1.timestamp.compareTo(d2.timestamp) }))
    }
}