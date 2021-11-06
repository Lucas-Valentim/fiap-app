package fiap.com.br.fiapapp.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.*
import kotlin.collections.ArrayList
import android.view.ViewGroup
import fiap.com.br.fiapapp.R
import fiap.com.br.fiapapp.model.Veiculo
import fiap.com.br.fiapapp.presenter.*
import fiap.com.br.fiapapp.presenter.interfaces.FilialContrato
import fiap.com.br.fiapapp.presenter.interfaces.ModeloContrato
import fiap.com.br.fiapapp.presenter.interfaces.VeiculoContrato
import android.os.Build
import androidx.annotation.RequiresApi
import fiap.com.br.fiapapp.model.*
import fiap.com.br.fiapapp.model.Empresa
import fiap.com.br.fiapapp.model.Modelo


class Lista : AppCompatActivity(), VeiculoContrato.VeiculoView, FilialContrato.FilialView, ModeloContrato.ModeloView {

    val presenterEmpresa: FilialContrato.FilialPresenter = FilialPresenter(this)
    val presenterVeiculo: VeiculoContrato.VeiculoPresenter = VeiculoPresenter(this)
    val presenterModelo: ModeloContrato.ModeloPresenter= ModeloPresenter(this)
    var listaFiliais: ArrayList<Empresa> = ArrayList()
    var listaModelos: ArrayList<Modelo> = ArrayList()
    var listaVeiculos: ArrayList<Veiculo> = ArrayList()


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        presenterEmpresa.obtemTodasFiliais()
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE


        val btnMenu = findViewById<ImageButton>(R.id.btnMenu)
        btnMenu.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
        }

        val btnFiltro = findViewById<Button>(R.id.btnFiltrar)
        btnFiltro.setOnClickListener {
            val intent = Intent(this, Consulta::class.java)
            startActivity(intent)
        }

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun montarTable(){
        //Extraindo o valores selecionados na tela anterior para consulta
        val marca = intent.getIntExtra("marca", -1);
        val modelo = intent.getIntExtra("modelo", -1);
        val ano = intent.getIntExtra("ano", -1);

        var listVeiculo: List<Veiculo> = listaVeiculos
        if (ano > 0)
            listVeiculo = listVeiculo.filter { veic -> veic.ano == ano }
        if (modelo > 0)
            listVeiculo = listVeiculo.filter { veic -> veic.cod_modelo == modelo }
        if (marca > 0)
            listVeiculo = listVeiculo.filter { veic -> veic.cod_marca == marca }

        //Limpa os dados
        val tableLayout = findViewById<TableLayout>(R.id.TableConsulta)
        if(tableLayout.childCount > 2)
            tableLayout.removeViews(2, tableLayout.childCount - 2)

        if(listVeiculo.count() == 0){
            val tbLayout = findViewById<TableLayout>(R.id.TableConsulta)
            var text = TextView(this)
            text.setText("Não há dados para exibição")
            text.gravity = Gravity.CENTER_VERTICAL
            text.textAlignment = View.TEXT_ALIGNMENT_CENTER
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
            tbLayout.addView(text)


        }

        listVeiculo.forEach {
            var tbLayout = findViewById<TableLayout>(R.id.TableConsulta)
            var emptyView = findViewById<View>(R.id.emptyView)
            var newDataRow = TableRow(this)
            var whiteSpace = View(this)
            newDataRow = adicionarRow(it);
            val params: ViewGroup.LayoutParams = emptyView.layoutParams
            whiteSpace.setLayoutParams(params)
            whiteSpace.setBackgroundResource(R.color.offWhite)
            tbLayout.addView(newDataRow)
            tbLayout.addView(whiteSpace)
        }
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.INVISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("ResourceAsColor", "UseCompatLoadingForDrawables")
    private fun adicionarRow(veic: Veiculo) : TableRow{

        val labelModelo = findViewById<TextView>(R.id.lblModelo)
        val labelAno = findViewById<TextView>(R.id.lblAno)
        val labelFilial = findViewById<TextView>(R.id.lblFilial)
        val labelValor = findViewById<TextView>(R.id.lblValor)
        val firstRow = findViewById<TableRow>(R.id.firstRow)
        val tbRowVeiculos = TableRow(this)
        tbRowVeiculos.setBackgroundResource(R.color.lightGrey)
        val params: ViewGroup.LayoutParams = firstRow.layoutParams
        tbRowVeiculos.setLayoutParams(params)

        var colModelo = TextView(this)
        colModelo.setBackgroundResource(R.color.lightGrey)
        colModelo.textAlignment = View.TEXT_ALIGNMENT_CENTER
        colModelo.setTextColor(R.color.black)
        val paramsModelo: ViewGroup.LayoutParams = labelModelo.layoutParams
        paramsModelo.width = labelModelo.width
        paramsModelo.height = labelModelo.height
        colModelo.gravity = Gravity.CENTER_VERTICAL
        colModelo.setLayoutParams(paramsModelo)
        val descricaoModelo = listaModelos.find { mod -> mod.cod_modelo == veic.cod_modelo }?.descricao
        colModelo.setText(descricaoModelo)

        var colAno = TextView(this)
        colAno.setBackgroundResource(R.color.lightGrey)
        colAno.textAlignment = View.TEXT_ALIGNMENT_CENTER
        colAno.setTextColor(R.color.black)
        val paramsAno: ViewGroup.LayoutParams = labelAno.layoutParams
        paramsAno.width = labelAno.width
        paramsAno.height = labelAno.height
        colAno.gravity = Gravity.CENTER_VERTICAL
        colAno.setLayoutParams(paramsAno)
        colAno.setText(veic.ano.toString())

        var colFilial = TextView(this)
        colFilial.setBackgroundResource(R.color.lightGrey)
        colFilial.textAlignment = View.TEXT_ALIGNMENT_CENTER
        colFilial.setTextColor(R.color.black)
        val paramsFilial: ViewGroup.LayoutParams = labelFilial.layoutParams
        paramsFilial.width = labelFilial.width
        paramsFilial.height = labelFilial.height
        colFilial.gravity = Gravity.CENTER_VERTICAL
        colFilial.setLayoutParams(paramsFilial)
        val razaoSocial = listaFiliais.filter { fil -> fil.cnpj == veic.cnpj }.firstOrNull()?.razao_social
        colFilial.setText(razaoSocial)

        var colValor = TextView(this)
        colValor.setBackgroundResource(R.color.lightGrey)
        colValor.textAlignment = View.TEXT_ALIGNMENT_CENTER
        colValor.setTextColor(R.color.black)
        val paramsValor: ViewGroup.LayoutParams = labelValor.layoutParams
        paramsValor.width = labelValor.width
        paramsValor.height = labelValor.height
        colValor.gravity = Gravity.CENTER_VERTICAL
        colValor.setLayoutParams(paramsValor)
        colValor.setText(veic.valor.toString())

        var colAcaoEdit = ImageButton(this)
        colAcaoEdit.isClickable = true
        colAcaoEdit.setBackgroundColor(R.color.lightGrey);
        colAcaoEdit.setBackgroundResource(R.color.lightGrey)
        colAcaoEdit.scaleType = ImageView.ScaleType.CENTER
        colAcaoEdit.setImageResource(R.drawable.edit_icon)

        colAcaoEdit.setOnClickListener{
            val intent = Intent(this, Alteracao::class.java)
            intent.putExtra("idDocumento", veic.id)
            startActivity(intent)
        }

        var colAcaoDelete = ImageButton(this)
        colAcaoDelete.isClickable = true
        colAcaoDelete.setBackgroundColor(R.color.lightGrey);
        colAcaoDelete.setBackgroundResource(R.color.lightGrey)
        colAcaoDelete.scaleType = ImageView.ScaleType.CENTER
        colAcaoDelete.setImageResource(R.drawable.delete_icon)

        colAcaoDelete.setOnClickListener{
            val progressBar = findViewById<ProgressBar>(R.id.progressBar)
            progressBar.visibility = View.VISIBLE
            presenterVeiculo.excluirVeiculo(veic.id);
        }

        tbRowVeiculos.addView(colModelo)
        tbRowVeiculos.addView(colAno)
        tbRowVeiculos.addView(colFilial)
        tbRowVeiculos.addView(colValor)
        tbRowVeiculos.addView(colAcaoEdit)
        tbRowVeiculos.addView(colAcaoDelete)

        return tbRowVeiculos;
    }

    override fun demonstrarVeiculo(veiculo: Veiculo) {
        TODO("Not yet implemented")
    }

    override fun demonstraRazaoSocial(filiais: ArrayList<String>) {
        TODO("Not yet implemented")
    }

    override fun demonstraModelos(modelos: ArrayList<String>) {
        TODO("Not yet implemented")
    }

    override fun demonstrarModelosMarcaSel(modelos: ArrayList<String>, descrModeloSel: String) {
        TODO("Not yet implemented")
    }

    override fun demonstrarMsgErro(msg: String) {
        TODO("Not yet implemented")
    }

    override fun carregarModelos(modelos: ArrayList<Modelo>) {
        listaModelos = modelos
        presenterVeiculo.obterListaVeiculos();
    }

    override fun carregarFiliais(filiais: ArrayList<Empresa>) {
        listaFiliais = filiais
        presenterModelo.obtemTodosModelos()
    }

    override fun demonstrarMsgSucesso(msg: String) {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.INVISIBLE
        var toast = Toast.makeText(this, msg, Toast.LENGTH_LONG)
        toast.show()
        presenterVeiculo.obterListaVeiculos();
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun carregarVeiculos(veiculos: ArrayList<Veiculo>) {
        listaVeiculos = veiculos
        montarTable();
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
}