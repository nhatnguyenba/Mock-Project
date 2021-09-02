package com.example.test.setting.model

class Category() {
    var name_Cat: String? = null
    var des_cat: String? = null

    constructor(name_Cat: String, des_cat: String) : this () {
        this.name_Cat = name_Cat
        this.des_cat = des_cat
    }
}