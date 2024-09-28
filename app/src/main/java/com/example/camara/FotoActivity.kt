package com.example.camara

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar

class FotoActivity : AppCompatActivity() {
    private lateinit var foto: ImageView
    private lateinit var btnTomar: Button
    private var bitmap: Bitmap? = null
    private lateinit var guardar: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foto)
        val toolbar: Toolbar = findViewById(R.id.toolbar2)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        foto = findViewById(R.id.imageView)
        btnTomar = findViewById(R.id.btnTomarFoto)
        btnTomar.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            responseLauncher.launch(intent)
        }
        guardar = findViewById(R.id.btnGuardarFoto)
        guardar.setOnClickListener {
            if (bitmap == null) {
                Toast.makeText(this, "No hay foto para guardar", Toast.LENGTH_SHORT).show()
            } else {
                guardarImagen(bitmap!!)
            }
        }
    }

    private val responseLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == RESULT_OK) {
                Toast.makeText(this, "Foto tomada", Toast.LENGTH_SHORT).show()
                val extras = activityResult.data?.extras
                val imageBitmap = extras?.get("data") as Bitmap
                foto.setImageBitmap(imageBitmap)
                bitmap = imageBitmap
                //guardarImagen(imageBitmap)
            } else {
                Toast.makeText(this, "Foto no tomada", Toast.LENGTH_SHORT).show()
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

    private fun guardarImagen(bitmap: Bitmap) {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "imagen_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val resolver = contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        uri?.let {
            resolver.openOutputStream(uri).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream ?: return@use)
            }
            values.clear()
            values.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(uri, values, null, null)
            Toast.makeText(this, "Imagen guardada", Toast.LENGTH_SHORT).show()
        } ?: run {
            Toast.makeText(this, "Error al guardar la imagen", Toast.LENGTH_SHORT).show()
        }
    }
}