package fiap.com.br.fiapapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fiap.com.br.fiapapp.R


class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var btnentrar = findViewById<Button>(R.id.btn_entrar)
        var editemail = findViewById<EditText>(R.id.edit_email)
        var editsenha = findViewById<EditText>(R.id.edit_senha)
        var textNovaConta = findViewById<TextView>(R.id.textNovaConta)

        val auth = Firebase.auth

        btnentrar.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)

            val email = editemail.text.toString()
            val senha = editsenha.text.toString()

            auth.signInWithEmailAndPassword(email, senha)
                .addOnSuccessListener {

                    startActivity(Intent(this, Menu::class.java))

                    Log.i("Teste", "Funcionou - botão entrar")

                }
                .addOnFailureListener {
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }

        }

        textNovaConta.setOnClickListener {
            val intent = Intent(this, Registro::class.java)
            startActivity(intent)

            Log.i("Teste", "Funcionou - link nova conta")
        }

        }

}
