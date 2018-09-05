package internship.asiantech.a2018summerfinal.model

class SingletonUser private constructor() {
    private object Holder {
        val INSTANCE = SingletonUser()
    }

    companion object {
        val instance: SingletonUser by lazy { Holder.INSTANCE }
    }

    var idUser: String? = null
    var mail: String? = null
    var name: String? = null
    var password: String? = null
    var age: Int = 0
    var avatar: String? = null
    var latitude: Double = 0.0
    var longitude: Double = 0.0
}
