package com.oceanentp.realcalling.data.model

data class CallLogEntry(
    val number: String,
    val name: String?,
    val date: Long,
    val duration: Long,
    val type: Int
)
