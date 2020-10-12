package com.example.demo.service

import com.example.demo.model.AverageResponse
import com.example.demo.model.DataPoint
import com.example.demo.model.MovingAverageResponse
import com.example.demo.repository.DataPointRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors

private val logger = KotlinLogging.logger {}

@Service
class StatisticService(private val dataPointRepository: DataPointRepository) {
    enum class DataType {
        DEVICE, USER
    }

    fun deviceAverageTime(device: String): List<AverageResponse> {
        val convertDataPointsToMap = convertDataPointsToMap(dataPointRepository.deviceAverageTime(device))
        return convertDataPointsToMap.entries.stream().map { (bucket, dataPoints) -> computeAverageTime(bucket, dataPoints) }.collect(Collectors.toList())
    }

    fun deviceMovingAverageTime(type: DataType, device: String, windowSize: Int): ArrayList<MovingAverageResponse> {
        val deviceAverageTime = findDataByTypeAndName(type, device)

        val deviceMovingTime = arrayListOf<MovingAverageResponse>()
        for (i in 0..deviceAverageTime.size) {
            if ((i + windowSize) <= deviceAverageTime.size) {
                var count: BigDecimal = BigDecimal.ZERO
                for (j in i..(i + windowSize)) {
                    count += deviceAverageTime.get(j).average
                }
                deviceMovingTime.add(MovingAverageResponse(deviceAverageTime.get(i).bucketStart, deviceAverageTime.get(i + windowSize).bucketEnd, BigDecimal.valueOf(count.toDouble() / windowSize)))
            }
        }
        return deviceMovingTime
    }

    private fun findDataByTypeAndName(type: DataType, name: String): List<AverageResponse> {
        return when (type) {
            DataType.DEVICE -> deviceAverageTime(name)
            DataType.USER -> userAverageTime(name)
        }
    }

    fun userAverageTime(user: String): List<AverageResponse> {
        val convertDataPointsToMap = convertDataPointsToMap(dataPointRepository.userAverageTime(user))
        return convertDataPointsToMap.entries.stream().map { (bucket, dataPoints) -> computeAverageTime(bucket, dataPoints) }.collect(Collectors.toList())
    }

    fun userMovingAverageTime(type: DataType, user: String, windowSize: Int): ArrayList<MovingAverageResponse> {
        val userAverageTime = findDataByTypeAndName(type, user)
        val userMovingTime = arrayListOf<MovingAverageResponse>()
        for (i in 0..userAverageTime.size) {
            if ((i + windowSize) <= userAverageTime.size) {
                var count: BigDecimal = BigDecimal.ZERO
                for (j in i..(i + windowSize)) {
                    count += userAverageTime.get(j).average
                }
                userMovingTime.add(MovingAverageResponse(userAverageTime.get(i).bucketStart, userAverageTime.get(i + windowSize).bucketEnd, BigDecimal.valueOf(count.toDouble() / windowSize)))
            }
        }
        return userMovingTime
    }

    private fun convertDataPointsToMap(datapoints: List<DataPoint>): TreeMap<Long, ArrayList<DataPoint>> {
        var deviceMap: TreeMap<Long, ArrayList<DataPoint>> = TreeMap()

        var lastTimeStampMinutes = (datapoints.first().timestamp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 60000) / 15
        var currentTimeStampMinutes = (LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 60000) / 15

        while (lastTimeStampMinutes <= currentTimeStampMinutes) {
            logger.debug { lastTimeStampMinutes }
            deviceMap.put(lastTimeStampMinutes, arrayListOf())
            lastTimeStampMinutes += 1
        }

        for (dataPoint in datapoints) {
            var bucket: Long = (dataPoint.timestamp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 60000) / 15
            if (deviceMap.containsKey(bucket)) {
                var bucketDataPoints = deviceMap.get(bucket)
                bucketDataPoints?.add(dataPoint)
            }
        }
        return deviceMap
    }

    private fun computeAverageTime(bucket: Long, dataPoints: ArrayList<DataPoint>): AverageResponse {
        var sumValue: Double = 0.0
        dataPoints.forEach(Consumer { dp -> sumValue += dp.value })
        var bucketStart = bucket * 15
        var bucketEnd = ((bucket + 1) * 15) - 1
        return AverageResponse(bucketStart, bucketEnd, BigDecimal(sumValue))
    }
}