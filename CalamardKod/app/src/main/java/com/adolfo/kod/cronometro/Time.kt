package com.adolfo.kod.cronometro

import android.widget.TextView


/**
 *
 * @author hectoradolfosc
 */
class Time  {

    private lateinit var tvCronometro : TextView

    fun time(cr: TextView) {

        tvCronometro = cr

    }


    private var horas = 0
    private var minutos = 0
    private var segundos = 0
    private var tiempo: String? = null



    fun  timeRun()  {
        try {
         //   while (true) {
                tiempo = ""
                if (horas < 10) {
                    tiempo += "0$horas"
                } else {
                    tiempo += horas
                }
                tiempo += ":"
                if (minutos < 10) {
                    tiempo += "0$minutos"
                } else {
                    tiempo += minutos
                }
                tiempo += ":"
                if (segundos < 10) {
                    tiempo += "0$segundos"
                } else {
                    tiempo += segundos
                }
                segundos++
                if (segundos == 60) {
                    minutos++
                    segundos = 0
                    if (minutos == 60) {
                        horas++
                        minutos = 0
                    }
                }
           // }
            tvCronometro.text = "tiempo: $tiempo"
        } catch (e: Exception) {
            println("ERRORS: $e")
        }
    }

    fun getHoras(): Int {
        return horas
    }

    fun getMinutos(): Int {
        return minutos
    }

    fun getSegundos(): Int {
        return segundos
    }

    fun getTiempo(): String? {
        return tiempo
    }

}