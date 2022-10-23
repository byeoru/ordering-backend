package com.byeoru.ordering_server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.*
import javax.annotation.PostConstruct

@SpringBootApplication
class OrderingServerApplication

fun main(args: Array<String>) {
    runApplication<OrderingServerApplication>(*args)
}

@PostConstruct
fun started() {
    // timezone setting
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
}