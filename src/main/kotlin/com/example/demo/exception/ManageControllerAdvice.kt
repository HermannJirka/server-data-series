package com.example.demo.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

@ControllerAdvice
class ManageControllerAdvice {

    @ExceptionHandler(Exception::class)
    @ResponseBody
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        val httpError = HttpStatus.BAD_REQUEST
        val errorResponse = ErrorResponse(e.message ?: "Bad request", "BAD_REQUEST")
        return ResponseEntity(errorResponse, httpError);
    }

    @ExceptionHandler(BadRequestException::class)
    @ResponseBody
    fun handleException(e: BadRequestException): ResponseEntity<ErrorResponse> {
        val httpError = HttpStatus.BAD_REQUEST
        val errorResponse = ErrorResponse(e.message ?: "Bad request", "BAD_REQUEST")
        return ResponseEntity(errorResponse, httpError);
    }

    @ExceptionHandler(NotFoundException::class)
    @ResponseBody
    fun handleException(e: NotFoundException): ResponseEntity<ErrorResponse> {
        val httpError = HttpStatus.NOT_FOUND
        val errorResponse = ErrorResponse(e.message ?: "Datapoint not found", "NOT_FOUND")
        return ResponseEntity(errorResponse, httpError);
    }

    @ExceptionHandler(DuplicateDataPointException::class)
    @ResponseBody
    fun handleException(e: DuplicateDataPointException): ResponseEntity<ErrorResponse> {
        val httpError = HttpStatus.BAD_REQUEST
        val errorResponse = ErrorResponse(e.message ?: "Duplicate record exception", "DUPLICATE_RECORD")
        return ResponseEntity(errorResponse, httpError);
    }

    @ExceptionHandler(WindowSizeOutOfBoundException::class)
    @ResponseBody
    fun handleException(e: WindowSizeOutOfBoundException): ResponseEntity<ErrorResponse> {
        val httpError = HttpStatus.BAD_REQUEST
        val errorResponse = ErrorResponse(e.message ?: "Window size is out of bound", "WINDOW_SIZE_IS_OUT_OF_BOUND")
        return ResponseEntity(errorResponse, httpError);
    }
}