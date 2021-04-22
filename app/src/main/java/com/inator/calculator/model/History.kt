package com.inator.calculator.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class History(
    @ColumnInfo(name = "Expression")
    var expr: String,

    @ColumnInfo(name = "Answer")
    var answer: String,

    @ColumnInfo(name = "Date")
    var date: String,

    @ColumnInfo(name = "Time")
    var time: String

) {
    @PrimaryKey(autoGenerate = true)
    var eid = 0

}