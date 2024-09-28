package com.example.camara

class Producto(
    var codigo: String = "",
    var nombre: String = "",
    var descripcion: String = "",
    var precio: Double = 0.0
) {
    fun capturar(codigo: String, nombre: String, descripcion: String, precio: Double) {
        this.codigo = codigo
        this.nombre = nombre
        this.descripcion = descripcion
        this.precio = precio
    }
}