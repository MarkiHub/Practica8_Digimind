package lopez.marcos.mydigimindt.ui.home

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import lopez.marcos.mydigimind.ui.Task
import lopez.marcos.mydigimindt.R
import lopez.marcos.mydigimindt.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var adaptador: AdaptadorTareas? = null
    private var _binding: FragmentHomeBinding? = null
    private val db = Firebase.firestore
    companion object{
        var tasks = ArrayList<Task>()
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        adaptador = AdaptadorTareas(root.context, tasks)

        val gridView: GridView = root.findViewById(R.id.reminders)

        gridView.adapter = adaptador
        fillTasks()
        return root
    }

    fun fillTasks(){

        db.collection("actividades")
            .get()
            .addOnSuccessListener { result ->
                tasks.clear()
                for (document in result) {
                    var datos = document.data

                    var days = ArrayList<String>()

                    if (datos["lu"] as Boolean) {
                        days.add("Monday")
                    }
                    if (datos["ma"] as Boolean) {
                        days.add("Tuesday")
                    }
                    if (datos["mi"] as Boolean) {
                        days.add("Wednesday")
                    }
                    if (datos["ju"] as Boolean) {
                        days.add("Thursday")
                    }
                    if (datos["vi"] as Boolean) {
                        days.add("Friday")
                    }
                    if (datos["sa"] as Boolean) {
                        days.add("Saturday")
                    }
                    if (datos["do"] as Boolean) {
                        days.add("Sunday")
                    }

                    val task = Task("${datos.get("actividad")}",days,"${datos.get("tiempo")}")
                    tasks.add(task)
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
                adaptador?.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }


    }

    private class AdaptadorTareas: BaseAdapter{
        var tasks = ArrayList<Task>()
        var contexto: Context? = null

        constructor(contexto: Context, tasks: ArrayList<Task>){
            this.contexto = contexto
            this.tasks = tasks
        }

        override fun getCount(): Int {
            return tasks.size
        }

        override fun getItem(p0: Int): Any {
            return tasks[p0]
        }

        override fun getItemId(p0: Int): Long {
           return  p0.toLong()
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var task = tasks[p0]
            var inflador = LayoutInflater.from(contexto)
            var vista = inflador.inflate(R.layout.task_view, null)
            var tv_title: TextView = vista.findViewById(R.id.tv_title)
            var tv_time: TextView = vista.findViewById(R.id.tv_time)
            var tv_days: TextView = vista.findViewById(R.id.tv_days)

            tv_title.setText(task.title)
            tv_time.setText(task.time)
            tv_days.setText(task.days.toString())

            return vista
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}