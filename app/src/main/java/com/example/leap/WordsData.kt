package com.example.leap

import io.realm.RealmObject

open class WordsData : RealmObject() {
    var number = 0
    var word = ""
    var meaningFirst = ""
    var meaningSecond = ""
    var meaningThird = ""
    var meaningFourth = ""
    var exampleEngFirst = ""
    var exampleEngSecond = ""
    var exampleEngThird = ""
    var exampleEngFourth = ""
    var exampleJpnFirst = ""
    var exampleJpnSecond = ""
    var exampleJpnThird = ""
    var exampleJpnFourth = ""
}