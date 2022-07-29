package com.devmasterteam.tasks.view

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.ActivityRegisterBinding
import com.devmasterteam.tasks.databinding.ActivityTaskFormBinding
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.viewmodel.RegisterViewModel
import com.devmasterteam.tasks.viewmodel.TaskFormViewModel
import java.text.SimpleDateFormat
import java.util.*

class TaskFormActivity : AppCompatActivity(), View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private lateinit var viewModel: TaskFormViewModel
    private lateinit var binding: ActivityTaskFormBinding
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
    private var  lista : List<PriorityModel> = mutableListOf();
    private var taskitemId : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Variáveis da classe
        viewModel = ViewModelProvider(this).get(TaskFormViewModel::class.java)
        binding = ActivityTaskFormBinding.inflate(layoutInflater)

        viewModel.loadPrioridades()



        // Eventos
        binding.buttonSave.setOnClickListener(this)
        binding.buttonDate.setOnClickListener(this)

        loadDataFromActivity()

        observe()

        // Layout
        setContentView(binding.root)
    }

    private fun loadDataFromActivity() {
        val bundle = intent.extras
        if(bundle !=  null){
            taskitemId =  bundle.getInt(TaskConstants.BUNDLE.TASKID)
            viewModel.load(taskitemId)
        }
    }
    private fun getIndex(priorityId : Int):Int{
        var index = 0
        for(l in lista){
            if(l.id == priorityId){
                break
            }
            index++
        }

        return index
    }
    private fun observe() {
        viewModel.prioridade.observe(this){
            lista = it
            val list = mutableListOf<String>()
            for(p in it){
                list.add(p.description)
            }
            val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,list)
            binding.spinnerPriority.adapter = adapter
        }
        viewModel.taskSave.observe(this){
            if(it.status()){

                toast("Sucesso")
                finish()
            }else{
                toast(it.mensagem())
            }
        }

        viewModel.task.observe(this){
            binding.editDescription.setText(it.description)
            binding.checkComplete.isChecked = it.complete
            val data = SimpleDateFormat("yyyy-MM-dd").parse(it.dueDate)
            binding.buttonDate.text = SimpleDateFormat("dd/MM/yyyy").format(data)
            getIndex(it.priorityId)
            binding.spinnerPriority.setSelection(getIndex(it.priorityId))
        }

        viewModel.taskLoad.observe(this){
            if(!it.status()){

                if(taskitemId == 0){
                    toast("Tarefa criada com sucesso!")
                }else{
                    toast("Tarefa atualizado com sucesso!")
                }

                finish()
            }
        }
    }

    private fun toast(str : String){
        Toast.makeText(applicationContext,str,Toast.LENGTH_SHORT).show()

    }

    override fun onClick(v: View) {
        if(v.id == R.id.button_date){
                handleData()
        }else if(v.id == R.id.button_save){
                handleSave()

        }
    }

    private fun handleSave() {
        val task = TaskModel().apply {
            this.id = taskitemId
            this.description = binding.editDescription.text.toString()
            //this.priorityId = binding.spinnerPriority
            this.complete = binding.checkComplete.isChecked
            this.dueDate = binding.buttonDate.text.toString()

            val index = binding.spinnerPriority.selectedItemPosition
            this.priorityId = lista.get(index).id
        }

        viewModel.save(task)
    }

    override fun onDateSet(v: DatePicker, year: Int, month: Int, dayofmonth: Int) {
       val calendario = Calendar.getInstance()
        calendario.set(year,month,dayofmonth)

       val data = dateFormat.format(calendario.time)
        binding.buttonDate.text = data
    }
    private fun handleData() {
       val calendario = Calendar.getInstance()
        val year = calendario.get(Calendar.YEAR)
        val month = calendario.get(Calendar.MONTH)
        val day = calendario.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this,this,year,month,day).show()
    }

}