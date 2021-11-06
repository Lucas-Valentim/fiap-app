package fiap.com.br.fiapapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fiap.com.br.fiapapp.R
import fiap.com.br.fiapapp.presenter.LoginPresenter
import fiap.com.br.fiapapp.presenter.interfaces.LoginContrato


class MainActivity : AppCompatActivity(), LoginContrato.AutenticaView{

    var auth = Firebase.auth
    private var presenterLogin: LoginContrato.AutenticaPresenter = LoginPresenter(this, auth)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var btnentrar = findViewById<Button>(R.id.btn_entrar)
        var editemail = findViewById<EditText>(R.id.edit_email)
        var editsenha = findViewById<EditText>(R.id.edit_senha)
        var textNovaConta = findViewById<TextView>(R.id.textNovaConta)

        btnentrar.setOnClickListener {
            val email = editemail.text.toString()
            val senha = editsenha.text.toString()

            presenterLogin.AutenticarUsuario(email, senha)

        }

        textNovaConta.setOnClickListener {
            val intent = Intent(this, Registro::class.java)
            startActivity(intent)
        }

        }

    override fun demonstrarUsuarioLogado(user: FirebaseUser?) {

        startActivity(Intent(this, Menu::class.java))
    }

    override fun demonstrarMsgErro(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}