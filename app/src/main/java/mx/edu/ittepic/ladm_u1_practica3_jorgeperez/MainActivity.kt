package mx.edu.ittepic.ladm_u1_practica3_jorgeperez

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    val vector: Array<Int> = Array(10, { 0 })
    var posicion = 0
    var valores = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Botón de permisos
        btnPermisos.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
            ) {
                // SI ENTRA EN ESTE IF, ES PORQUE NO TIENE LOS PERMISOS
                // EL SIGUIENTE CÓDIGO SOLICITA LOS PERMISOS
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), 0
                )
            } else {
                mensaje("PERMISOS YA OTORGADOS")
            }
        }

        // Botón para asignar al vector
        btnAsignar.setOnClickListener {
            insertarVector()
        }

        // Botón para mostrar el vector
        btnMostrar.setOnClickListener {
            mostrarTodo()
        }

        // Botón para guardar en la SD
        btnGuardar.setOnClickListener {
            guardarArchivoSD()
        }

        // Botón para leer desde la SD
        btnLeer.setOnClickListener {
            leerArchivoSD()
        }
    }

    private fun insertarVector() {
        if (txtValor.text.isEmpty() || txtPosicion.text.isEmpty()) {
            mensaje("Error todos los campos deben ser llenados. (Posición y Valor")
            return

        }
        var value = txtValor.text.toString().toInt()
        var posicion = txtPosicion.text.toString().toInt()
        vector[posicion] = value
        limpiarCampos()
        mensaje("Se insertó al vector")
    }

    // Función para mostrar el vector
    fun mostrarTodo() {
        valores = ""

        (0..9).forEach {
            valores = valores + vector[it]
            if(it<9)
                valores = valores + ","
        }

        mensaje(valores)
    }

    // Función para guardar en la SD
    fun guardarArchivoSD() {
        if (noSD()) {
            mensaje("NO TIENE MEMORA EXTERNA")
            return
        }
        try {
            var rutaSD = Environment.getExternalStorageDirectory()
            var data = txtNombreArchivo.text.toString()

            var flujo = File(rutaSD.absolutePath, data)
            var flujoSalida = OutputStreamWriter(FileOutputStream(flujo))

            flujoSalida.write(valores)
            flujoSalida.flush()
            flujoSalida.close()
            mensaje("Se guardó correctamente el vector en un archivo")
            limpiarCampos()
        } catch (error: IOException) {
            mensaje(error.message.toString())
        }
    }

    // Función para leer archivo desde la SD
    fun leerArchivoSD() {
        var nombreleer = txtArchivoLeer.text.toString()

        if (noSD()) {
            mensaje("NO TIENE MEMORIA EXTERNA")
            return
        }

        try
        {
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath, nombreleer)
            var flujoEntrada = BufferedReader(InputStreamReader(FileInputStream(datosArchivo)))
            var data = flujoEntrada.readLine()

            mensaje(data)

            var valoresSD = data.split(",")

            (0..9).forEach {
                vector[it] = valoresSD[it].toInt()
            }


            flujoEntrada.close()

        }catch (error:IOException) {
            mensaje(error.message.toString())
        }
    }

    // Limpiar los campos
    private fun limpiarCampos() {
        txtValor.setText("")
        txtPosicion.setText("")
        txtNombreArchivo.setText("")
        txtArchivoLeer.setText("")
    }

    // Verificar que se tenga una memoria SD
    fun noSD() : Boolean{
        var estado = Environment.getExternalStorageState()
        if(estado != Environment.MEDIA_MOUNTED){
            return true
        }
        return false
    }

    // Función para mostrar un mensaje
    fun mensaje(m:String){
        AlertDialog.Builder(this)
            .setTitle("ATENCIÓN")
            .setMessage((m))
            .setPositiveButton("OK"){d,i->}
            .show()

    }
}
