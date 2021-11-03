package com.example.expenseappmvvm.screen.actionScreen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.expenseappmvvm.R
import com.example.expenseappmvvm.data.database.entities.Expense
import com.example.expenseappmvvm.databinding.ActivityActionBinding
import com.example.expenseappmvvm.screen.actionScreen.adapter.CategoryAdapter
import com.example.expenseappmvvm.screen.actionScreen.adapter.CategoryModel
import com.example.expenseappmvvm.screen.mainScreen.MainActivity
import com.example.expenseappmvvm.utils.DataUtils
import com.example.expenseappmvvm.utils.DataUtils.Companion.sdf
import com.example.expenseappmvvm.utils.ImageStorageManager
import com.example.expenseappmvvm.utils.extensions.show
import kotlinx.android.synthetic.main.activity_action.*
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ActionActivity : AppCompatActivity() {
    private val actionViewModel: ActionViewModel by viewModel()

    private val PICK_IMAGE = 123
    private var imageUri: Uri? = null
    private var bitmapImage: Bitmap? = null

    private var dateAction: Calendar = Calendar.getInstance()

    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityActionBinding>(this, R.layout.activity_action).apply {
            viewModel = this@ActionActivity.actionViewModel
            lifecycleOwner = this@ActionActivity
        }

        initListeners()
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

    private fun populateActivity(expenseDB: Expense){

        if (expenseDB.expenseAmount<0.0){
            et_amount_value.setText((-expenseDB.expenseAmount).toString())
        } else {
            et_amount_value.setText(expenseDB.expenseAmount.toString())
        }

        et_add_date.setText(sdf.format(expenseDB.expenseDate))
        et_add_details.setText(expenseDB.expenseDetails)

//        val image = ImageStorageManager.getImageFromInternalStorage(this, expenseDB.expensePhoto)
//        iv_detail_photo.setImageBitmap(image)

    }

    private fun initImage() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGE)
    }

    private fun initObservers() {

        if (DataUtils.actionIndex == 1){
            actionViewModel.getExpenseDataFromDB(DataUtils.EXPENSE_ID)
            actionViewModel.expense.observe(this, {
                populateActivity(it)
            })
        }

        actionViewModel.openDateTimePicker.observe(this, {
            datePicker()
        })
//TODO
        actionViewModel.photoName.observe(this, {
            initImage()
            bitmapImage = iv_detail_photo.drawable.toBitmap()
            ImageStorageManager.saveToInternalStorage(this, bitmapImage!!, it) + "/" + it.toUri()
            ib_delete.isVisible = true
        })

        actionViewModel.deleteImg.observe(this, {
            iv_detail_photo.setImageURI(null)
            ib_delete.isVisible = false
        })

        actionViewModel.amount.observe(this, {
            when {
                it == "" -> {
                    tv_amount_error.visibility = VISIBLE
                    tv_amount_error.text = getString(R.string.field_empty)
                }
                it.toDouble() == 0.0 -> {
                    tv_amount_error.visibility = VISIBLE
                    tv_amount_error.text = getString(R.string.amount_zero)
                }
                else -> {
                    tv_amount_error.visibility = INVISIBLE

                    imageView_toolbar_add.setOnClickListener {
                        progress_loader.visibility = VISIBLE
                        actionViewModel.getCurrentBalance()

                        if ( DataUtils.actionIndex == 1){
                            actionViewModel.updateExpenseOnClick()
                        } else {
                            actionViewModel.saveExpenseOnClick()
                        }

                        DataUtils.actionIndex = 0
                    }
                }
            }
        })

        actionViewModel.checkAdd.observe(this, {
            if (it) {
                Toast.makeText(this, R.string.action_success, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                Toast.makeText(this, R.string.action_failed, Toast.LENGTH_SHORT).show()
                progress_loader.visibility = INVISIBLE
            }
        })

        actionViewModel.date.observe(this, {
            et_add_date.setText(sdf.format(it))
        })
    }

    private fun initComponents() {
        title_toolbar.setText(R.string.title_add_action)
        imageView_toolbar_add.setImageResource(R.drawable.img_save)
        iv_burger_menu.setImageResource(R.drawable.back_arrow_icon)
        categoryRCV()
        ib_delete.isVisible = false

        if (iv_detail_photo.drawable != null) {
            bitmapImage = iv_detail_photo.drawable.toBitmap()
        }
    }

    private fun initListeners() {
        iv_burger_menu.setOnClickListener { super.onBackPressed() }
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
        actionViewModel.date.value = dateAction.timeInMillis
    }

    private fun categoryRCV() {
        rcv_category.layoutManager = GridLayoutManager(applicationContext, 4)
        val dataList = mutableListOf<CategoryModel>()
        dataList.add(CategoryModel(R.drawable.img_income, getString(R.string.txt_income_category)))
        dataList.add(CategoryModel(R.drawable.img_food, getString(R.string.txt_food_category)))
        dataList.add(CategoryModel(R.drawable.img_car, getString(R.string.txt_car_category)))
        dataList.add(CategoryModel(R.drawable.img_clothes, getString(R.string.txt_clothes_category)))
        dataList.add(CategoryModel(R.drawable.img_savings, getString(R.string.txt_savings_category)))
        dataList.add(CategoryModel(R.drawable.img_health, getString(R.string.txt_health_category)))
        dataList.add(CategoryModel(R.drawable.img_beauty, getString(R.string.txt_beauty_category)))
        dataList.add(CategoryModel(R.drawable.img_travel, getString(R.string.txt_travel_category)))

        categoryAdapter = CategoryAdapter(dataList.toMutableList(), actionViewModel.itemSelectedPublishSubject)
        rcv_category.adapter = categoryAdapter
    }
}