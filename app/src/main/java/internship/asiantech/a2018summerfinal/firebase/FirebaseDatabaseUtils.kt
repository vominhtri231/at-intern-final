package internship.asiantech.a2018summerfinal.firebase

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import internship.asiantech.a2018summerfinal.model.SingletonUser
import internship.asiantech.a2018summerfinal.model.User

object FirebaseDatabaseUtils {
    private const val TABLE_NAME = "Users"
    private val database = FirebaseDatabase.getInstance().reference

    fun getCurrentUser(mail: String, updater: DatabaseUpdater) {
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childDataSnapshot: DataSnapshot in dataSnapshot.children) {
                    val user = childDataSnapshot.getValue(User::class.java)
                    if (user?.mail == mail) {
                        SingletonUser.instance.idUser = user.idUser
                        SingletonUser.instance.mail = user.mail
                        SingletonUser.instance.name = user.name
                        SingletonUser.instance.password = user.password
                        SingletonUser.instance.age = user.age
                        SingletonUser.instance.avatar = user.avatar
                        SingletonUser.instance.latitude = user.latitude
                        SingletonUser.instance.longitude = user.longitude
                        updater.onComplete(user)
                        break
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TTT", error.message)
            }
        }

        database.child(TABLE_NAME).addListenerForSingleValueEvent(listener)
    }

    fun addUser(user: User) {
        database.child(TABLE_NAME).push().key?.let {
            user.idUser = it
            database.child(TABLE_NAME).child(it).setValue(user)
        }
    }
}
