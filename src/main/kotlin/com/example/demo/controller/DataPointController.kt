package com.example.demo.controller

import com.example.demo.model.DataPoint
import com.example.demo.service.DataPointService
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.*


@RestController
class DataPointController(private val dataPointService: DataPointService) {

    @PostMapping("/datapoints")
    @ResponseStatus(CREATED)
    fun addDataPoint(@RequestBody dataPoint: DataPoint) {
        dataPointService.addDataPoint(dataPoint)
    }

    @DeleteMapping("/devices/{device}/datapoints")
    fun deleteAllDeviceDataPoints(@PathVariable("device") device: String) {
        dataPointService.deleteAllDeviceDataPoints(device)
    }

    @DeleteMapping("/users/{user}/datapoints")
    fun deleteAllUserDataPoints(@PathVariable("user") user: String) {
        dataPointService.deleteAllUserDataPoints(user)
    }
}