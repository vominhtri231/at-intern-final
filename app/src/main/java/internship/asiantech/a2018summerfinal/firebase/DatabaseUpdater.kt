package internship.asiantech.a2018summerfinal.firebase

import internship.asiantech.a2018summerfinal.model.User

interface DatabaseUpdater {
    fun onComplete(user: User)
}
