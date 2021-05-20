package com.albertabdullin.testtask.network

import com.albertabdullin.testtask.database.CurrencyEntity
import com.albertabdullin.testtask.util.convertStringToDouble
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "ValCurs", strict = false)
data class ValCurs @JvmOverloads constructor(
        @field:Attribute(name = "ID", required = false) var id: String = "",
        @field:Attribute(name = "DateRange1", required = false) var dateRange1: String = "",
        @field:Attribute(name = "DateRange2", required = false) var dateRange2: String = "",
        @field:Attribute(name = "name", required = false) var name: String = "",
        @field:ElementList(inline = true, required = false) var list: ArrayList<Record> = ArrayList()
)

@Root(name = "Record", strict = false)
data class Record @JvmOverloads constructor(
        @field:Attribute(name = "Date", required = false) var date: String = "",
        @field:Attribute(name = "Id", required = false) var id: String = "",
        @field:Element(name = "Nominal", required = false) var nominal: Byte = 1,
        @field:Element(name = "Value", required = false) var value: String = "")


fun ValCurs.transformToDataBaseEntity(): List<CurrencyEntity> =
        list.mapIndexed { index, item -> CurrencyEntity(index.toLong(), item.date,
                convertStringToDouble(item.value))
        }