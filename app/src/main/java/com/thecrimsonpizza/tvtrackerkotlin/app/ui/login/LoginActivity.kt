package com.thecrimsonpizza.tvtrackerkotlin.app.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.core.base.MainActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 1001
    var mAuth: FirebaseAuth? = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val currentUser = mAuth?.currentUser
        launchMainActivity(currentUser)

        username.addTextChangedListener(getTextWatcher())
        password.addTextChangedListener(getTextWatcher())

        google_sign_in_button.setOnClickListener { googleLogin() }
        forget.setOnClickListener { recoverPassword(username.text.toString()) }
        login.setOnClickListener { login(username.text.toString(), password.text.toString()) }
        sign_up.setOnClickListener {
            createUser(username.text.toString(), password.text.toString())
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth!!.currentUser
        launchMainActivity(currentUser)
    }

    private fun login(email: String, pass: String) {
        if (username.text.toString().isNotEmpty() && password.text.toString().isNotEmpty()) {
            mAuth!!.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) launchMainActivity(mAuth!!.currentUser)
                    else showToastMessage(
                        getString(R.string.login_fail) + getLocalizedMessage(it)
                    )
                }
        } else showToastMessage(getString(R.string.no_conn))
    }

    private fun createUser(email: String, pass: String) {
        if (username.text.toString().isNotEmpty() && password.text.toString().isNotEmpty()) {
            mAuth!!.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        showToastMessage(getString(R.string.success_register))
                        launchMainActivity(mAuth!!.currentUser)
                    } else showToastMessage(
                        getString(R.string.register_fail) + getLocalizedMessage(it)
                    )
                }
        } else showToastMessage(getString(R.string.no_conn))
    }

    private fun recoverPassword(email: String) {
        if (username.text.toString().isNotEmpty()) {
            mAuth!!.sendPasswordResetEmail(email).addOnCompleteListener(this) {
                if (it.isSuccessful) showToastMessage(getString(R.string.reset_success))
                else showToastMessage(getString(R.string.reset_fail) + getLocalizedMessage(it))
            }
        } else showToastMessage(getString(R.string.no_conn))
    }

    private fun googleLogin() {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(this, gso)
        startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        mAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener(this) {
                if (it.isSuccessful) launchMainActivity(mAuth!!.currentUser)
                else showToastMessage(getString(R.string.auth_fail) + it.exception)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            firebaseAuthWithGoogle(
                task.getResult(ApiException::class.java)!!
            )
//                showToastMessage(getString(R.string.google_fail) + e)
        }
    }

    private fun validateFields(email: String, pass: String): Boolean {
        if (!isUserNameValid(email)) {
            username.error = getString(R.string.invalid_email)
            return false
        }
        if (!isPasswordValid(pass)) {
            password.error = getString(R.string.invalid_password)
            return false
        }
        return true
    }

    private fun isUserNameValid(username: String?): Boolean {
        return if (username == null) {
            false
        } else Patterns.EMAIL_ADDRESS.matcher(username).matches()
    }

    private fun isPasswordValid(password: String?): Boolean {
        return password != null && password.trim { it <= ' ' }.length > 5
    }

    private fun getTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
                // ignore
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                login.isEnabled = validateFields(username.text.toString(), password.text.toString())
                sign_up.isEnabled =
                    validateFields(username.text.toString(), password.text.toString())
            }
        }
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_LONG).show()
    }

    private fun launchMainActivity(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun getLocalizedMessage(task: Task<*>): String {
        return task.exception?.localizedMessage.toString()
    }

}