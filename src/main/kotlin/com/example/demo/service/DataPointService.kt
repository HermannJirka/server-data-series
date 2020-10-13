package com.example.demo.service

import com.example.demo.exception.DuplicateDataPointException
import com.example.demo.exception.NotFoundException
import com.example.demo.model.DataPoint
import com.example.demo.model.DataType
import com.example.demo.repository.DataPointRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class DataPointService(private val dataPointRepository: DataPointRepository) {
    fun addDataPoint(dataPoint: DataPoint) {
        if (dataPointRepository.containsDataPoint(dataPoint)) {
            logger.warn { "Insert duplicate datapoint" }
            throw DuplicateDataPointException("Record is in database! user: ${dataPoint.user} device: ${dataPoint.device}")
        }
        dataPointRepository.addDataPoint(dataPoint)
        logger.info { "Datapoint was saved" }
    }

    fun deleteAllDeviceDataPoints(device: String) {
        val dataPoints = dataPointRepository.findByDevice(device)
        if (dataPoints.isEmpty()) {
            logger.warn { "DataPoint with device $device not found" }
            throw NotFoundException("Device $device not found!!!")
        } else {
            dataPointRepository.removeAll(dataPoints)
            logger.info { "DataPoint with device $device was delete" }
        }
    }

    fun deleteAllUserDataPoints(user: String) {
        val dataPoints = dataPointRepository.findByUser(user)
        if (dataPoints.isEmpty()) {
            logger.warn { "DataPoint with user $user not found" }
            throw NotFoundException("User $user not found!!!")
        } else {
            dataPointRepository.removeAll(dataPoints)
            logger.info { "DataPoint with user $user was delete" }
        }
    }

    fun getAllDeviceByName(name: String, dataType: DataType): List<DataPoint> {
        val foundDataPoint = findDataByTypeAndName(name, dataType)
        if (foundDataPoint.isEmpty()) {
            logger.warn { "DataPoint with $dataType $name not found" }
            throw NotFoundException("$dataType $name not found!!!")
        } else {
            logger.info { "DataPoints with user $dataType $name founded" }
            return foundDataPoint
        }
    }

    private fun findDataByTypeAndName(name: String, type: DataType): List<DataPoint> {
        return when (type) {
            DataType.DEVICE -> dataPointRepository.findByDevice(name)
            DataType.USER -> dataPointRepository.findByUser(name)
        }
    }
}