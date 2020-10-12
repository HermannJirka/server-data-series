package com.example.demo.controller

import com.example.demo.model.DataPoint
import com.example.demo.service.DataPointService
import org.springframework.web.bind.annotation.*


@RestController
class DataPointController(private val dataPointService: DataPointService) {
    //    POST /datapoints [timestamp;value;device;user] - add new datapoint to time series. Tuple {timestamp, device, user} is unique. Adding the same point should cause bad request response and not change the data.
    @PostMapping("/datapoints")
    fun addDataPoint(@RequestBody dataPoint: DataPoint) {
        dataPointService.addDataPoint(dataPoint)
    }

    //    DELETE /devices/{device}/datapoints - delete all device datapoints
    @DeleteMapping(" /devices/{device}/datapoints")
    fun deleteAllDeviceDataPoints(@PathVariable("device") device: String) {
        dataPointService.deleteAllDeviceDataPoints(device)
    }

    //    DELETE /users/{user}/datapoints - delete all user datapoints
    @DeleteMapping("/users/{user}/datapoints")
    fun deleteAllUserDataPoints(@PathVariable("user") user: String) {
        dataPointService.deleteAllUserDataPoints(user)
    }
}