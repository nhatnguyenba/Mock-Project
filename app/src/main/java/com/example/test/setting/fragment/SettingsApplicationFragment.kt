package com.example.test.setting.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import com.example.test.R
import com.example.test.setting.IDarkMode


class SettingsApplicationFragment : PreferenceFragmentCompat(),
    PreferenceFragmentCompat.OnPreferenceStartScreenCallback,
    Preference.OnPreferenceChangeListener {

    companion object {
        private const val TAG = "SettingsApplicationFrag"
    }

    private lateinit var mIDarkMode: IDarkMode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mIDarkMode = context as IDarkMode
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
        this.preferenceManager.findPreference<Preference>("enable_dark_mode")?.onPreferenceChangeListener =
            this
        this.preferenceManager.findPreference<Preference>("enable_notification")?.onPreferenceChangeListener =
            this
        this.preferenceManager.findPreference<Preference>("enable_password_application")?.onPreferenceChangeListener =
            this
        this.preferenceManager.findPreference<Preference>("password_application")?.onPreferenceChangeListener =
            this
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_settings_application, rootKey)
    }

    override fun getCallbackFragment(): Fragment {
        Log.d(TAG, "getCallbackFragment: ")
        return this
    }


    override fun onPreferenceStartScreen(
        caller: PreferenceFragmentCompat?,
        pref: PreferenceScreen?
    ): Boolean {
        caller?.preferenceScreen = pref
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.framelayout_settings, SettingsApplicationFragment())?.commit()
        }
        Log.d(TAG, "onPreferenceStartScreen: ")
        return true
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        when (preference!!.key) {
            "enable_dark_mode" -> {
                processDarkMode(newValue)
            }
            "enable_notification" -> {
                processNotification(newValue)
            }
            "enable_password_application" -> {
                processPasswordApplication(newValue)
            }
            "password_application" -> {
                Log.d(TAG, "onPreferenceChange: $newValue")
            }
        }
        return true
    }

    private fun processDarkMode(newValue: Any?) {
        if (newValue == true) {
            Toast.makeText(context, "B???t ch??? ????? m??n h??nh t???i", Toast.LENGTH_SHORT).show()
            darkModeOn()
        } else {
            Toast.makeText(context, "T???t ch??? ????? m??n h??nh t???i", Toast.LENGTH_SHORT).show()
            darkModeOff()
        }
    }

    private fun darkModeOn() {
        mIDarkMode.processDarkMode()
    }

    private fun darkModeOff() {
        mIDarkMode.processDarkMode()
    }

    private fun processNotification(newValue: Any?) {
        if (newValue == true) {
            Toast.makeText(context, "B???t th??ng b??o", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "T???t th??ng b??o", Toast.LENGTH_SHORT).show()
        }
    }

    private fun processPasswordApplication(newValue: Any?) {
        if (newValue == true) {
            Toast.makeText(context, "B???t m???t kh???u ???ng d???ng", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "T???t m???t kh???u ???ng d???ng", Toast.LENGTH_SHORT).show()
        }
    }

}