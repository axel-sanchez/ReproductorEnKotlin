package com.axel.reproductorenkotlin

class Cancion(cancion: Int, portada: Int, nombre:String){ //#353333---Snookplay
    private var cancion: Int = 0
    private var portada: Int = 0
    private var nombre: String = ""

    init {
        this.cancion = cancion
        this.portada = portada
        this.nombre = nombre
    }

    fun getCancion():Int = cancion
    fun getPortada():Int = portada
    fun getNombre():String = nombre
}


/*class Cancion<int>(cancion: int, portada: Int, nombre:String){ //#353333---Snookplay
    private var cancion: int? = null
    private var portada: Int = 0
    private var nombre: String = ""

    init {
        this.cancion = cancion
        this.portada = portada
        this.nombre = nombre
    }

    fun getCancion() = {cancion}
    fun getPortada() = {portada}
    fun getNombre() = {nombre}
}*/

