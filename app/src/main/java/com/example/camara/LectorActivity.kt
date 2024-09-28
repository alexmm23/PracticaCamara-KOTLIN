package com.example.camara

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.zxing.integration.android.IntentIntegrator

class LectorActivity : AppCompatActivity() {
    private lateinit var codigo: EditText
    private lateinit var descripcion: EditText
    private lateinit var precio: EditText
    private lateinit var nombre: EditText
    private lateinit var btnEscanear: Button
    private lateinit var btnCapturar: Button
    private lateinit var btnLimpiar: Button
    private lateinit var btnBuscar: Button

    //Crear un array de objectos para guardar los datos capturados
    private val productos = ArrayList<Producto>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lector)
        codigo = findViewById(R.id.edtCodigo)
        descripcion = findViewById(R.id.edtDescripcion)
        precio = findViewById(R.id.edtPrecio)
        nombre = findViewById(R.id.edtNombreProducto)
        btnEscanear = findViewById(R.id.btnLector)
        btnCapturar = findViewById(R.id.btnCapturar)
        btnLimpiar = findViewById(R.id.btnLimpiar)
        btnBuscar = findViewById(R.id.btnBuscar)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnEscanear.setOnClickListener {
            escanear()
        }
        btnCapturar.setOnClickListener {
            capturar()
        }
        btnLimpiar.setOnClickListener {
            limpiar()
        }
        btnBuscar.setOnClickListener {
            buscar()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    fun escanear() {
        val intentIntegrator = IntentIntegrator(this@LectorActivity)
        intentIntegrator.setBeepEnabled(false)
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        intentIntegrator.setPrompt("Escanear código")
        intentIntegrator.setCameraId(0)
        intentIntegrator.setBarcodeImageEnabled(true)
        intentIntegrator.initiateScan()
    }

    fun capturar() {
        if (codigo.text.toString().isNotEmpty() && descripcion.text.toString()
                .isNotEmpty() && precio.text.toString().isNotEmpty() && nombre.text.toString()
                .isNotEmpty()
        ) {
            Toast.makeText(this, "Datos capturados", Toast.LENGTH_SHORT).show()
            productos.add(
                Producto(
                    codigo = codigo.text.toString(),
                    descripcion = descripcion.text.toString(),
                    precio = precio.text.toString().toDouble(),
                    nombre = nombre.text.toString()
                )
            )
            limpiar()
            Log.d("Productos", productos.toString())
        } else {
            Toast.makeText(this, "Debe registrar datos", Toast.LENGTH_LONG).show()
        }
    }

    fun limpiar() {
        codigo.setText("")
        descripcion.setText("")
        precio.setText("")
        nombre.setText("")
    }

    fun buscar() {
        if (codigo.text.toString().isNotEmpty()) {
            Toast.makeText(this, "Buscando código", Toast.LENGTH_SHORT).show()
            Log.d("Productos", productos.toString())
            Log.d("Código", codigo.text.toString())

            val producto = productos.find { it.codigo == codigo.text.toString() }
            if (producto != null) {
                descripcion.setText(producto.descripcion)
                precio.setText(producto.precio.toString())
                nombre.setText(producto.nombre)
            } else {
                Toast.makeText(this, "Código no encontrado", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Debe ingresar código", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (intentResult != null) {
            if (intentResult.contents == null) {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Escaneado: ${intentResult.contents}", Toast.LENGTH_SHORT)
                    .show()
                codigo.setText(intentResult.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}