package com.example.memo

data class rvModel(val title : String,val content : String,val notesKey : String?){
    constructor():this("","","")
}
