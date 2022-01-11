package com.adolfo.kod

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import java.lang.Exception


/**
 *
 * @author hectoradolfosc
 */
class kodPlayer : AppCompatActivity() {

    lateinit var imgGoku : ImageView
    lateinit var imgNaruto : ImageView
    lateinit var imgSasuke : ImageView
    lateinit var imgSeya : ImageView

    var name = ""
    lateinit  var id :String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kod_player)

        //obtenemos los parametros de la activity anterior
        name = getIntent().getStringExtra("n").toString();
        id = getIntent().getStringExtra("id").toString()

        imgGoku = findViewById(R.id.img_goku)
        imgNaruto = findViewById(R.id.img_naruto)
        imgSasuke = findViewById(R.id.img_sasuke)
        imgSeya = findViewById(R.id.img_seya)

       /* imgGoku.setImageResource(R.drawable.goku)
//        imgNaruto.setImageResource(R.drawable.naruto)
        imgSasuke.setImageResource(R.drawable.sasuke)
        imgSeya.setImageResource(R.drawable.seya)*/

        nuevoJuego()




    }



    fun reloadGame(p: String) {
        var intent = Intent(this, KalamardActivity::class.java)
        intent.putExtra("n", name)
        intent.putExtra("id", id)
        intent.putExtra("play", p)
        startActivity(intent)
       // finish()
    }

    fun nuevoJuego() {

        imgGoku.setOnClickListener() {
            reloadGame("1")
        }

        imgNaruto.setOnClickListener() {
            reloadGame("2")
        }

        imgSasuke.setOnClickListener() {
            reloadGame("3")
        }

        imgSeya.setOnClickListener() {
            reloadGame("4")
        }

    }


   /* fun goku(view : View) {


        val intent = Intent(this, KalamardActivity ::class.java)
        intent.putExtra("n", "Goku")
        intent.putExtra("id", "12")

        finish()
        startActivity(intent)
    }*/

}