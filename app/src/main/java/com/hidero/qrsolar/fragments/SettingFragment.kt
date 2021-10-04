package com.hidero.qrsolar.fragments


import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import androidx.preference.SwitchPreferenceCompat
import android.util.Log
import android.view.View
import android.widget.Toast
import com.hidero.qrsolar.R


class SettingFragment : PreferenceFragmentCompat() {
    private lateinit var sound: SwitchPreferenceCompat
    private lateinit var vibrate: SwitchPreferenceCompat
    private lateinit var saveHistory: SwitchPreferenceCompat
    private lateinit var removeAds: SwitchPreferenceCompat

    //    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return super.onCreateView(inflater, container, savedInstanceState)
//        var view = super.onCreateView(inflater, container, savedInstanceState);
//        view!!.setBackgroundColor(getResources().getColor(R.color.colorbg));
//        return view;
//    }


    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
        addPreferencesFromResource(com.hidero.qrsolar.R.xml.preferences)
//        var preference = Preference(activity)

        sound = findPreference("sound") as SwitchPreferenceCompat
        vibrate = findPreference("vibrate") as SwitchPreferenceCompat
        saveHistory = findPreference("save_history") as SwitchPreferenceCompat
        removeAds = findPreference("remove_ads") as SwitchPreferenceCompat
        sound.setOnPreferenceChangeListener { preference, any ->
            if (!sound?.isChecked) {
                Toast.makeText(activity, "Turn on Sound", Toast.LENGTH_LONG).show()

                Log.d("ABC", "sound is${sound.isChecked}")
            } else {
                Toast.makeText(activity, "Turn off Sound", Toast.LENGTH_LONG).show()
//                    Log.d("ABC", "No")
            }
            true
        }
        vibrate.setOnPreferenceChangeListener { preference, any ->
            if (!vibrate?.isChecked) {
                Toast.makeText(activity, "Turn on vibrate", Toast.LENGTH_LONG).show()

//                    Log.d("ABC", "No")
            } else {
                Toast.makeText(activity, "Turn off vibrate", Toast.LENGTH_LONG).show()
//                    Log.d("ABC", "No")
            }
            true
        }
        saveHistory.setOnPreferenceChangeListener { preference, any ->
            if (!saveHistory?.isChecked) {
                Toast.makeText(activity, "Save history", Toast.LENGTH_LONG).show()

//                    Log.d("ABC", "No")
            } else {
                Toast.makeText(activity, "Don't save history", Toast.LENGTH_LONG).show()
//                    Log.d("ABC", "No")
            }
            true
        }
//        sound.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener
//        vibrate.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener
//        saveHistory.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener
//        removeAds.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener
    }


//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        fragmentManager!!.putFragment(outState, "settingFragment", this)
//
//    }
//
//    override fun onRestoreInstanceState(inState: Bundle) {
//        myFragment = fragmentManager!!.getFragment(inState, "settingFragment")!!
//        MainActivity.myBundle = inState
//    }

//    private var cameraAdapter: SettingAdapter? = null
//    private var historyAdapter: SettingAdapter? = null
//    private var cameraSettings: ArrayList<SettingMenu>? = null
//    private var historySettings: ArrayList<SettingMenu>? = null

    // ...

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */

//    var vibrate: SwitchPreference = findPreference("vibrate") as SwitchPreference
//    var saveHistory: SwitchPreference = findPreference("save_history") as SwitchPreference
//    var removeADS: SwitchPreference = findPreference("remove_ads") as SwitchPreference
    private val sBindPreferenceSummaryToValueListener =
        Preference.OnPreferenceChangeListener { preference, value ->
            //            Log.d("ABC", "No")

            val stringValue = value.toString()

            if (preference is SwitchPreferenceCompat) {

//                Log.d("ABC", "Sound is null ${sound == null}")
//                if (sound == null){
////                    Log.d("ABC", "No")
//                }else{
//                    Toast.makeText(activity, "n", Toast.LENGTH_LONG).show()
//                    Log.d("ABC", "No")
//                }
//                if (sound?.isChecked) {
//                    Toast.makeText(activity, "Turn on Sound", Toast.LENGTH_LONG).show()
//
////                    Log.d("ABC", "No")
//                } else {
//                    Toast.makeText(activity, "Turn off Sound", Toast.LENGTH_LONG).show()
////                    Log.d("ABC", "No")
//                }
//                if (vibrate?.isChecked) {
//                    Toast.makeText(activity, "Turn on vibrate", Toast.LENGTH_LONG).show()
//
////                    Log.d("ABC", "No")
//                } else {
//                    Toast.makeText(activity, "Turn off vibrate", Toast.LENGTH_LONG).show()
////                    Log.d("ABC", "No")
//                }
//                if (saveHistory?.isChecked) {
//                    Toast.makeText(activity, "Save history", Toast.LENGTH_LONG).show()
//
////                    Log.d("ABC", "No")
//                } else {
//                    Toast.makeText(activity, "Don't save history", Toast.LENGTH_LONG).show()
////                    Log.d("ABC", "No")
//                }


            }
            true
        }

    private fun bindPreferenceSummaryToValue(preference: androidx.preference.Preference) {
        // Set the listener to watch for value changes.
        preference.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(
            preference,
            PreferenceManager
                .getDefaultSharedPreferences(preference.context)
                .getString(preference.key, "")
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundColor(resources.getColor(R.color.colorbg))
    }


//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(com.hidero.qrsolar.R.layout.fragment_setting, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        cameraSettings = ArrayList<SettingMenu>()
//        cameraSettings!!.add(
//            SettingMenu(
//                id = 1,
//                title = "Sound",
//                image = com.hidero.qrsolar.R.drawable.ic_sound,
//                status = true
//            )
//        )
//        cameraSettings!!.add(
//            SettingMenu(
//                id = 2,
//                title = "Vibrate",
//                image = com.hidero.qrsolar.R.drawable.ic_vibrate,
//                status = false
//            )
//        )
//        cameraAdapter = SettingAdapter(context!!, cameraSettings!!)
//        rvCameraSetting.adapter = cameraAdapter
//        historySettings = ArrayList()
//        historySettings!!.add(
//            SettingMenu(
//                id = 3,
//                title = "Save History",
//                image = com.hidero.qrsolar.R.drawable.ic_save_history,
//                status = false
//            )
//        )
//        historySettings!!.add(
//            SettingMenu(
//                id = 4,
//                title = "Remove ads",
//                image = com.hidero.qrsolar.R.drawable.ic_remove_ads,
//                status = false
//            )
//        )
//        historyAdapter = SettingAdapter(context!!, historySettings!!)
//        rvHistorySetting.adapter = historyAdapter
//    }

}