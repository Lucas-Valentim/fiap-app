package fiap.com.br.fiapapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import fiap.com.br.fiapapp.R

class Cadastro : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        val marcas = resources.getStringArray(R.array.marcas)

        //Populando o combo marca
        val spinner = findViewById<Spinner>(R.id.cmbMarca)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, marcas)
        adapter.notifyDataSetChanged()
        spinner.adapter = adapter


    }
}