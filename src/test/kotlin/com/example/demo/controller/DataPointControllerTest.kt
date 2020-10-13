package com.example.demo.controller

import com.example.demo.model.DataPoint
import com.example.demo.service.DataPointService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime
import java.time.ZoneId

@SpringBootTest
@AutoConfigureMockMvc
internal class DataPointControllerTest {

    @Autowired lateinit var mockMvc: MockMvc
    @Autowired lateinit var mapper: ObjectMapper

    @Test
    fun successAddDataPoint() {
        mockMvc.perform(post("/datapoints")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(DataPoint(16,"device1","user1", LocalDateTime.now(ZoneId.systemDefault())))))
                .andExpect(status().isCreated);
    }

    @Test
    fun errorAddDataPointMissingValues(){
        mockMvc.perform(post("/datapoints")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest);
    }

    @Test
    fun successDeleteAllDeviceDataPoints() {
        mockMvc.perform(post("/datapoints")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(DataPoint(15,"device1","user1", LocalDateTime.now(ZoneId.systemDefault())))))
                .andExpect(status().isCreated);

        mockMvc.perform(delete("/users/user1/datapoints"))
                .andExpect(status().isOk)

        mockMvc.perform(get("/devices/device1/datapoints"))
                .andExpect(status().isNotFound)
    }

    @Test
    fun successDeleteAllUserDataPoints() {
        mockMvc.perform(post("/datapoints")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(DataPoint(10,"device1","user1", LocalDateTime.now(ZoneId.systemDefault())))))
                .andExpect(status().isCreated);

        mockMvc.perform(delete("/devices/device1/datapoints"))
                .andExpect(status().isOk)

        mockMvc.perform(get("/users/user1/datapoints"))
                .andExpect(status().isNotFound)
    }

    @Test
    fun deleteAllDeviceDataPointsUserNotFound() {
        mockMvc.perform(delete("/devices/device1/datapoints"))
                .andExpect(status().isNotFound)
    }

    @Test
    fun deleteAllUserDataPointsDeviceNotFound() {
        mockMvc.perform(delete("/users/user1/datapoints"))
                .andExpect(status().isNotFound)
    }

    @Test
    fun addDuplicateDataEndpoint(){
        mockMvc.perform(post("/datapoints")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(DataPoint(16,"device1","user1", LocalDateTime.parse("2012-10-13T19:19:19")))))
                .andExpect(status().isCreated)

        mockMvc.perform(post("/datapoints")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(DataPoint(16,"device1","user1", LocalDateTime.parse("2012-10-13T19:19:19")))))
                .andExpect(status().isBadRequest)
    }

}