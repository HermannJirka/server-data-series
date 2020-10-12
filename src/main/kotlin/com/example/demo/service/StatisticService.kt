package com.example.demo.service

import com.example.demo.model.DataPoint
import com.example.demo.repository.DataPointRepository
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList

@Service
class StatisticService(private val dataPointRepository: DataPointRepository) {

    fun deviceAverageTime(device: String): MutableList<Double> {
        var convertDataPointsToMap = convertDataPointsToMap(dataPointRepository.deviceAverageTime(device))
        return convertDataPointsToMap.values.stream().map { dataPoints -> computeAverageTime(dataPoints) }.collect(Collectors.toList())
    }

    fun deviceMovingAverageTime(device: String, windowSize: Int): ArrayList<Double> {
        var deviceAverageTime = deviceAverageTime(device)
        var deviceMovingTime = arrayListOf<Double>()
        for(i in 0..deviceAverageTime.size){
            if((i+windowSize) <= deviceAverageTime.size){
                var count: Double = 0.0
                for (j in i..(i+windowSize)){
                    count += deviceAverageTime.get(j)
                }
            deviceMovingTime.add((count/windowSize))
            }
        }
        return deviceMovingTime
    }

    fun userAverageTime(user: String): MutableList<Double> {
        var convertDataPointsToMap = convertDataPointsToMap(dataPointRepository.userAverageTime(user))
        return convertDataPointsToMap.values.stream().map { dataPoints -> computeAverageTime(dataPoints) }.collect(Collectors.toList())
    }

    fun userMovingAverageTime(user: String, windowSize: Int): ArrayList<Double> {
        var userAverageTime = deviceAverageTime(user)
        var userMovingTime = arrayListOf<Double>()
        for(i in 0..userAverageTime.size){
            if((i+windowSize) <= userAverageTime.size){
                var count: Double = 0.0
                for (j in i..(i+windowSize)){
                    count += userAverageTime.get(j)
                }
                userMovingTime.add((count/windowSize))
            }
        }
        return userMovingTime
    }

    private fun convertDataPointsToMap(datapoints: Set<DataPoint>): TreeMap<Long, ArrayList<DataPoint>> {
        var deviceMap: TreeMap<Long, ArrayList<DataPoint>> = TreeMap()
        for (dataPoint in datapoints) {
            var bucket: Long = (dataPoint.timestamp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 60000) / 15
            if (deviceMap.containsKey(bucket)) {
                var bucketDataPoints = deviceMap.get(bucket)
                bucketDataPoints?.add(dataPoint)
            } else {
                deviceMap.put(bucket, arrayListOf(dataPoint))
            }
        }
        return deviceMap
    }

    private fun computeAverageTime(dataPoints: java.util.ArrayList<DataPoint>): Double {
        return dataPoints.stream().map { dp -> dp.timestamp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 60000 }.collect(Collectors.toList()).average()
    }
}