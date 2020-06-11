package com.example.basicotplogin

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private var Username: String = ""
    private var Contact_no: String = "+91"
    private var Address: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val toolbar : Toolbar = findViewById(R.id.toolbar_register)                 //create a back button on top of toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Register"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent =  Intent(this@RegisterActivity, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        Register_OTP_btn.setOnClickListener{
            getUserRegistered()
        }
    }

    private fun getUserRegistered() {
        Username = username_register.text.toString()
        Contact_no += contact_no_register.text.toString()
        Address = Address_register.text.toString()

        var refUserAdmin = FirebaseDatabase.getInstance().reference.child("Admin")
        refUserAdmin.addListenerForSingleValueEvent( object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.child("phone").value!!.equals(Contact_no) && p0.child("logged").value!!.equals("true")) {
                    Toast.makeText(this@RegisterActivity, "You are already registered from another Device", Toast.LENGTH_LONG).show()
                    FirebaseAuth.getInstance().signOut()
                    finishAndRemoveTask();
                }
                else{
                    val intent =  Intent(this@RegisterActivity, OTP_Checker::class.java)
                    intent.putExtra("username", Username)
                    intent.putExtra("contact", Contact_no)
                    intent.putExtra("address", Address)
                    startActivity(intent)
                    finish()
                }
            }
        })
    }
}