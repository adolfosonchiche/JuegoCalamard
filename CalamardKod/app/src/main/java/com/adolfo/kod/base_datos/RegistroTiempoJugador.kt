package com.adolfo.kod.base_datos

import android.app.Activity
import android.database.Cursor
import android.util.Log
import com.adolfo.kod.MainActivity

class RegistroTiempoJugador {

    var id = 0
    lateinit var tiempo : String
    lateinit  var main : MainActivity

    lateinit var NombreListJugadores : ArrayList<String>
    lateinit var idJugador : ArrayList<Int>

    constructor(id: Int, tiempo: String) {

        this.id = id
        this.tiempo = tiempo
    }

    fun registrarMejorTiempo (es: Activity) {

        try {
            //nombreProducto.clear();
            println("..fdk..  ")
            var adminLite = AdminSQLiteOpenHelper(
                es, main.NOMBRE_DB
                , null, main.VERSION_DB
            )
            var baseDatos = adminLite.writableDatabase
            var cursor: Cursor = baseDatos.rawQuery("select * from jugador", null)

            if (cursor.moveToFirst()) {
                var ids: Int = cursor.getInt(0)
                //idJugador.indexOf(ids)
                idJugador.add(ids)//=ids

                NombreListJugadores.add(cursor.getString(1)) //= listOf(cursor.getString(1))

                while (cursor.moveToNext()) {
                    Log.e("c", "Tvv")
                    val num: Int = cursor.getInt(0)
                    idJugador.add(ids)//=ids
                    NombreListJugadores.add(cursor.getString(1))
                    //cursor.getString(1) + " ");
                    //cursor.getString(2) + " ");
                }
            } else {
                NombreListJugadores.add("no existe jugadores registrados")
            }
            baseDatos.close()
        } catch (e: Exception) {
            Log.e("error", "error en la obtencion de datos de los jugadores \n $e");
        }
    }


}