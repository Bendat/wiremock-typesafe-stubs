package com.github.bendat.wiremock.typesafe_stubs

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

interface JsonClass {
    @get:JsonIgnore
    val json get() = jacksonObjectMapper().writer().writeValueAsString(this)
}