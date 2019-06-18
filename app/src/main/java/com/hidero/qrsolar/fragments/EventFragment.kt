package com.hidero.qrsolar.fragments


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.hidero.qrsolar.R
import com.hidero.qrsolar.activities.MainActivity
import com.hidero.qrsolar.databinding.FragmentEventBinding
import com.hidero.qrsolar.viewmodels.EventViewModel
import java.text.SimpleDateFormat
import java.util.*

class EventFragment : Fragment() {
    private lateinit var binding: FragmentEventBinding
    private var vm: EventViewModel? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.opt = 8
        vm = ViewModelProviders.of(activity!!).get(EventViewModel::class.java)
        vm!!.getValue().observe(activity!!, android.arch.lifecycle.Observer {it->
            if (it == null) {
//                Toast.makeText(activity, "Null", Toast.LENGTH_SHORT).show()
            } else {
//                Log.e(TAG, it.content)
                binding.etName.setText(it.name)
            }
        })
    }
    fun myClick(view: View) {
        when (view.id) {
            R.id.btnHome -> {
                vm!!.setValue(null)
                if (activity!!.supportFragmentManager.backStackEntryCount > 0) {
                    activity!!.supportFragmentManager.popBackStack()
                } else {
                    replaceFragment(CreateFragment())
                }
            }
            R.id.btnSave -> {

            }
            R.id.etStartDate -> datePickerDialog(binding.etStartDate)
            R.id.etStartTime -> timePickerDialog(binding.etStartTime)
            R.id.etEndDate -> datePickerDialog(binding.etEndDate)
            R.id.etEndTime -> timePickerDialog(binding.etEndTime)
        }

    }

    private fun replaceFragment(fragment: Fragment) {

        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.content, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun datePickerDialog(et: EditText) {

        var calendar = Calendar.getInstance()
        var year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH)
        var day = calendar.get(Calendar.DAY_OF_MONTH)

//        val dayLongName = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
//        var dayLongName = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.UK)

        val dpd = DatePickerDialog(
            activity,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                // Display Selected date in text box
                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//                var currentDate = DateFormat.getDateInstance().format(calendar.time)
                var dayLongName =
                    calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.UK)
                et.setText(dayLongName + ", " + simpleDateFormat.format(calendar.time))
            },
            year,
            month,
            day
        )
// If you're calling this from a support Fragment
        dpd.show()
// If you're calling this from an AppCompatActivity
// dpd.show(getSupportFragmentManager(), "Datepickerdialog");
    }

    fun timePickerDialog(et: EditText) {

        var calendar = Calendar.getInstance()
        var hour = calendar.get(Calendar.HOUR)
        var minute = calendar.get(Calendar.MINUTE)
//        var second = calendar.get(Calendar.SECOND)
//        var a = calendar.get(Calendar.ALL_STYLES)
        val tpd = TimePickerDialog(
            activity,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                // Display Selected date in textbox
                val simpleDateFormat = SimpleDateFormat("HH:mm:ss a")
                calendar.set(Calendar.HOUR, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                et.setText(simpleDateFormat.format(calendar.time))
            },
            hour,
            minute,
            true
        )

// If you're calling this from a support Fragment
        tpd.show()
// If you're calling this from an AppCompatActivity
// dpd.show(getSupportFragmentManager(), "Datepickerdialog");
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
                DataBindingUtil.inflate(
                    inflater,
                    R.layout.fragment_event,
                    container,
                    false
                )
        binding.event = this@EventFragment
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (MainActivity.opt == 0 || MainActivity.opt == 1 || MainActivity.opt == 2 || MainActivity.opt == 3 || MainActivity.opt == 4 || MainActivity.opt == 5 || MainActivity.opt == 6 || MainActivity.opt == 7 || MainActivity.opt == 8 || MainActivity.opt == 9 || MainActivity.opt == 10 || MainActivity.opt == 11) {
            MainActivity.binding.bottomNavigation.visibility = View.GONE
//            MainActivity.binding.mainLayout.background = activity!!.getDrawable(android.R.color.white)
        } else {
            MainActivity.binding.bottomNavigation.visibility = View.VISIBLE
//            MainActivity.binding.mainLayout.background = activity!!.getDrawable(R.color.colorbg)
        }
    }

}
