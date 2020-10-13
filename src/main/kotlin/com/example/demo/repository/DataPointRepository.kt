package com.example.demo.repository

import com.example.demo.model.DataPoint
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.util.*

@Service
class DataPointRepository {

    private var dataPoints = TreeSet<DataPoint> { o1, o2 ->
        Comparator.comparing(DataPoint::timestamp)
                .thenComparing(DataPoint::device)
                .thenComparing(DataPoint::user)
                .compare(o1, o2) }

    fun findByDevice(device: String): List<DataPoint> {
        return dataPoints.filter { dataSeries -> device == dataSeries.device }.sortedBy { dataPoint -> dataPoint.timestamp }.toList()
    }

    fun findByUser(user: String): List<DataPoint> {
        return dataPoints.filter { dataSeries -> user == dataSeries.user }.sortedBy { dataPoint -> dataPoint.timestamp}.toList()
    }

    fun containsDataPoint(dataPoint: DataPoint): Boolean {
        return dataPoints.contains(dataPoint)
    }

    fun addDataPoint(dataPoint: DataPoint) {
        dataPoints.add(dataPoint)
    }

    fun removeAll(deleteDataPoints: List<DataPoint>) {
        dataPoints.removeAll(deleteDataPoints)
    }
}