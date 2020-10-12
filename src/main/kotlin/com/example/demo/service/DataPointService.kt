package com.example.demo.service

import com.example.demo.exception.DuplicateDataPointException
import com.example.demo.exception.NotFoundException
import com.example.demo.model.DataPoint
import com.example.demo.repository.DataPointRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.function.Predicate

private val logger = KotlinLogging.logger {}

@Service
class DataPointService(private val dataPointRepository: DataPointRepository) {
    fun addDataPoint(dataPoint: DataPoint) {
        if (dataPointRepository.containsDataPoint(dataPoint)) {
            logger.warn { "Insert duplicate datapoint" }
            throw DuplicateDataPointException("Record is in database! user: ${dataPoint.user} device: ${dataPoint.device}")
        }
        logger.info { "Datapoint was saved" }
        dataPointRepository.addDataPoint(dataPoint)
    }

    fun deleteAllDeviceDataPoints(device: String) {
        val dataPoints = dataPointRepository.dataPoints
        val dataPoint = dataPoints.stream().filter { dt -> dt.device == device }.findAny()
        if (dataPoint.isEmpty) {
            logger.warn { "DataPoint with device $device not found" }
            throw NotFoundException("Device $device not found!!!")
        } else {
            logger.info { "DataPoint with device $device was delete" }
            dataPoints.remove(dataPoint.get())
        }
    }

    fun deleteAllUserDataPoints(user: String) {
        val dataPoints = dataPointRepository.dataPoints
        val dataPoint = dataPoints.stream().filter { dt -> dt.user == user }.findAny()
        if (dataPoint.isEmpty) {
            logger.warn { "DataPoint with user $user not found" }
            throw NotFoundException("User $user not found!!!")
        } else {
            logger.info { "DataPoint with user $user was delete" }
            dataPoints.remove(dataPoint.get())
        }
    }
}