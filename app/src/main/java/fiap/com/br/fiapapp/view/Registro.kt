package fiap.com.br.fiapapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import fiap.com.br.fiapapp.R
import fiap.com.br.fiapapp.presenter.LoginPresenter
import fiap.com.br.fiapapp.presenter.interfaces.LoginContrato

class Registro : AppCompatActivity(), LoginContrato.AutenticaView {

    var auth = Firebase.auth
    private var presenterLogin: LoginContrato.AutenticaPresenter = LoginPresenter(this, auth)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        var nome = findViewById<EditText>(R.id.editNome)
        var editemail = findViewById<EditText>(R.id.editEmail)
        var editsenha = findViewById<EditText>(R.id.editSenha)
        var btncadastrar = findViewById<Button>(R.id.btnCadastrar)
        var btnfoto = findViewById<Button>(R.id.btnFoto)
        var ivfoto  = findViewById<CircleImageView>(R.id.iVFoto)
        var auth = Firebase.auth


        val loadImage = registerForActivityResult(ActivityResultContracts.GetContent(),
            ActivityResultCallback{
                ivfoto.setImageURI(it)
            })

        btnfoto.setOnClickListener {
            Log.i("Teste", "Entrou no click do botão")
            loadImage.launch("image/*")
        }


        btncadastrar.setOnClickListener {

            val nomeUsuario =  nome.text.toString()
            val email = editemail.text.toString()
            val senha = editsenha.text.toString()

            presenterLogin.AutenticarUsuario(email, senha)

        }

    }

    override fun demonstrarUsuarioLogado(user: FirebaseUser?) {
        startActivity(Intent(this, Menu::class.java))
    }

    override fun demonstrarMsgErro(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}