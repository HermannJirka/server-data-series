package com.example.demo.repository

import com.example.demo.model.DataPoint
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.util.*

@Service
class DataPointRepository {

    var dataPoints = TreeSet<DataPoint> { o1, o2 -> o1.timestamp.compareTo(o2.timestamp) }

    fun deviceAverageTime(device: String): List<DataPoint> {
        return dataPoints.filter { dataSeries -> device == dataSeries.device }.sortedBy { dataPoint -> dataPoint.timestamp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() }.toList()
    }

    fun userAverageTime(user: String): List<DataPoint> {
        return dataPoints.filter { dataSeries -> user == dataSeries.device }.sortedBy { dataPoint -> dataPoint.timestamp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() }.toList()
    }

    fun containsDataPoint(dataPoint: DataPoint): Boolean {
        return dataPoints.contains(dataPoint)
    }

    fun addDataPoint(dataPoint: DataPoint) {
        dataPoints.add(dataPoint)
    }
}