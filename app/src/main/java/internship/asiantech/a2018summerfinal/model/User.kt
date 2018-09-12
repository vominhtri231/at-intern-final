package internship.asiantech.a2018summerfinal.model

class User {
    var idUser: String = ""
    var mail: String = ""
    var name: String = ""
    var password: String = ""
    var age: Int = 0
    var avatar: String = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    constructor()

    constructor(idUser: String, mail: String, name: String, password: String, age: Int, avatar: String, latitude: Double, longitude: Double) {
        this.idUser = idUser
        this.mail = mail
        this.name = name
        this.password = password
        this.age = age
        this.avatar = avatar
        this.latitude = latitude
        this.longitude = longitude
    }
}
