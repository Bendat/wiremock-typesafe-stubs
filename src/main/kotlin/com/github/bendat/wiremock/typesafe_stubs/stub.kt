package com.github.bendat.wiremock.typesafe_stubs

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

import java.util.*

fun stub(op: Stub.() -> Unit): Stub {
    return Stub().apply(op)
}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Stub(var request: Request = Request(),
                var response: Response = Response()) : JsonClass {

    fun request(op: Request.() -> Unit) {
        request.apply(op)
    }

    fun response(op: Response.() -> Unit) {
        response.apply(op)
    }
}


@JsonInclude(JsonInclude.Include.NON_NULL)
data class Request(var method: HttpMethod? = null,
                   var url: String? = null,
                   var urlPattern: String? = null,
                   @JsonProperty("queryParameters")
                   var queryParams: QueryParameters? = null,
                   @JsonProperty("requestParameters")
                   var params: QueryParameters? = null,
                   var bodyPatterns: ArrayList<BodyPattern> = arrayListOf(),
                   @JsonProperty("basicAuth")
                   var auth: BasicAuth? = null,
                   var headers: ReqHeader? = null) : JsonClass {

    fun queryParameters(op: QueryParameters.() -> Unit) {
        if(queryParams == null) queryParams = QueryParameters()
        queryParams?.apply(op)
    }

    fun headers(op: ReqHeader.() -> Unit): ReqHeader {
        headers = ReqHeader().apply(op)
        return headers!!
    }

    fun bodyPattern(op: BodyPattern.() -> Unit) {
        bodyPatterns.add((BodyPattern().apply(op)))
    }

    fun basicAuth(op: BasicAuth.() -> Unit) {
        auth = BasicAuth().also(op)
    }
}


@JsonInclude(JsonInclude.Include.NON_NULL)
data class Response(
        var status: Int? = null,
        var body: String? = null,
        var bodyFileName: String? = null,
        var base64Body: String? = null,
        var jsonBody: JsonClass? = null,
        var headers: Header? = null) : JsonClass {

    fun header(op: Header.() -> Unit): Header {
        headers = Header().apply(op)
        return headers!!
    }

    fun jsonBody(op: () -> JsonClass) {
        body = op.toString()
    }
}


@JsonInclude(JsonInclude.Include.NON_NULL)
data class Header(
        @JsonProperty("Content-Type") var contentType: String? = null,
        @JsonProperty("Cache-Control") var cacheControl: String? = null,
        @JsonProperty("Set-Cookie") var cookie: Cookie = Cookie()
) : JsonClass

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ReqHeader(
        @JsonProperty("Content-Type") var contentType: ContentType? = null,
        @JsonProperty("Cache-Control") var cacheControl: String? = null,
        @JsonProperty("Set-Cookie") var cookieob: Cookie? = null) : JsonClass {

    fun contentType(op: ContentType.() -> Unit) {
        ContentType().apply(op)
    }

    fun cookie(op: ()->Unit = {}): Cookie {
        if (cookieob == null) cookieob = Cookie()
        return cookieob!!
    }
}


data class ContentType(var contains: String? = null)

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class Cookie : ArrayList<String>(2), JsonClass {
    operator fun get(key: String, value: String) {
        clear()
        add(key)
        add(value)
    }
}

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class QueryParameters : HashMap<String, QueryMatcher>(), JsonClass {
    fun param(name: String, matcher: QueryMatcher.() -> Unit) {
        put(name, QueryMatcher().apply(matcher))
    }

    operator fun String.invoke(matcher: QueryMatcher.() -> Unit) {
        param(this, matcher)
    }


}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class QueryMatcher(var matches: String? = null,
                        var equalTo: String? = null,
                        var doesNotMatch: String? = null,
                        var absent: Boolean? = null) : JsonClass


@JsonInclude(JsonInclude.Include.NON_NULL)
data class BodyPattern(var equalToJson: String? = null,
                       var matchesJsonPath: String? = null,
                       var ignoreArrayOrder: Boolean = true,
                       var ignoreExtraElements: Boolean = true) : JsonClass

data class BasicAuth(var username: String? = null,
                     var password: String? = null)

enum class HttpMethod {
    GET, PUT, POST, DELETE, ANY
}
