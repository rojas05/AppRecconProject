package com.rojasdev.apprecconproject.controller

object price {
    fun priceSplit (valor:Int,price: (String) -> Unit){
        val valorReversed = valor.toString().reversed()
        val valorReversedDiv = valorReversed.chunked(3)
        val valorFinal = valorReversedDiv.toString().replace("[","").replace("]","$")
        price(valorFinal.reversed())
    }
}