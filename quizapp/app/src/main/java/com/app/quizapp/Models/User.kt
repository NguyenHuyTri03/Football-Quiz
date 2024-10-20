package com.app.quizapp.Models

class User {
    var id : String ?= null
        get() = field
        public set(value) { field = value }

    var name : String ?= null
        get() = field
        public set(value) { field = value }

    var email : String ?= null
        get() = field
        set(value) { field = value }


    var password : String ?= null
        get() = field
        set(value) { field = value }

    var easy_score : Long ?= null
        get() = field
        set(value) { field = value }

    var normal_score : Long ?= null
        get() = field
        set(value) { field = value }

    var hard_score : Long ?= null
        get() = field
        set(value) { field = value }


}