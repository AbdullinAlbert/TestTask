package com.albertabdullin.testtask.network

import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

import retrofit2.http.GET
import retrofit2.http.QueryMap

private const val BASE_URL = "http://www.cbr.ru/scripts/"

interface DollarRateService {
    @GET("XML_dynamic.asp")
    suspend fun getDollarRateForCertainPeriod(@QueryMap filters: Map<String, String>): ValCurs
}

private val retroFit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(SimpleXmlConverterFactory.createNonStrict(Persister(AnnotationStrategy())))
    .build()

object DollarRateAPI {
    val dollarRateService: DollarRateService by lazy {
        retroFit.create(DollarRateService::class.java)
    }
}