package com.example.demo.service

import com.example.demo.exception.NotFoundException
import com.example.demo.exception.WindowSizeOutOfBoundException
import com.example.demo.model.AverageResponse
import com.example.demo.model.DataPoint
import com.example.demo.model.DataType
import com.example.demo.model.MovingAverageResponse
import com.example.demo.repository.DataPointRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.util.CollectionUtils
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

private val logger = KotlinLogging.logger {}

@Service
class StatisticService(private val dataPointRepository: DataPointRepository) {

    fun deviceAverageTime(device: String): List<AverageResponse> {
        val dataPoints = dataPointRepository.findByDevice(device)
        if(dataPoints.isEmpty()){
            logger.warn { "DataPoints with device $device not found" }
            throw NotFoundException("Device $device not found!!!")
        }
        val convertDataPointsToMap = convertDataPointsToMap(dataPoints)
        return convertDataPointsToMap.entries.map { (bucket, dataPoints) -> computeAverageTime(bucket, dataPoints) }.toCollection(arrayListOf())
    }

    fun deviceMovingAverageTime(type: DataType, device: String, windowSize: Int): List<MovingAverageResponse> {
        val deviceAverageTime = findDataByTypeAndName(type, device)

        if(windowSize > deviceAverageTime.size -1){
            throw WindowSizeOutOfBoundException("Windows size $windowSize is bigger then size of array")
        }

        val deviceMovingTime = arrayListOf<MovingAverageResponse>()
        for (i in deviceAverageTime.indices) {
            if ((i + (windowSize-1)) <= (deviceAverageTime.size - 1)) {
                var count: BigDecimal = BigDecimal.ZERO
                for (j in i until i + windowSize) {
                    count += deviceAverageTime[j].average
                }
                deviceMovingTime.add(MovingAverageResponse(deviceAverageTime[i].bucketStart, deviceAverageTime[i +  (windowSize-1)].bucketEnd, BigDecimal.valueOf(count.toDouble() / windowSize)))
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
        val dataPoints = dataPointRepository.findByUser(user)
        if(dataPoints.isEmpty()){
            logger.warn { "DataPoints with user $user not found" }
            throw NotFoundException("Device $user not found!!!")
        }
        val convertDataPointsToMap = convertDataPointsToMap(dataPointRepository.findByUser(user))
        return convertDataPointsToMap.entries.map { (bucket, dataPoints) -> computeAverageTime(bucket, dataPoints) }.toCollection(arrayListOf())
    }

    fun userMovingAverageTime(type: DataType, user: String, windowSize: Int): List<MovingAverageResponse> {
        val userAverageTime = findDataByTypeAndName(type, user)

        if(windowSize > userAverageTime.size -1){
            throw WindowSizeOutOfBoundException("Windows size $windowSize is bigger then size of array")
        }

        val userMovingTime = arrayListOf<MovingAverageResponse>()
        for (i in userAverageTime.indices) {
            if ((i + (windowSize-1)) <= (userAverageTime.size - 1)) {
                var count: BigDecimal = BigDecimal.ZERO
                for (j in i until i + windowSize) {
                    count += userAverageTime[j].average
                }
                userMovingTime.add(MovingAverageResponse(userAverageTime[i].bucketStart, userAverageTime[i +  (windowSize-1)].bucketEnd, BigDecimal.valueOf(count.toDouble() / windowSize)))
            }
        }
        return userMovingTime
    }

    private fun convertDataPointsToMap(datapoints: List<DataPoint>): Map<Long, List<DataPoint>> {
        val deviceMap: TreeMap<Long, ArrayList<DataPoint>> = TreeMap()

        var lastTimeStampMinutes = (datapoints.first().timestamp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 60000) / 15
        val currentTimeStampMinutes = (LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 60000) / 15

        while (lastTimeStampMinutes <= currentTimeStampMinutes) {
            deviceMap[lastTimeStampMinutes] = arrayListOf()
            lastTimeStampMinutes += 1
        }

        for (dataPoint in datapoints) {
            val bucket: Long = (dataPoint.timestamp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 60000) / 15
            if (deviceMap.containsKey(bucket)) {
                val bucketDataPoints = deviceMap[bucket]
                bucketDataPoints?.add(dataPoint)
            }
        }
        return deviceMap
    }

    private fun computeAverageTime(bucket: Long, dataPoints: List<DataPoint>): AverageResponse {
        val sum = dataPoints.map(DataPoint::value).average()
        val bucketStart = bucket * 15
        val bucketEnd = ((bucket + 1) * 15) - 1
        return AverageResponse(bucketStart, bucketEnd, BigDecimal(sum))
    }
}