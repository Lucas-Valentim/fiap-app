package fiap.com.br.fiapapp.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import fiap.com.br.fiapapp.R
import fiap.com.br.fiapapp.model.Empresa
import fiap.com.br.fiapapp.model.Modelo
import fiap.com.br.fiapapp.presenter.*
import fiap.com.br.fiapapp.presenter.interfaces.CorContrato
import fiap.com.br.fiapapp.presenter.interfaces.FilialContrato
import fiap.com.br.fiapapp.presenter.interfaces.MarcaContrato
import fiap.com.br.fiapapp.presenter.interfaces.ModeloContrato


class Consulta : AppCompatActivity(), MarcaContrato.ListaMarcaView, ModeloContrato.ModeloView,
CorContrato.ListaCorView, FilialContrato.FilialView{

    //private lateinit var firebaseFirestore: FirebaseFirestore
    private var presenterMarca: MarcaContrato.ListaMarcaPresenter = MarcaPresenter(this)
    private var presenterModelo: ModeloContrato.ModeloPresenter = ModeloPresenter(this)
    private var presenterCor: CorContrato.ListaCorPresenter = CorPresenter(this)
    private var presenterFilial: FilialContrato.FilialPresenter = FilialPresenter(this)
    private var marcaSerlecionada: Int = 0

    private var spinnerArrayMarca = ArrayList<String>()
    private var spinnerArrayModelo = ArrayList<String>()


    private lateinit var cmbMarca: Spinner
    private lateinit var cmbModelo: Spinner

    @RequiresApi(Build.VERSION_CODES.O)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consulta)

        val btnMenu = findViewById<ImageButton>(R.id.btnMenu)
        btnMenu.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
        }

        cmbMarca = findViewById<Spinner>(R.id.cmbMarca)
        cmbModelo = findViewById<Spinner>(R.id.cmbModelo)

        val btnFiltrar = findViewById<Button>(R.id.btnFiltrar)

        presenterMarca.obtemMarca()
        presenterCor.obtemCor()
        presenterModelo.obtemModelo(null)

        //Obtendo o id do documento selecionado na combo Marcas, para popular a combo Modelo
        cmbMarca.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                var descricaoMarca = spinnerArrayMarca[p2]
                presenterMarca.obterMarcaSelecionada(descricaoMarca)
                Log.i("Codigo da Marca", marcaSerlecionada.toString())

             }
        }

            // Aplicação filtro de pesquisa
            btnFiltrar.setOnClickListener {

                var msg = validaFiltro()

                if (msg.isNotEmpty()) {
                    var toast = Toast.makeText(this, msg, Toast.LENGTH_LONG)
                    toast.show()
                } else {

                    cmbMarca = findViewById<Spinner>(R.id.cmbMarca)
                    cmbModelo = findViewById<Spinner>(R.id.cmbModelo)
                    val txtdtaAno = findViewById<EditText>(R.id.dtaAno)

                    intent = Intent(this, Lista::class.java)

                    if(!cmbMarca.selectedItem.toString().isNullOrEmpty() && cmbMarca.selectedItem.toString() != "Selecione" ){
                        var marca = presenterMarca.obtemMarcaPorNome(cmbMarca.selectedItem.toString())
                        intent.putExtra("marca", marca)
                    }
                    if(!cmbModelo.selectedItem .toString().isNullOrEmpty() && cmbModelo.selectedItem.toString() != "Selecione" ){
                        var modelo = presenterModelo.obtemModeloPorNome(cmbModelo.selectedItem.toString())
                        intent.putExtra("modelo", modelo)
                    }
                    if(!txtdtaAno.text.isNullOrEmpty()){
                        intent.putExtra("ano", txtdtaAno.text.toString().toInt())
                    }

                    startActivity(intent)

                    }

                }

    } // fim do on create

    override fun demonstraMarcas(marcas: ArrayList<String>) {
        spinnerArrayMarca = marcas
        var adapterMarca = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArrayMarca)
        adapterMarca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        cmbMarca.adapter = adapterMarca
        adapterMarca.notifyDataSetChanged()
        cmbMarca.setSelection(0);
    }

    override fun demonstraModelos(modelos: ArrayList<String>) {

        spinnerArrayModelo = modelos
        var adapterModelo = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArrayModelo)
        adapterModelo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        cmbModelo.adapter = adapterModelo
        adapterModelo.notifyDataSetChanged()
    }

    override fun demonstrarModelosMarcaSel(modelos: ArrayList<String>, descrModeloSel: String) {
        TODO("Not yet implemented")
    }

    override fun demonstrarMarcaSelecionada(codMarca: Int, descricao: String) {
        presenterModelo.obtemModelo(codMarca)
    }

    override fun demonstraCores(cores: ArrayList<String>) {
    }

    override fun demonstrarCorSelecionada(codCor: Int, descricao: String) {

    }

    override fun demonstraRazaoSocial(filiais: ArrayList<String>) {
        TODO("Not yet implemented")
    }

    override fun demonstrarMsgErro(msg: String) {
        TODO("Not yet implemented")
        //implementar poup-up de erro
    }

    override fun carregarModelos(modelos: ArrayList<Modelo>) {
        TODO("Not yet implemented")
    }

    override fun carregarFiliais(filiais: ArrayList<Empresa>) {
        TODO("Not yet implemented")
    }


    private fun validaFiltro(): String {

        var msg: String = ""

         if (cmbMarca.selectedItem == null || cmbModelo.selectedItem == null) {
            msg = "Marca/Modelo do Veículo não Selecionados"
        }
        return msg
    }


    override fun onDestroy() {
        presenterMarca.destruirView()
        presenterModelo.destruirView()
        presenterCor.destruirView()
        presenterFilial.destruirView()
        super.onDestroy()
    }

}


