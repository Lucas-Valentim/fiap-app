package fiap.com.br.fiapapp.presenter

import android.app.Activity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import fiap.com.br.fiapapp.presenter.interfaces.LoginContrato
import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito


import org.junit.Test
import org.mockito.kotlin.times
import java.lang.Exception
import java.util.concurrent.Executor

class LoginPresenterTest: LoginContrato.AutenticaView {

    var firebaseAuth = Mockito.mock(FirebaseAuth::class.java)
    var authResult = Mockito.mock(AuthResult::class.java)
    var email = "user_fiap@fiap.com.br"
    var senha = "fiap123"
    lateinit var task: Task<AuthResult>
    lateinit var presenterLogin: LoginContrato.AutenticaPresenter


     private fun mockTask(){

        Mockito.`when`(firebaseAuth.signInWithEmailAndPassword(email, senha)).thenReturn(task)
    }

    @Before
    fun setup(){

        presenterLogin = LoginPresenter(this, firebaseAuth)

        task = object : Task<AuthResult>() {
            override fun isComplete(): Boolean {
                return true
            }

            override fun isSuccessful(): Boolean {
                return true
            }

            override fun isCanceled(): Boolean {
                return false
            }

            override fun getResult(): AuthResult? {
                return authResult
            }

            override fun <X : Throwable?> getResult(p0: Class<X>): AuthResult? {
                return authResult
            }

            override fun getException(): Exception? {
                return null
            }

            override fun addOnSuccessListener(p0: OnSuccessListener<in AuthResult>): Task<AuthResult> {
                p0.onSuccess(authResult)
                return task
            }

            override fun addOnSuccessListener(
                p0: Executor,
                p1: OnSuccessListener<in AuthResult>
            ): Task<AuthResult> {
                p1.onSuccess(authResult)
                return task
            }

            override fun addOnSuccessListener(
                p0: Activity,
                p1: OnSuccessListener<in AuthResult>
            ): Task<AuthResult> {
                p1.onSuccess(authResult)
                return task
            }

            override fun addOnFailureListener(p0: OnFailureListener): Task<AuthResult> {
                p0.onFailure(Exception())
                return task
            }

            override fun addOnFailureListener(
                p0: Executor,
                p1: OnFailureListener
            ): Task<AuthResult> {
                p1.onFailure(Exception())
                return task
            }

            override fun addOnFailureListener(
                p0: Activity,
                p1: OnFailureListener
            ): Task<AuthResult> {
                p1.onFailure(Exception())
                return task
            }


        }

        mockTask()

    }


    @Test
    fun verificarParametros (){
        presenterLogin.AutenticarUsuario(email, senha)
    }

    override fun demonstrarUsuarioLogado(user: FirebaseUser?) {

        Mockito.verify(firebaseAuth, times(1)).signInWithEmailAndPassword(email, senha)
    }

    override fun demonstrarMsgErro(msg: String) {
        assertNotNull(msg)
    }
}