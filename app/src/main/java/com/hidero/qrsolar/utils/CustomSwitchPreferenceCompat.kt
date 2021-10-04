package com.hidero.qrsolar.utils

import android.content.Context
import androidx.preference.SwitchPreferenceCompat
import android.util.AttributeSet
import com.hidero.qrsolar.R

class CustomSwitchPreferenceCompat: SwitchPreferenceCompat{
    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
        layoutResource = R.layout.item_setting
    }
}