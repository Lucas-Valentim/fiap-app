package fiap.com.br.fiapapp.view

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toDrawable
import com.google.android.gms.tasks.OnSuccessListener
import fiap.com.br.fiapapp.R
import fiap.com.br.fiapapp.model.Cor
import fiap.com.br.fiapapp.model.Marca
import fiap.com.br.fiapapp.model.Modelo
import fiap.com.br.fiapapp.model.Veiculo
import fiap.com.br.fiapapp.presenter.*
import fiap.com.br.fiapapp.presenter.interfaces.*
import java.util.*
import kotlin.collections.ArrayList

class Cadastro: AppCompatActivity(), VeiculoContrato.VeiculoView , MarcaContrato.ListaMarcaView,
ModeloContrato.ListaModeloView, CorContrato.ListaCorView, FilialContrato.ListaFilialView {


    private var presenterMarca: MarcaContrato.ListaMarcaPresenter = MarcaPresenter(this)
    private var presenterModelo: ModeloContrato.ListaModeloPresenter = ModeloPresenter(this)
    private var presenterCor: CorContrato.ListaCorPresenter = CorPresenter(this)
    private var presenterFilial: FilialContrato.ListaFilialPresenter = FilialPresenter(this)
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
    private var codModelo: Int = 0
    private var codMarca: Int = 0

    private var nomeModelo: String = ""
    private var nomeCor: String = ""
    private var filial: String = ""
    private lateinit var textArray: List<String>
    private var cnpjEmpresa: String = ""
    private var ano: String = ""
    private var km: String = ""
    private var valor: String = ""
    private var placa: String = ""
    private var detalhes: String = ""

    private final val ANO_INICIO = 1900



    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        val btnMenu = findViewById<Button>(R.id.btnMenu)
        btnMenu.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
        }

        //val cmbMarca = resources.getStringArray(R.array.marcas)
        /*Populando o combo marca
        val spinner = findViewById<Spinner>(R.id.cmbMarca)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, marcas)
        adapter.notifyDataSetChanged()
        spinner.adapter = adapter */

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
        presenterFilial.obtemFilial()


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

            nomeModelo = cmbModelo.selectedItem.toString()
            nomeCor = cmbCor.selectedItem.toString()
            filial = cmbFilial.selectedItem.toString()
            textArray = filial.toString().split("/")
            cnpjEmpresa = textArray[0].toString()
            ano = dtaAno.text.toString()
            km = etnKm.text.toString()
            valor = etnValor.text.toString()
            placa = etxPlaca.text.toString()
            detalhes = etmDetalhes.text.toString()

            var msg = validaFiltro(nomeModelo, nomeCor, cnpjEmpresa, ano, km, valor, placa)

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

                var requestModelo = Modelo(0, 0, nomeModelo, 0)
                var queryModeloSelecionado = presenterModelo.obterCodDescrModelo(requestModelo)
                Log.i("Query MOdelo", queryModeloSelecionado.toString())


                queryModeloSelecionado.get()
                    .addOnSuccessListener { document ->
                        for (doc in document) {
                            var modeloSel = doc.toObject(Modelo::class.java);
                            codModelo = modeloSel.cod_modelo

                            Log.i("Modelo Selecionado", "recuperada ok! ${codModelo.toString()}")
                        }

                    }/*
                    .addOnFailureListener {e ->
                        Log.e("Modelo Selecionado", "erro !!", e)
                        //  Toast.makeText(this, "Não foi possível recuperar a Marca escolhida", Toast.LENGTH_SHORT).show()
                    } */

                var requestCor = Cor(0, nomeCor)
                var queryCorSelecionado = presenterCor.obterCodDescrCor(requestCor)
                queryCorSelecionado.get()
                    .addOnSuccessListener { document ->
                        for (doc in document) {
                            var corSel = doc.toObject(Cor::class.java);
                            codCor = corSel.cod_cor
                            Log.i("Cor Selecionado", "recuperada ok! ${codCor.toString()}")
                        }

                    }
/*
                    .addOnFailureListener {
                        //  Toast.makeText(this, "Não foi possível recuperar a Marca escolhida", Toast.LENGTH_SHORT).show()
                    }*/

                presenterVeiculo.cadastrarVeiculo(
                    codModelo, codCor, cnpjEmpresa.toLong(), km.toInt(),
                    valor.toDouble(), placa, detalhes
                )
            }

        }

        btnLimpar.setOnClickListener {

            dtaAno.text.clear()
            etnKm.text.clear()
            etnValor.text.clear()
            etxPlaca.text.clear()
            etmDetalhes.text.clear()

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

    override fun demonstraFiliais(filiais: ArrayList<String>) {
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

    override fun demonstrarFilialSelecionada(codFilial: Int, razaoSocial: String) {
        TODO("Not yet implemented")
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

    override fun demonstrarModeloSelecionado(codModelo: Int, descricao: String) {
        this.codModelo = codModelo
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
                        // Toast.makeText(this, "Aceite Usuario", Toast.LENGTH_SHORT).show()

                        //Problema: Verificar como colocar o foco no campo que está inconsistente
                    })
                .show()
        }
    }

    override fun demonstrarListaVeiculos(veiculos: ArrayList<Veiculo>) {
        TODO("Not yet implemented")
    }

    override fun demonstrarVeiculo(veiculo: Veiculo) {
        TODO("Not yet implemented")
    }

    override fun demonstrarMsgSucesso(msg: String) {
        if (msg.isNotEmpty()) {

            val alertDialog = AlertDialog.Builder(this)
            alertDialog
                .setTitle("Sucesso!")
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

        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun validaFiltro(
        modelo: String, cor: String, cnpjEmpresa: String, ano: String, km: String, valor: String,
        placa: String
    ): String {
        var msg: String = ""

        if (modelo.isNullOrEmpty()) {
            msg = "Modelo do Veículo não Selecionados"
        }

        if (cor.isNullOrEmpty()) {
            msg = "Cor do Veículo não Selecionados"
        }

        if (cnpjEmpresa.isNullOrEmpty()) {
            msg = "Filial não Selecionada"
        }

        if (ano.isNullOrEmpty()) {
            msg = "Ano não informado"
        }

        if (km.isNullOrEmpty()) {
            msg = "KM não informado"
        }

        if (placa.isNullOrEmpty()) {
            msg = "Placa não informada"
        }

        if (valor.isNullOrEmpty()) {
            msg = "Valor não informado"
        } else {

            var aux = valor.toDouble()

            if (aux <= 0) {
                msg = "Valor menor ou igual a 0"
            }

        }

        var formatData: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy" )
        var dataAtual = formatData.format(Date())
        var anoAtual = (dataAtual.substring(6)).toInt()

        var anoVeiculo = ano.toInt()

        if (anoVeiculo < ANO_INICIO || anoVeiculo > anoAtual ){

            msg = "Ano informado inválido"

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
