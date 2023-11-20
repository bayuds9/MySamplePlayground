package id.flowerencee.mysampleplayground

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.flowerencee.mysampleplayground.data.DataSource
import id.flowerencee.mysampleplayground.databinding.ActivityMainBinding
import id.flowerencee.mysampleplayground.objects.FormData
import id.flowerencee.mysampleplayground.ui.FormsView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private val listForm = ArrayList<FormData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        initView()
    }

    private fun initData() {
        val dataSource = DataSource(this)
        listForm.clear()
        listForm.addAll(dataSource.getSampleData())
    }

    private fun initView() {
        binding.tbToolbar.apply {
            title = "Concept"
        }
        val formListener = object : FormsView.FormInterface {
        }
        binding.formView.apply {
            setListener(formListener)
        }
        showData()
        binding.btnContent.setOnClickListener {
            showData(listForm.filter { it.label.isNullOrEmpty() })
        }
        binding.btnField.setOnClickListener {
            showData(listForm.filter { !it.label.isNullOrEmpty() })
        }
        binding.btnAll.setOnClickListener { showData() }
    }

    private fun showData(list: List<FormData>? = null) {
        when (list.isNullOrEmpty()) {
            true -> binding.formView.setData(listForm)
            else -> binding.formView.setData(ArrayList(list))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.formView.cancelCoroutines()
    }
}