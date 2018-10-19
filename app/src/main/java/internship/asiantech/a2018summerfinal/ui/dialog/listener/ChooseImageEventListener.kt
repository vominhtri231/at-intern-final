package internship.asiantech.a2018summerfinal.ui.dialog.listener

import internship.asiantech.a2018summerfinal.ui.dialog.ChooseImageDialog

interface ChooseImageEventListener {
    fun open(type: ChooseImageDialog.ChosenImageType)
}
