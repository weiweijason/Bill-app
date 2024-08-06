package com.example.billapp.activity

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.billapp.R
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth


class SignUpActivity : BaseActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // 設置Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar_sign_up_activity)
        setSupportActionBar(toolbar)

        // 啟用返回按鈕
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Initialize the EditText views
        etName = findViewById(R.id.et_name)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]

        val btnSignUp: Button = findViewById(R.id.btn_sign_up)
        btnSignUp.setOnClickListener{
            registerUser()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun registerUser(){
        val name: String = etName.text.toString().trim { it <= ' ' }
        val email: String = etEmail.text.toString().trim { it <= ' ' }
        val password: String = etPassword.text.toString().trim { it <= ' ' }

        if(validateForm(name, email, password))
        {
            showProgressDialog(resources.getString(R.string.please_wait))
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    hideProgressDialog()
                    if(task.isSuccessful){
                        Log.d("SignUpActivity", "createUserWithEmail:success")
                        //val firebaseUser : FirebaseUser = task.result!!.user!!
                        val firebaseUser = auth.currentUser
                        val registeredEmail = firebaseUser!!.email!!
                        Toast.makeText(this,"$name, you have" +
                                "successfully registered the email" +
                                "address $registeredEmail", Toast.LENGTH_LONG).show()
                        //FirebaseAuth.getInstance().signOut()
                        finish()
                    }else {
                        Log.w("SignUpActivity", "signInWithEmail:failure", task.exception)
                        Toast.makeText(this,
                            task.exception!!.message,
                            Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }

    private fun validateForm(name: String, email: String, password: String): Boolean {
        return when{
            TextUtils.isEmpty(name)->{
                showErrorSnackBar("Please enter a name")
                false
            }
            TextUtils.isEmpty(email)->{
                showErrorSnackBar("Please enter a email")
                false
            }
            TextUtils.isEmpty(password)->{
                showErrorSnackBar("Please enter a password")
                false
            }else -> {
                true
            }
        }
    }

}