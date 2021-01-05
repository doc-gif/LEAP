package com.example.leap

import io.realm.RealmObject

open class Correct : RealmObject() {
    var number = 0
    var kindQuestion = 0
    var indexMeaning = 0
    var howManyCorrect = 0
}