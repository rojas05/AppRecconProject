package com.rojasdev.apprecconproject.controller

import androidx.recyclerview.widget.RecyclerView

object scrolling {
    private var scrollingUp = false
    private var scrollingDown = false
    fun scrolling(rv: RecyclerView, scroll:(String)-> Unit){
        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // Se llama cuando el RecyclerView se desplaza
                if (dy > 0) {
                    scrollingUp = false
                    if (!scrollingDown) {
                        scroll("down")
                        scrollingDown = true
                    }
                } else {
                    scrollingDown = false
                    if (!scrollingUp) {
                        scroll("up")
                        scrollingUp = true
                    }
                }
            }
        }

        rv.addOnScrollListener(scrollListener as RecyclerView.OnScrollListener)
    }

}