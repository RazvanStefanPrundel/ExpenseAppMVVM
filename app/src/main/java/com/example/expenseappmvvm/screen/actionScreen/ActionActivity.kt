package com.example.expenseappmvvm.screen.actionScreen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.databinding.ActivityActionBinding
import com.example.expenseappmvvm.screen.actionScreen.adapter.CategoryAdapter
import com.example.expenseappmvvm.screen.actionScreen.adapter.CategoryModel
import com.example.expenseappmvvm.screen.loginScreen.LoginActivity
import com.example.expenseappmvvm.screen.mainScreen.MainActivity
import com.example.expenseappmvvm.utils.DateUtil.Companion.sdf
import com.example.expenseappmvvm.utils.ImageStorageManager
import kotlinx.android.synthetic.main.activity_action.*
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class ActionActivity : AppCompatActivity() {
    private val actionViewModel: ActionViewModel by viewModel()

    private val PICK_IMAGE = 123
    private var imageUri: Uri? = null
    private var dataList = mutableListOf<CategoryModel>()
    private var dateAction: Calendar = Calendar.getInstance()

    private lateinit var bitmapImage: Bitmap
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityActionBinding>(this, R.layout.activity_action).apply {
            viewModel = this@ActionActivity.actionViewModel
            lifecycleOwner = this@ActionActivity
        }

        initComponents()
        initObservers()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data?.data
            iv_detail_photo.setImageURI(imageUri)
        }
    }

    private fun initObservers() {
        actionViewModel.openDateTimePicker.observe(this, {
            datePicker()
            actionViewModel.expense.value!!.expenseDate = dateAction.timeInMillis
        })

        actionViewModel.photoName.observe(this, {
            ImageStorageManager.saveToInternalStorage(this, bitmapImage, it) + "/" + it.toUri()
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, PICK_IMAGE)
            ib_delete.isVisible = true
        })

        actionViewModel.deleteImg.observe(this, {
            iv_detail_photo.setImageURI(null)
            ib_delete.isVisible = false
        })

        actionViewModel.checkAdd.observe(this, {
            if (it){
                Toast.makeText(this, R.string.action_success, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, R.string.action_failed, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initComponents() {
        title_toolbar.setText(R.string.title_add_action)
        imageView_toolbar_add.setImageResource(R.drawable.img_save)
        iv_burger_menu.setImageResource(R.drawable.back_arrow_icon)
        categoryRCV()
        ib_delete.isVisible = false

        if (iv_detail_photo != null) {
            bitmapImage = iv_detail_photo.drawable.toBitmap()
        }

        initListeners()
    }

    private fun initListeners() {
        iv_burger_menu.setOnClickListener { super.onBackPressed() }
        imageView_toolbar_add.setOnClickListener {
            actionViewModel.saveExpenseOnClick()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun datePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val mDatePicker = DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->
            timePicker(year, monthOfYear, dayOfMonth)
        }, year, month, day)
        mDatePicker.show()
    }

    private fun timePicker(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val mTimePicker: TimePickerDialog
        val mCurrentTime = Calendar.getInstance()
        val hour = mCurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mCurrentTime.get(Calendar.MINUTE)

        mTimePicker = TimePickerDialog(this, { _, hourOfDay, minute ->
            updateDateLabel(year, dayOfMonth, monthOfYear, hourOfDay, minute)
        }, hour, minute, true)
        mTimePicker.show()
    }

    private fun updateDateLabel(
        userSelectedYear: Int,
        userSelectedDay: Int,
        userSelectedMonth: Int,
        hourOfDay: Int,
        minute: Int

    ) {
        dateAction.set(userSelectedYear, userSelectedMonth, userSelectedDay, hourOfDay, minute)
        et_add_date.setText(sdf.format(dateAction.timeInMillis))
    }

    private fun categoryRCV() {
        rcv_category.layoutManager = GridLayoutManager(applicationContext, 4)

        dataList.add(CategoryModel(R.drawable.img_income, getString(R.string.txt_income_category)))
        dataList.add(CategoryModel(R.drawable.img_food, getString(R.string.txt_food_category)))
        dataList.add(CategoryModel(R.drawable.img_car, getString(R.string.txt_car_category)))
        dataList.add(CategoryModel(R.drawable.img_clothes, getString(R.string.txt_clothes_category)))
        dataList.add(CategoryModel(R.drawable.img_savings, getString(R.string.txt_savings_category)))
        dataList.add(CategoryModel(R.drawable.img_health, getString(R.string.txt_health_category)))
        dataList.add(CategoryModel(R.drawable.img_beauty, getString(R.string.txt_beauty_category)))
        dataList.add(CategoryModel(R.drawable.img_travel, getString(R.string.txt_travel_category)))

        categoryAdapter = CategoryAdapter(dataList, actionViewModel.itemSelectedPublishSubject)
        rcv_category.adapter = categoryAdapter
    }
}