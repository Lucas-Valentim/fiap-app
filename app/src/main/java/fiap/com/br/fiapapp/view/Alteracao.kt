package fiap.com.br.fiapapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.isEmpty
import com.google.firebase.firestore.FirebaseFirestore
import fiap.com.br.fiapapp.R
import fiap.com.br.fiapapp.model.*
import fiap.com.br.fiapapp.presenter.*
import fiap.com.br.fiapapp.presenter.interfaces.*

class Alteracao : AppCompatActivity(), VeiculoContrato.VeiculoView, MarcaContrato.ListaMarcaView,
    ModeloContrato.ModeloView, CorContrato.ListaCorView, FilialContrato.FilialView {

   // private lateinit var firebaseFirestore: FirebaseFirestore
    private var presenterMarca: MarcaContrato.ListaMarcaPresenter = MarcaPresenter(this)
    private var presenterModelo: ModeloContrato.ModeloPresenter = ModeloPresenter(this)
    private var presenterCor: CorContrato.ListaCorPresenter = CorPresenter(this)
    private var presenterFilial: FilialContrato.FilialPresenter = FilialPresenter(this)
    private var presenterVeiculo: VeiculoContrato.VeiculoPresenter = VeiculoPresenter(this)

    private var spinnerArrayMarca = ArrayList<String>()
    private var spinnerArrayModelo = ArrayList<String>()
    private var spinnerArrayCor = ArrayList<String>()
    private var spinnerArrayFilial = ArrayList<String>()

    private lateinit var cmbMarca: Spinner
    private lateinit var cmbModelo: Spinner
    private lateinit var cmbCor: Spinner
    private lateinit var cmbFilial: Spinner
    private lateinit var btnSalvar: Button
    private lateinit var dtaAno: EditText
    private lateinit var etnKm: EditText
    private lateinit var etnValor: EditText
    private lateinit var etxPlaca: EditText
    private lateinit var etmDetalhes: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alteracao)

        val idVeiculo = intent.getStringExtra("idDocumento");

        val btnMenu = findViewById<Button>(R.id.btnMenu)

        btnMenu.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
        }

        cmbMarca = findViewById<Spinner>(R.id.cmbMarca)
        cmbModelo = findViewById<Spinner>(R.id.cmbModelo)
        cmbCor = findViewById<Spinner>(R.id.cmbCor)
        cmbFilial = findViewById<Spinner>(R.id.cmbFilial)
        btnSalvar = findViewById<Button>(R.id.btnSalvar)
        dtaAno = findViewById<EditText>(R.id.dtaAno)
        etnKm = findViewById<EditText>(R.id.etnKm)
        etnValor = findViewById<EditText>(R.id.etnValor)
        etxPlaca = findViewById<EditText>(R.id.etxPlaca)
        etmDetalhes = findViewById<EditText>(R.id.etmDetalhes)

        presenterVeiculo.obterVeiculo(idVeiculo.toString())
        presenterMarca.obtemMarca()
        presenterCor.obtemCor()
        presenterFilial.obtemRazaoSocial()

        btnSalvar.setOnClickListener {

            var marca = cmbMarca.selectedItem.toString()

            var nomeModelo = ""
            if (!cmbModelo.isEmpty()) {
               nomeModelo = cmbModelo.selectedItem.toString()
            }

            var nomeCor = cmbCor.selectedItem.toString()
            var razaoSocial = cmbFilial.selectedItem.toString()

            var ano = dtaAno.text.toString()
            if (ano.isEmpty()){ano = "0"}

            var km = etnKm.text.toString()
            if (km.isEmpty()){km = "0"}

            var valor = etnValor.text.toString()
            if (valor.isEmpty()){valor = "0"}

            var placa = etxPlaca.text.toString()
            var detalhes = etmDetalhes.text.toString()

            var msgValidacao = presenterVeiculo.validaDadosVeiculo(marca, nomeModelo, nomeCor, razaoSocial, ano, km, valor, placa)

            if (msgValidacao.isNotEmpty()){

                Toast.makeText(this, msgValidacao, Toast.LENGTH_SHORT).show()
            }else {
                presenterVeiculo.alterarVeiculo(
                    idVeiculo.toString(),
                    nomeModelo,
                    nomeCor,
                    razaoSocial,
                    km.toInt(),
                    valor.toDouble(),
                    placa.toString(),
                    detalhes.toString(),
                    ano.toInt()
                )
            }
        }

        //carregar a combo Modelo, a partir de uma marca selecionada
        cmbMarca.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                var descricaoMarca = spinnerArrayMarca[p2]
                presenterMarca.obterMarcaSelecionada(descricaoMarca)

            }
        }
    }

    private fun obterPosicao (sp: Spinner, text: String): Int{
        var encontrado = false
        var pos = 0

        while (!encontrado){

            var itemSpinner = sp.getItemAtPosition(pos).toString()

            if (text == itemSpinner ){
                encontrado = true
            }else {

                pos++
            }

        }

        return pos

    }

    override fun demonstrarVeiculo( veiculo: Veiculo ) {

        dtaAno.setText(veiculo.ano.toString())
        etnKm.setText(veiculo.km.toString())
        etnValor.setText(veiculo.valor.toString())
        etxPlaca.setText(veiculo.placa.toString())
        etmDetalhes.setText(veiculo.detalhes.toString())

    }

    override fun demonstraCores(cores: ArrayList<String>) {
        spinnerArrayCor = cores
        var adapterCor = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            spinnerArrayCor
        )
        adapterCor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        cmbCor.adapter = adapterCor
        adapterCor.notifyDataSetChanged()
    }

    override fun demonstrarCorSelecionada(codCor: Int, descricao: String) {
        TODO("Not yet implemented")
    }

    override fun demonstraRazaoSocial(filiais: ArrayList<String>) {
        spinnerArrayFilial = filiais
        var adapterFilial = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            spinnerArrayFilial
        )
        adapterFilial.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        cmbFilial.adapter = adapterFilial
        adapterFilial.notifyDataSetChanged()
    }

    override fun demonstraMarcas(marcas: ArrayList<String>) {
        spinnerArrayMarca = marcas
        var adapterMarca = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            spinnerArrayMarca
        )
        adapterMarca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        cmbMarca.adapter = adapterMarca
        adapterMarca.notifyDataSetChanged()
    }

    override fun demonstrarMarcaSelecionada(codMarca: Int, descricao: String) {
        presenterModelo.obtemModelo(codMarca)
    }

    override fun demonstraModelos(modelos: ArrayList<String>) {
        spinnerArrayModelo = modelos
        var adapterModelo = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            spinnerArrayModelo
        )
        adapterModelo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        cmbModelo.adapter = adapterModelo
        adapterModelo.notifyDataSetChanged()
    }

    override fun demonstrarModelosMarcaSel(modelos: ArrayList<String>, descrModeloSel: String) {
        spinnerArrayModelo = modelos
        var adapterModelo = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            spinnerArrayModelo
        )
        adapterModelo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        cmbModelo.adapter = adapterModelo

        var posicao = obterPosicao(cmbModelo, descrModeloSel)
        cmbModelo.setSelection(posicao)
        adapterModelo.notifyDataSetChanged()
        cmbModelo.setSelection(posicao)




    }

    override fun demonstrarMsgErro(msg: String) {
        if (msg.isNotEmpty()) {

            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun carregarModelos(modelos: ArrayList<Modelo>) {
        TODO("Not yet implemented")
    }

    override fun carregarFiliais(filiais: ArrayList<Empresa>) {
        TODO("Not yet implemented")
    }

    override fun demonstrarMsgSucesso(msg: String) {

        if (msg.isNotEmpty()) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun demonstrarModeloVeiculo(modelo: Modelo) {

        presenterModelo.obtemModeloMarcaSel(modelo.cod_marca, modelo.descricao)

    }

    override fun demonstrarMarcaVeiculo(marca: Marca) {

        var posicao = obterPosicao(cmbMarca, marca.descricao)
        cmbMarca.setSelection(posicao)

    }

    override fun demonstrarCorVeiculo(cor: Cor) {

        var posicao = obterPosicao(cmbCor, cor.descricao)
        cmbCor.setSelection(posicao)

    }

    override fun demonstrarFilialVeiculo(empresa: Empresa) {
        var posicao = obterPosicao(cmbFilial, empresa.razao_social)
        cmbFilial.setSelection(posicao)
    }

    override fun carregarVeiculos(veiculos: ArrayList<Veiculo>) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        presenterMarca.destruirView()
        presenterModelo.destruirView()
        presenterCor.destruirView()
        presenterFilial.destruirView()
        presenterVeiculo.destruirView()
        super.onDestroy()
    }
}