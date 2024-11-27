package com.app.Finny.Activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.app.Finny.Adapters.UserAdapter
import com.app.Finny.Controllers.UserController
import com.app.Finny.Models.UserModel
import com.app.Finny.databinding.ActivityAdminUsersBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@DelicateCoroutinesApi
class AdminUsers : AppCompatActivity() {
    private lateinit var binding: ActivityAdminUsersBinding
    private lateinit var userController: UserController
    private lateinit var userList: List<UserModel>

    private lateinit var confirmDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Run splash screen
        startActivity(Intent(this, SplashScreen::class.java))

        binding = ActivityAdminUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.closeBtn.setOnClickListener {
            finish()
        }

        userController = UserController()
        val channel = Channel<List<UserModel>>()
        val emailList = mutableListOf<String>()
        val nameList = mutableListOf<String>()

        // Get all users
        GlobalScope.launch {
            val data = userController.getAll()

            channel.send(data)
        }
        runBlocking {
            userList = channel.receive()
        }
        userList.forEach { user ->
            emailList.add(user.email)
            nameList.add(user.name)
        }

        // Binding to list
        val userAdapter = UserAdapter(this, emailList, nameList)
        binding.list.adapter = userAdapter

        // Update
        // Open edit form


        // Delete
        // Open confirm message
    }

    private fun updateUser(user: UserModel) {
        // Update DB

    }

    private fun deleteUser(uid: String) {
        // Delete from DB


        // Delete from auth

    }

    private fun createConfirmDialog(option: String) {
        var title = "Are you sure?"
        when(option) {
            "update" -> { title = "Update user information?" }
            "delete" -> { title = "Delete user?" }
        }

        // Create a an exit confirmation popup
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setTitle(title)
            .setMessage("This action cannot be undone")
            // Return the user to the home screen
            .setPositiveButton("Yes") { _, _ ->
                when(option) {
                    "update" -> {
//                        updateUser()
                    }
                    "delete" -> {
//                        deleteUser()
                    }

                }
            }
            .setNegativeButton("No") { _, _ ->
                confirmDialog.cancel()
            }
        confirmDialog = builder.create()

//        binding.exitBtn.setOnClickListener {
//            confirmDialog.show()
//        }
    }
}