package com.bubududu.shoppinglist

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bubududu.shoppinglist.databinding.ActivityMainBinding
import com.example.sqlexample.DBHelper


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null
    var data: ArrayList<Items> = ArrayList<Items>()

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = DBHelper(this, null)
        updateData(db)
        initViews()

        val swipeToDeleteCallback = object : SwapToDeleteCallback(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val id = data.get(position).id
                db.deleteItem(id)
                updateData(db)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        binding.buttonAdd.setOnClickListener {
            val inputName = binding.inputName.text.toString()
            val inputQuantity = binding.inputQuantity.text.toString()

            if(inputName == "" && inputQuantity == "") {
                Toast.makeText(this, "Cannot insert empty data", Toast.LENGTH_LONG).show()
            }
            else if(inputName == ""){
                Toast.makeText(this, "Please insert a name", Toast.LENGTH_LONG).show()
            }
            else if(inputQuantity == ""){
                Toast.makeText(this, "Please insert a quantity", Toast.LENGTH_LONG).show()
            }
            else {
                db.addName(inputName, inputQuantity)
                binding.inputName.text.clear()
                binding.inputQuantity.text.clear()

                updateData(db)
            }
            binding.inputName.clearFocus()
            binding.inputQuantity.clearFocus()
        }

        binding.buttonClear.setOnClickListener {
            db.deleteAll()
            updateData(db)
            binding.inputName.clearFocus()
            binding.inputQuantity.clearFocus()
        }
    }

    //Initialize RecyclerView
    fun initViews() {
        //Initialize layoutManager and adapter
        layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager //set layoutManager to layoutManager
        adapter = RecyclerAdapter(data) { position -> onListItemClick(position) }
        binding.recyclerView.adapter = adapter //set adapter to adapter
    }

    //Call to update data
    @SuppressLint("Range")
    fun updateData(db: DBHelper) {
        if(!db.isEmpty()) {
            data.clear()
            val cursor = db.getName()
            cursor!!.moveToFirst()
            val itemId = cursor.getInt(cursor.getColumnIndex(DBHelper.ID_COL))
            val itemName = cursor.getString(cursor.getColumnIndex(DBHelper.NAME_COl))
            val itemQuantity = cursor.getString(cursor.getColumnIndex(DBHelper.QTY_COL))
            data.add(Items(itemId, itemName, itemQuantity))

            while (cursor.moveToNext()) {
                val itemId = cursor.getInt(cursor.getColumnIndex(DBHelper.ID_COL))
                val itemName = cursor.getString(cursor.getColumnIndex(DBHelper.NAME_COl))
                val itemQuantity = cursor.getString(cursor.getColumnIndex(DBHelper.QTY_COL))
                data.add(Items(itemId, itemName, itemQuantity))
            }
        } else {
            data.clear()
        }
        adapter?.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        binding.inputName.clearFocus()
        binding.inputQuantity.clearFocus()
    }

    private fun onListItemClick(position: Int) {
        val db = DBHelper(this, null)
        val id = data.get(position).id
        db.deleteItem(id)
        updateData(db)
    }
}