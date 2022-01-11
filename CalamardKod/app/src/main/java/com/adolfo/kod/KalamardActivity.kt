package com.adolfo.kod

import android.content.Intent
import android.media.MediaPlayer
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.adolfo.kod.cronometro.Time
import com.bumptech.glide.Glide


/**
 *
 * @author hectoradolfosc
 */
class KalamardActivity : AppCompatActivity() {

    lateinit var imgPlayer: ImageView
    lateinit var frameContainerMeta : FrameLayout
    lateinit var frameContaner : FrameLayout
    lateinit var imgSemaforo : ImageView
    lateinit var btnJugar : Button
    lateinit var tvCronometro : TextView
    lateinit var tvJugador : TextView

    private var xCurrent = 0f //indicar que sea de tipo flotante
    private var isActivateTouch = false
    var isTouchClick = false

    var widthImgPlayer : Int = 0
    var heigtImgPlayer : Int = 0
    var widthContainer : Int = 0
    var heigtContainer : Int = 0
    var limiteMeta : Int = 0
    lateinit var imgMeta : ImageView
    var isLlegasteMeta = false
    var isDeed = false
    var name = ""
    lateinit  var id :String
    lateinit var play : String

    lateinit var myTaskRed : KalamardActivity.MyTaskRed
    lateinit var myTaskGreen : KalamardActivity.MyTaskGreen
    // lateinit var cronometro : RelojLimiteTiempo
    lateinit var  time : Time

    //linear Lauout android:background="@drawable/background_game"
    lateinit var campo : LinearLayout

    //parametros para el audio
    var sonidoReproduccion = 0
    lateinit  var mp: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kalamard)

        imgPlayer = findViewById(R.id.img_player)
        frameContaner = findViewById(R.id.frame_contenedor)
        frameContainerMeta = findViewById(R.id.frame_contenedor_meta)
        imgSemaforo = findViewById(R.id.img_semaforo)
        btnJugar = findViewById(R.id.boton_jugar)
        tvCronometro = findViewById(R.id.tv_cronometro)
        tvJugador = findViewById(R.id.nombre_jugado_activo)

        campo = findViewById(R.id.campo_id)

        name = getIntent().getStringExtra("n").toString();
        id = getIntent().getStringExtra("id").toString()
        play = getIntent().getStringExtra("play").toString()

        Log.e("PLAY-------", "play: $play")
        Log.e("ID-------", "play: $id")


        obtenerCampo()
        animationStop()

        tvJugador.text = "jugador@: ${name}"

        //listenerOnclickPrueba()
        getParamFrameContainer()
        listener()
        //animateView()
        myTaskRed = MyTaskRed()
        myTaskGreen = MyTaskGreen()
        inTaskRed()

        /*/cronometro
        cronometro = RelojLimiteTiempo(0, 0, 0)
        cronometro.addObserver(this)
        var t = Thread(cronometro)
        t.start()*/
        time = Time()
        time.time(tvCronometro)
        time.timeRun()

        audioMediaPlayer()
        //onBackPressed()

    }

    //metodo para el boton regresar de la barra de navegacion del movil
    //accedemos al metodo para terminar el audio y la acrtividad
    override fun onBackPressed() {
        super.onBackPressed()
        try {
            mp.stop()
        } catch (e: Exception) {

        }
        finish()
    }

    //funciones para el audio


    //metodo para el boton MediaPlayer sonido largo cancion
    fun audioMediaPlayer() {

        try {
            mp.stop()
        } catch (e: Exception) {

        }
        //mp.stop()


        if (play.equals("1")) {

              //  mp!!.stop()

            mp = MediaPlayer.create(this, R.raw.audio_juego2)
            mp!!.start()
        } else if (play.equals("3") || play.equals("2")){
            //     mp!!.stop()

            mp = MediaPlayer.create(this, R.raw.music_naruto)
            mp!!.start()
        } else {
           //     mp!!.stop()

            mp = MediaPlayer.create(this, R.raw.music_seya)
            mp!!.start()
        }
    }

    //crear el campo del juego
    private fun obtenerCampo() {

        if (play.equals("1")) {
            campo.setBackgroundResource(R.drawable.campo_goku)
        } else if (play.equals("2")) {
            campo.setBackgroundResource(R.drawable.camop_naruto)
        } else if (play.equals("3")) {
            campo.setBackgroundResource(R.drawable.campo_sasuke)
        } else {
            campo.setBackgroundResource(R.drawable.background_game)
        }
    }

    /* fun listenerOnclickPrueba(){
        frameContaner.setOnClickListener() {
            Glide.with(this).asGif().load(R.raw.amoung_us_red).into(imgPlayer)
        }
    }*/

    //funcion para reproducir el gif para jugar correr
    private fun animationWolk() {

        if (play.equals("1")) {
            Glide.with(this).asGif().load(R.raw.goku_move).into(imgPlayer)
        } else if (play.equals("2")) {
            Glide.with(this).asGif().load(R.raw.naruto_run).into(imgPlayer)
        } else if (play.equals("3")) {
            Glide.with(this).asGif().load(R.raw.sasuke_move).into(imgPlayer)
        } else {
            Glide.with(this).asGif().load(R.raw.seya_move).into(imgPlayer)
        }
    }

    //funcion para reproducir el gif de ganador del juego
    private fun animationWin() {
        Glide.with(this).asGif().load(R.raw.win).into(imgPlayer)
    }

    //funcion para detener el gif y agregar la imagen statica
    private  fun animationStop() {

        if (play.equals("1")) {
            imgPlayer.setImageResource(R.drawable.goku_puse)
        } else if (play.equals("2")) {
            Glide.with(this).asGif().load(R.raw.naruto_pause).into(imgPlayer)
        } else if (play.equals("3")) {
            imgPlayer.setImageResource(R.drawable.sasuke_akaski)
        } else {
            imgPlayer.setImageResource(R.drawable.seya)
        }

       // imgPlayer.setImageResource(R.drawable.jugador)
    }

    //funcion para activiar los eventos del imagePlayer
    private fun listener(){
        frameContaner.setOnTouchListener { view, event ->
            //se retorna verdadero para hacer que cada evento termine y empieza otro de lo contrario
            //no se van a respetar y se creara un bucle infinito
            if (event.action == MotionEvent.ACTION_UP) {
                isActivateTouch = false
                isTouchClick = false
                animationStop()
                true
            } else if ( event.action == MotionEvent.ACTION_DOWN) {
                isActivateTouch = true
                isTouchClick = true
                animationWolk()
                //animationStop()
                animateView()
                true
            }
            Log.e("as", "cxg")
            true
        }
    }

    //funcion para mover la imagen o animarlok
    fun animateView () {
        //va iniciar en cero XCurrent y avanzara 3 pixeles en X
        var amin: Animation = TranslateAnimation(xCurrent, xCurrent + 3, 0f, 0f)
        amin.duration= 10 //1 milisegundo
        amin.fillAfter= true //
        amin.isFillEnabled = true

        amin.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationStart(p0: Animation?) {
                //este esvento  sirve para cuando se inicia la animacion
                //se puede agregar un mensaje o algo mas de inicio
            }

            override fun onAnimationRepeat(p0: Animation?) {
                //la logica no se coloca aqui porque existe celulares que ya no reconocen este metodo
            }

            override fun onAnimationEnd(p0: Animation?) {
                //indicamos que cuando se presione el touch del celular la imagen va avanzar
                // 3 pixeles cada 1 milisegundo
                imgPlayer.x = imgPlayer.x + 3
                imgPlayer.clearAnimation()

                Log.e("player x", " ${imgPlayer.x}")
                //si la imagen llega a una parte en especifica de la pantalla (3/4 partes)

                if (imgPlayer.x >= limiteMeta) {
                    Log.e("ganaste", "llegaste a la meta")
                    isLlegasteMeta = true
                    isActivateTouch = false
                    //isTouchClick = false
                    frameContaner.setOnTouchListener(null) //desstruir el evento de al llegar a la meta, es decir ya no va a avanzar la imagen
                    mensajeGanaste()
                    animationWin()
                    nuevoJuego()


                }

                if (isActivateTouch) {  //se verifica si esta presionado o no
                    imgPlayer.startAnimation(amin)
                }
            }
        })

        imgPlayer.startAnimation(amin)
    }

    //funcion para obtener las coordenadas de la imagen y el tamaño
    fun getParamFrameContainer() {

        //para conocer la longitud de imagen o las coordenada dende se encuentra
        var vtnImgPlayer : ViewTreeObserver = imgPlayer.getViewTreeObserver()
        vtnImgPlayer.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                //es para quitar este listener y solo ver este mensaje en consola una vez
                imgPlayer.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                //variables de la imagen como la imagen se creo desde xml entonces no importa la poscion inicial
                //siempre sera (0,0) a diferencia si se hubiera creado en java o kotlin
                widthImgPlayer = imgPlayer.width
                heigtImgPlayer = imgPlayer.height

                //imprimir en consola con kotlin
                Log.e("Whidth IMG PLAYER", "whidth:  $widthImgPlayer")
                Log.e("heigth IMG PLAYER", "heigth:  $heigtImgPlayer")

                //definimos la coordenada de la meta
                limiteMeta = (widthContainer / 2) + (widthImgPlayer / 2)

                addViewMetaContainer()

            }
        })


        var vtoFrameContainer : ViewTreeObserver = frameContaner.getViewTreeObserver()

        vtoFrameContainer.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                frameContaner.getViewTreeObserver().removeOnGlobalLayoutListener(this)

                //obtenemos las coordenadas del contenedor donde se encuentra la imagen
                widthContainer = frameContaner.width
                heigtContainer = frameContaner.height
                //imprimir en consola con kotlin
                Log.e("Whidth CONTAINER", "whidth:  $widthContainer")
                Log.e("heigth CONTAINER", "heigth:  $heigtContainer")
            }


        })



    }

    //imagen superpuesta en el container para indicar la meta
    fun addViewMetaContainer() {
        imgMeta = ImageView(this)
        imgMeta.layoutParams = ViewGroup.LayoutParams(widthImgPlayer, heigtImgPlayer)
        imgMeta.x = limiteMeta.toFloat() -2
        imgMeta.y =  heigtContainer - heigtImgPlayer.toFloat()
        imgMeta.setImageResource(R.drawable.ic_meta)
        frameContainerMeta.addView(imgMeta)

    }


    //clase para la tarea asincrona que se ejecuta en cada tiempo determinado
    inner class  MyTaskRed : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            //imgSemaforo.setImageResource(R.drawable.semaforo_rojo)

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!isDeed) {
                //colocamos la imagen de color verde
                //myTaskRed.cancel(true)
                imgSemaforo.setImageResource(R.drawable.semaforo_verde)
                inTaskGreen()
            } else {
                //  Toast.makeText(null, "Perdiste...!!", Toast.LENGTH_LONG).show()
                nuevoJuego()
            }
        }
        override fun doInBackground(vararg p0: String?): String {
            //agregar un evento (en este caso es para el touch para ver si esta presionado)

            var l = 0

            while (l <= 3000) {
                Thread.sleep(100)
//                tvCronometro.text = cronometro.tiempo
                if(isTouchClick) {
                    //perdio
                    Log.e("perdio", "perdiste ")

                    //ejecutar en tiempo de hilo los cambios de interfacez
                    runOnUiThread {
                        isDeed = true
                        isActivateTouch = false
                        imgPlayer.clearAnimation()
                        imgPlayer.setImageResource(R.drawable.cry)
                        frameContaner.setOnTouchListener(null)

                        mensajePerdiste()
                    }
                }

                try {
                    if(l == 1000 || l == 2000 || l == 3000) {
                        time.timeRun()//(tvCronometro)
                        tvCronometro.text = time.getTiempo()
                    }
                } catch (e: Exception) {
                    print(e)
                }
                l += 100
            }

            return ""

        }

    }


    fun inTaskRed () {

        if (myTaskGreen != null) {
            myTaskGreen.cancel(true)
        }
        if (myTaskRed != null) {
            myTaskRed.cancel(true)
            myTaskRed = MyTaskRed()
        }
        myTaskRed.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }


    //clase para la tarea asincrona que se ejecuta en cada tiempo determinado
    inner class  MyTaskGreen : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            //imgSemaforo.setImageResource(R.drawable.semaforo_rojo)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!isLlegasteMeta) {
                //colocamos la imagen de color verde
                //  myTaskGreen.cancel(true)
                imgSemaforo.setImageResource(R.drawable.semaforo_rojo)
                inTaskRed()
            }
        }
        override fun doInBackground(vararg p0: String?): String {
            //agregar un evento (en este caso es para el touch para ver si esta presionado)

            var l = 0


            while (l <= 2000) {
                Thread.sleep(100)
//                tvCronometro.text = cronometro.tiempo
                if (isLlegasteMeta) {
                    //ganaste
                    this.cancel(true)
                    // Toast.makeText(null, "Felicidades, Ganaste!!!", Toast.LENGTH_LONG).show()
                    //   btnJugar.visibility = View.VISIBLE
                    // nuevoJuego()
                }
                Log.e("puede avanzar", "sigue avanzando")


                try {
                    if(l == 1000 || l == 2000 || l == 3000) {
                        time.timeRun()//(tvCronometro)
                        tvCronometro.text = time.getTiempo()
                    }
                } catch (e: Exception) {
                    print(e)
                }
                l += 100
            }


            return ""

        }

    }


    fun mensajePerdiste() {
        Toast.makeText(this, "PERDISTE... intenta de nuevo", Toast.LENGTH_LONG).show()
    }

    fun mensajeGanaste() {
        Toast.makeText(this, "FELICITACIONES GANASTE-...!!", Toast.LENGTH_LONG).show()
    }

    fun inTaskGreen () {
        if (myTaskRed != null) {
            myTaskRed.cancel(true)
        }

        if (myTaskGreen != null) {
            myTaskGreen.cancel(true)
            myTaskGreen = MyTaskGreen()
        }
        myTaskGreen.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    fun reloadGame() {
        var intent = Intent(this, KalamardActivity::class.java)
        intent.putExtra("n", name)
        intent.putExtra("id", id)
        intent.putExtra("play", play)
        mp!!.stop()
        startActivity(intent)
        finish()
    }

    fun nuevoJuego() {
        btnJugar.visibility = View.VISIBLE

        btnJugar.setOnClickListener() {
            reloadGame()
        }

    }

    /*override fun update(p0: Observable?, p1: Any?) {
        //aqui se sebe de agregar el label para que aparezca el cronometro
        tvCronometro.text = p1.toString()
    }*/

}
