package fiap.com.br.fiapapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView

class Registro : AppCompatActivity() {

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

 //               ivfoto.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.bg_image_circulo))
                ivfoto.setImageURI(it)
                Log.i("Teste", "carregou")

            })

        btnfoto.setOnClickListener {

            Log.i("Teste", "Entrou no click do botão")

            loadImage.launch("image/*")

   //         selectFoto()
        }


        btncadastrar.setOnClickListener {

            val nomeUsuario =  nome.text.toString()
            val email = editemail.text.toString()
            val senha = editsenha.text.toString()

            auth.createUserWithEmailAndPassword(email, senha).addOnSuccessListener {
                val currentUser = auth.currentUser
                val userProfileChangeRequest = UserProfileChangeRequest.Builder().setDisplayName(nomeUsuario).build()
                currentUser!!.updateProfile(userProfileChangeRequest).addOnCompleteListener {

                    startActivity(Intent(this, Menu::class.java))

                }

            }

                .addOnFailureListener {
                    Toast.makeText(baseContext, "Falha na Autenticação do usuário.",
                        Toast.LENGTH_SHORT).show()
                }
        }

    }
}