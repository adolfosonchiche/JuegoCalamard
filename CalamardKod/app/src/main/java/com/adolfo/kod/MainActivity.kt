package com.adolfo.kod

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import com.adolfo.kod.base_datos.AdminSQLiteOpenHelper
import java.util.*
import kotlin.collections.ArrayList


/**
 *
 * @author hectoradolfosc
 */
class MainActivity : AppCompatActivity()/*, Observer*/ {

    lateinit var btnNewJugador : Button
    lateinit var btnRankin : Button
    lateinit var txtNamePlayer : EditText
    lateinit var listPlayers : ListView
    final val NOMBRE_DB: String = "VX2"
    final var VERSION_DB = 1
    private lateinit var nombreJugador : String
    lateinit  var idsJugador  :String
    lateinit var NombreListJugadores : ArrayList<String>
    lateinit var idJugador : ArrayList<Int>





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnNewJugador = findViewById(R.id.btn_crear_jugador)
        btnRankin = findViewById(R.id.btn_rankin)
        txtNamePlayer = findViewById(R.id.name_jugador)
        listPlayers = findViewById(R.id.list_jugadores)

        btnNewJugador.setOnClickListener() {
            sig()
        }

        NombreListJugadores = ArrayList()
        idJugador = ArrayList()
        mostrarDatos()


//mostrar datos en el listView
        val adapterList: Any? =
            ArrayAdapter<Any?>(this, R.layout.list_view_plauer, NombreListJugadores as List<Any?>)
        listPlayers.setAdapter(adapterList as ListAdapter?)


        //funcionalidad del listVIew cuando se selecciona un nombre u opcion
        listPlayers.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
            try {
                nombreJugador = NombreListJugadores.get(position);
                idsJugador = idJugador.get(position).toString();
                jugar(nombreJugador, idsJugador);
            }catch (e: java.lang.Exception) {

            }


        })

    }



    fun jugar(n: String, id: String) {
        val intent = Intent(this, kodPlayer ::class.java)
        intent.putExtra("n", n)
        intent.putExtra("id", id)
        //finish()
        startActivity(intent)
    }

    fun mostrarDatos() {
        try {
            //nombreProducto.clear();
            println("..fdk..  ")
            var adminLite = AdminSQLiteOpenHelper(
                this,
                NOMBRE_DB, null, VERSION_DB
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

    fun sig() {

        var name = txtNamePlayer.text.toString()

        if (name.isEmpty()) {
            Toast.makeText(
                this,
                "Debes de escribir un nombre para identificarte..!! intenta de nuevo",
                Toast.LENGTH_LONG
            ).show()
        } else {
            registrarJugador(name)
            var intent = Intent(this, kodPlayer::class.java)
            intent.putExtra("n", nombreJugador)
            intent.putExtra("id", idsJugador)
            txtNamePlayer.setText("")
            startActivity(intent)
            //finish()
        }
    }

    fun registrarJugador(nam: String) {

        val adminLite = AdminSQLiteOpenHelper(
            this,
            NOMBRE_DB, null, VERSION_DB
        )
        val baseDatos = adminLite.writableDatabase
       // jugador(codigo INTEGER PRIMARY KEY AUTOINCREMENT, nombre text, tiempo text, ganados real, perdidos real)")

        val registro = ContentValues()
        registro.put("nombre", nam)
        registro.put("tiempo", "00:00:00")
        registro.put("ganados", 0)
        registro.put("perdidos", 0)
        //guardamos los datos en la tabla
        //guardamos los datos en la tabla
        baseDatos.insert("jugador", null, registro)

        nombreJugador = nam

        idsJugador = (idJugador.size + 1).toString()

        baseDatos.close()
        Toast.makeText(this, "se creo Exitosamente!!", Toast.LENGTH_SHORT).show()


    }


}