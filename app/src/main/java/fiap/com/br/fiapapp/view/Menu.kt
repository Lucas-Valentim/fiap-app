package fiap.com.br.fiapapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import fiap.com.br.fiapapp.R

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        var btnCadastro = findViewById<Button>(R.id.btnCadastro)

        var btnConsulta = findViewById<Button>(R.id.btnConsulta)

        btnCadastro.setOnClickListener {
            val intent = Intent(this, Cadastro::class.java)
            startActivity(intent)
        }

        btnConsulta.setOnClickListener {
            val intent = Intent(this, Lista::class.java)
            startActivity(intent)
        }


    }
}