package fiap.com.br.fiapapp.presenter

import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fiap.com.br.fiapapp.presenter.interfaces.CorContrato
import fiap.com.br.fiapapp.presenter.interfaces.FilialContrato
import fiap.com.br.fiapapp.presenter.interfaces.LoginContrato
import fiap.com.br.fiapapp.view.Menu

class LoginPresenter: LoginContrato.AutenticaPresenter{

    //private var auth = Firebase.auth
    private lateinit var auth: FirebaseAuth
    private var nomeUsuario: String = ""
    private var view: LoginContrato.AutenticaView?

    constructor(view: LoginContrato.AutenticaView, auth: FirebaseAuth) {
        this.view = view
        this.auth = auth
    }

    override fun AutenticarUsuario(email: String, senha: String) {
       // auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, senha)
            .addOnSuccessListener {

                val user = auth.currentUser
                view?.demonstrarUsuarioLogado(user)
            }
            .addOnFailureListener {
                view?.demonstrarMsgErro("Usuário não Autenticado!")
            }
    }

    override fun CadastrarUsuario(email: String, senha: String){
        auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, senha).addOnSuccessListener {
            val currentUser = auth.currentUser
            val userProfileChangeRequest = UserProfileChangeRequest.Builder().setDisplayName(nomeUsuario).build()
            currentUser!!.updateProfile(userProfileChangeRequest).addOnCompleteListener {
                val user = auth.currentUser
                view?.demonstrarUsuarioLogado(user)
            }

        }
            .addOnFailureListener {
                view?.demonstrarMsgErro("Falha ao Cadastrar o Usuário!")
            }
     }

    override fun destruirView() {
        this.view = null
    }
}