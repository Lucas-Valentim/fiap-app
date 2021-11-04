package fiap.com.br.fiapapp.view

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import fiap.com.br.fiapapp.R
import fiap.com.br.fiapapp.presenter.*
import fiap.com.br.fiapapp.presenter.interfaces.CorContrato
import fiap.com.br.fiapapp.presenter.interfaces.FilialContrato
import fiap.com.br.fiapapp.presenter.interfaces.MarcaContrato
import fiap.com.br.fiapapp.presenter.interfaces.ModeloContrato


class Consulta : AppCompatActivity(), MarcaContrato.ListaMarcaView, ModeloContrato.ListaModeloView,
CorContrato.ListaCorView, FilialContrato.ListaFilialView{

    private var presenterMarca: MarcaContrato.ListaMarcaPresenter = MarcaPresenter(this)
    private var presenterModelo: ModeloContrato.ListaModeloPresenter = ModeloPresenter(this)
    private var presenterCor: CorContrato.ListaCorPresenter = CorPresenter(this)
    private var presenterFilial: FilialContrato.ListaFilialPresenter = FilialPresenter(this)
    private var marcaSerlecionada: Int = 0


    //var intent = Intent(this, Lista::class.java)
    private var spinnerArrayMarca = ArrayList<String>()
    private var spinnerArrayModelo = ArrayList<String>()
    //private var spinnerArrayCor= ArrayList<String>()
    //private var spinnerArrayFilial= ArrayList<String>()


    private lateinit var cmbMarca: Spinner
    private lateinit var cmbModelo: Spinner
    //private lateinit var cmbCor: Spinner
    //private lateinit var cmbFilial: Spinner

    @RequiresApi(Build.VERSION_CODES.O)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consulta)

        val btnMenu = findViewById<Button>(R.id.btnMenu)
        btnMenu.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
        }

        cmbMarca = findViewById<Spinner>(R.id.cmbMarca)
        cmbModelo = findViewById<Spinner>(R.id.cmbModelo)
        //cmbCor = findViewById<Spinner>(R.id.cmbCor)
        //cmbFilial = findViewById<Spinner>(R.id.cmbFilial)

        val btnFiltrar = findViewById<Button>(R.id.btnFiltrar)

        presenterMarca.obtemMarca()
        presenterCor.obtemCor()
        presenterFilial.obtemFilial()
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
                var teste = marcaSerlecionada;

                if (msg.isNotEmpty()) {

                    val alertDialog = AlertDialog.Builder(this)
                    alertDialog
                        .setTitle("Aviso!")
                        .setIcon(R.drawable.alert.toDrawable())
                        .setMessage(msg)
                        .setCancelable(false)
                        .setPositiveButton(
                            "ok",
                            DialogInterface.OnClickListener { dialogInterface, i ->
                                // Toast.makeText(this, "Aceite Usuario", Toast.LENGTH_SHORT).show()

                                //Problema: Verificar como colocar o foco no campo que está inconsistente
                            })
                        .show()
                } else {

                    cmbMarca = findViewById<Spinner>(R.id.cmbMarca)
                    cmbModelo = findViewById<Spinner>(R.id.cmbModelo)
                    //cmbCor = findViewById<Spinner>(R.id.cmbCor)
                    //cmbFilial = findViewById<Spinner>(R.id.cmbFilial)
                    val txtdtaAno = findViewById<EditText>(R.id.dtaAno)
                    //val txtetnKm = findViewById<EditText>(R.id.etnKm)
                    //val txtPlaca = findViewById<EditText>(R.id.etnPlaca)
                    //val txtValor = findViewById<EditText>(R.id.etnValor)

                    intent = Intent(this, Lista::class.java)

                    if(!cmbMarca.selectedItem.toString().isNullOrEmpty() && cmbMarca.selectedItem.toString() != "Selecione" ){
                        var marca = presenterMarca.obtemMarcaPorNome(cmbMarca.selectedItem.toString())
                        intent.putExtra("marca", marca)
                    }
                    if(!cmbModelo.selectedItem .toString().isNullOrEmpty() && cmbModelo.selectedItem.toString() != "Selecione" ){
                        var modelo = presenterModelo.obtemModeloPorNome(cmbModelo.selectedItem.toString())
                        intent.putExtra("modelo", modelo)
                    }
                    /*if(!cmbCor.selectedItem.toString().isNullOrEmpty() && cmbCor.selectedItem.toString() != "Selecione" ){
                        var cor = presenterCor.obtemCorPorNome(cmbCor.selectedItem.toString())
                        intent.putExtra("cor", cor)
                    }
                    if(!cmbFilial.selectedItem.toString().isNullOrEmpty() && cmbFilial.selectedItem.toString() != "Selecione" ){
                        var filial = presenterFilial.obtemFilialPorNome(cmbFilial.selectedItem.toString())
                        intent.putExtra("filial", filial)
                    }*/
                    if(!txtdtaAno.text.isNullOrEmpty()){
                        intent.putExtra("ano", txtdtaAno.text.toString().toInt())
                    }
                    /*if(!txtetnKm.text.isNullOrEmpty()){
                        intent.putExtra("km", txtetnKm.text.toString().toInt())
                    }
                    if(!txtPlaca.text.isNullOrEmpty()){
                        intent.putExtra("placa", txtPlaca.text.toString())
                    }
                    if(!txtValor.text.isNullOrEmpty()){
                        intent.putExtra("valor", txtValor.text.toString().toDouble())
                    }*/

                    startActivity(intent)
                    /*if (cmbModelo.selectedItem != null){
                        var nomeModelo = cmbModelo.selectedItem.toString()
                        presenterModelo.obterModeloSelecionado(nomeModelo)
                    }*/

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

    override fun demonstrarModeloSelecionado(codModelo: Int, descricao: String) {
        this.intent.putExtra("codModelo", codModelo)
        this.intent.putExtra("nomeModelo", descricao)
        startActivity(intent)

    }

    override fun demonstrarMarcaSelecionada(codMarca: Int, descricao: String) {

        presenterModelo.obtemModelo(codMarca)
       // this.intent.putExtra("codMarca", codMarca)
       // this.intent.putExtra("nomeMarca", descricao)
    }

    override fun demonstraCores(cores: ArrayList<String>) {
        //spinnerArrayCor = cores
        //var adapterCor = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArrayCor)
        //adapterCor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //cmbCor.adapter = adapterCor
        //adapterCor.notifyDataSetChanged()
    }

    override fun demonstrarCorSelecionada(codCor: Int, descricao: String) {

    }

    override fun demonstraFiliais(filiais: ArrayList<String>) {
        //spinnerArrayFilial = filiais
        //var adapterFilial = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArrayFilial)
        //adapterFilial.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //cmbFilial.adapter = adapterFilial
        //adapterFilial.notifyDataSetChanged()
    }

    override fun demonstrarFilialSelecionada(codFilial: Int, razaoSocial: String) {
       // intent.putExtra("codFilial", codFilial)
       // intent.putExtra("nomeFilial", razaoSocial)

    }

    override fun demonstrarMsgErro(msg: String) {
        TODO("Not yet implemented")
        //implementar poup-up de erro
    }


    private fun validaFiltro(): String {

        var msg: String = ""

         if (cmbMarca.selectedItem == null || cmbModelo.selectedItem == null) {
        //if (cmbMarca.selectedItem == null ) {
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


