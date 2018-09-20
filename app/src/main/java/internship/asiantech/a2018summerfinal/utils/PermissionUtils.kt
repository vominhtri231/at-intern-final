package internship.asiantech.a2018summerfinal.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log

/**
 * check permission of application
 *
 * @param activity where we need permission
 * @param permissions all permissions that we need
 * @param requestCode the code use to request permission
 *
 * @return True if permission granted , if not return false and request permission
 */
fun checkPermissions(activity: Activity, permissions: Array<String>, requestCode: Int): Boolean {
    var result = true
    val unGrantedPermissions = mutableListOf<String>()
    for (permission in permissions) {
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            result = false
            unGrantedPermissions.add(permission)
        }
    }
    if (unGrantedPermissions.isNotEmpty()) {
        val permissionArray = Array(unGrantedPermissions.size) { i -> unGrantedPermissions[i] }
        ActivityCompat.requestPermissions(activity, permissionArray, requestCode)
    }
    return result
}
