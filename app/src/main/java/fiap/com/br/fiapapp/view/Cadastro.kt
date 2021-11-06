package fiap.com.br.fiapapp.view

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isEmpty
import com.google.firebase.firestore.FirebaseFirestore
import fiap.com.br.fiapapp.R
import fiap.com.br.fiapapp.model.Cor
import fiap.com.br.fiapapp.model.Empresa
import fiap.com.br.fiapapp.model.Modelo
import fiap.com.br.fiapapp.model.Veiculo
import fiap.com.br.fiapapp.presenter.*
import fiap.com.br.fiapapp.presenter.interfaces.*
import kotlin.collections.ArrayList
import fiap.com.br.fiapapp.model.*

class Cadastro: AppCompatActivity(), VeiculoContrato.VeiculoView , MarcaContrato.ListaMarcaView,
ModeloContrato.ModeloView, CorContrato.ListaCorView, FilialContrato.FilialView {

    //private lateinit var firebaseFirestore: FirebaseFirestore
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
    private lateinit var btnLimpar: Button

    private var codCor: Int = 0

    private var nomeModelo: String = ""
    private var nomeMarca: String = ""
    private var nomeCor: String = ""
    private var razaoSocial: String = ""
    private var ano: String = ""
    private var km: String = ""
    private var valor: String = ""
    private var placa: String = ""
    private var detalhes: String = ""

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        val btnMenu = findViewById<ImageButton>(R.id.btnMenu)
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
        btnLimpar = findViewById<Button>(R.id.btnLimpar)

        presenterMarca.obtemMarca()
        presenterCor.obtemCor()
        presenterFilial.obtemRazaoSocial()


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

        btnSalvar.setOnClickListener {

            nomeMarca = cmbMarca.selectedItem.toString()

            if (cmbModelo.isEmpty()) {
                nomeModelo = ""
            }else {
                nomeModelo = cmbModelo.selectedItem.toString()
            }
            nomeCor = cmbCor.selectedItem.toString()
            razaoSocial = cmbFilial.selectedItem.toString()

            ano = dtaAno.text.toString()
            if (ano.isEmpty()){ano = "0"}

            km = etnKm.text.toString()
            if (km.isEmpty()){km = "0"}

            valor = etnValor.text.toString()
            if (valor.isEmpty()){valor = "0"}

            placa = etxPlaca.text.toString()
            detalhes = etmDetalhes.text.toString()

            var msgValidacao = presenterVeiculo.validaDadosVeiculo(nomeMarca, nomeModelo, nomeCor, razaoSocial, ano, km, valor, placa )

            if (msgValidacao.isNotEmpty()){

                Toast.makeText(this, msgValidacao, Toast.LENGTH_SHORT).show()
            }else {

                presenterVeiculo.cadastrarVeiculo(nomeMarca, nomeModelo, nomeCor, razaoSocial, km.toInt(),
                                                  valor.toDouble(), placa, detalhes, ano.toInt() )

                Limpar()
            }


        }

        btnLimpar.setOnClickListener {
            Limpar()
        }

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
        this.codCor = codCor

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
        TODO("Not yet implemented")
    }

    override fun demonstrarMsgErro(msg: String) {

        if (msg.isNotEmpty()) {

            val alertDialog = AlertDialog.Builder(this)
            alertDialog
                .setTitle("Erro!")
                .setIcon(R.drawable.alert.toDrawable())
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(
                    "ok",
                    DialogInterface.OnClickListener { dialogInterface, i ->
                    })
                .show()
        }
    }

    override fun carregarModelos(modelos: ArrayList<Modelo>) {
        TODO("Not yet implemented")
    }

    override fun carregarFiliais(filiais: ArrayList<Empresa>) {
        TODO("Not yet implemented")
    }

    override fun demonstrarVeiculo(veiculo: Veiculo) {
        TODO("Not yet implemented")
    }

    override fun demonstrarMsgSucesso(msg: String) {
        if (msg.isNotEmpty()) {

         Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun demonstrarModeloVeiculo(modelo: Modelo) {
        TODO("Not yet implemented")
    }

    override fun demonstrarMarcaVeiculo(marca: Marca) {
        TODO("Not yet implemented")
    }

    override fun demonstrarCorVeiculo(cor: Cor) {
        TODO("Not yet implemented")
    }

    override fun demonstrarFilialVeiculo(empresa: Empresa) {
        TODO("Not yet implemented")
    }

    override fun carregarVeiculos(veiculos: ArrayList<Veiculo>) {
        TODO("Not yet implemented")
    }


    override fun onDestroy() {
        presenterMarca.destruirView()
        presenterModelo.destruirView()
        presenterCor.destruirView()
        presenterFilial.destruirView()
        super.onDestroy()
    }

    private fun Limpar(){
        dtaAno.text.clear()
        etnKm.text.clear()
        etnValor.text.clear()
        etxPlaca.text.clear()
        etmDetalhes.text.clear()
        cmbCor.setSelection(0)
        cmbFilial.setSelection(0)
        cmbMarca.setSelection(0)
        cmbModelo.setSelection(0)

    }
}
