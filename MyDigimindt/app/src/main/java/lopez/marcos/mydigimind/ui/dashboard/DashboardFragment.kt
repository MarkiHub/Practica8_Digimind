package lopez.marcos.mydigimindt.ui.dashboard

import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import lopez.marcos.mydigimind.ui.Task
import lopez.marcos.mydigimindt.R
import lopez.marcos.mydigimindt.databinding.FragmentDashboardBinding
import lopez.marcos.mydigimindt.ui.home.HomeFragment
import java.text.SimpleDateFormat
import java.util.Calendar

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val btn_time = binding.time

        btn_time.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE,minute)

                btn_time.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(root.context,timeSetListener,cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),true).show()
        }
        val btn_save = root.findViewById(R.id.done) as Button
        val et_titulo = root.findViewById(R.id.name) as EditText
        val checkMonday = root.findViewById(R.id.monday) as CheckBox
        val checkTuesday = root.findViewById(R.id.tuesday) as CheckBox
        val checkWednesday = root.findViewById(R.id.wednesday) as CheckBox
        val checkThursday = root.findViewById(R.id.thursday) as CheckBox
        val checkFriday = root.findViewById(R.id.friday) as CheckBox
        val checkSaturday = root.findViewById(R.id.saturday) as CheckBox
        val checkSunday = root.findViewById(R.id.sunday) as CheckBox

        btn_save.setOnClickListener {
            guardarTask()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun guardarTask(){
        FirebaseApp.initializeApp(binding.root.context)
        val storage: FirebaseStorage = Firebase.storage
        var storageRef = storage.reference
        var actRef: StorageReference? = storageRef.child("/actividades/Pnr1fYPnu2jKsL5hxzG5")


        val root: View = binding.root
        val et_titulo = root.findViewById(R.id.name) as EditText
        val checkMonday = root.findViewById(R.id.monday) as CheckBox
        val checkTuesday = root.findViewById(R.id.tuesday) as CheckBox
        val checkWednesday = root.findViewById(R.id.wednesday) as CheckBox
        val checkThursday = root.findViewById(R.id.thursday) as CheckBox
        val checkFriday = root.findViewById(R.id.friday) as CheckBox
        val checkSaturday = root.findViewById(R.id.saturday) as CheckBox
        val checkSunday = root.findViewById(R.id.sunday) as CheckBox
        val btn_time = binding.time

        var title = et_titulo.text.toString()
        var days = ArrayList<String>()
        var time = btn_time.text.toString()

        if(checkMonday.isChecked){
            days.add("Monday")
        }
        if(checkTuesday.isChecked){
            days.add("Tuesday")
        }
        if(checkWednesday.isChecked){
            days.add("Wednesday")
        }
        if(checkThursday.isChecked){
            days.add("Thursday")
        }
        if(checkFriday.isChecked){
            days.add("Friday")
        }
        if(checkSaturday.isChecked){
            days.add("Saturday")
        }
        if(checkSunday.isChecked){
            days.add("Sunday")
        }

        val task = hashMapOf(
            "actividad" to "$title",
            "lu" to days.contains("Monday"),
            "ma" to days.contains("Tuesday"),
            "mi" to days.contains("Wednesday"),
            "ju" to days.contains("Thursday"),
            "vi" to days.contains("Friday"),
            "sa" to days.contains("Saturday"),
            "do" to days.contains("Sunday"),
            "tiempo" to time
        )

        db.collection("actividades")
            .add(task)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(root.context,"New task added",Toast.LENGTH_SHORT).show()
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
}