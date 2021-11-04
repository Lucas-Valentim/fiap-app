package fiap.com.br.fiapapp.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import android.widget.LinearLayout
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi


class Lista : AppCompatActivity(), VeiculoContrato.VeiculoView, FilialContrato.FilialView, ModeloContrato.ModeloView {

    // private var firestoreDB = FirebaseFirestore.getInstance()
    val presenterEmpresa: FilialContrato.FilialPresenter = FilialPresenter(this)
    val presenterVeiculo: VeiculoContrato.VeiculoPresenter = VeiculoPresenter(this)
    val presenterModelo: ModeloContrato.ModeloPresenter= ModeloPresenter(this)

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        Log.i("Consulta", "Iniciou onCreate")

        val btnMenu = findViewById<Button>(R.id.btnMenu)

        val btnFiltro = findViewById<Button>(R.id.btnFiltrar)

        btnFiltro.setOnClickListener {
            val intent = Intent(this, Consulta::class.java)
            startActivity(intent)
        }
        btnMenu.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
        }

        //Extraindo o valores selecionados na tela anterior para consulta
        /*val intent = Intent(this, Lista::class.java)*/
        val marca = intent.getIntExtra("marca", -1);
        val modelo = intent.getIntExtra("modelo", -1);
        val cor = intent.getIntExtra("cor", -1);
        val ano = intent.getIntExtra("ano", -1);
        var km = intent.getIntExtra("km", -1);
        var filial = intent.getIntExtra("filial", -1);
        var placa = intent.getStringExtra("placa");
        var valor = intent.getDoubleExtra("valor", -1.0);

        /*fun configImageButton(objeto: ImageButton, funcao: String): ImageButton {
            objeto.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            objeto.layoutParams =
                TableRow.LayoutParams(27, TableRow.LayoutParams.MATCH_PARENT, 1.0f)
            objeto.isClickable = true
            objeto.setBackgroundColor(this.getColor(R.color.design_default_color_background))
            objeto.setPadding(1, 1, 1, 1)
            objeto.scaleType = ImageView.ScaleType.FIT_CENTER
            objeto.adjustViewBounds = true
            objeto.left = 0
            objeto.top = 0
            objeto.right = 0
            objeto.bottom = 0
            if (funcao == "edit") {
                objeto.setBackgroundResource(R.drawable.pencil_icon)
            }
            if (funcao == "delete") {
                objeto.setBackgroundResource(R.drawable.trash_icon)
            }
            return objeto
        }*/

        var request = Veiculo(0, modelo, marca, cor, -1L, km, valor, ano, filial, placa, "")

        //busca o veiculo no banco
        var querylistaVeiculo = presenterVeiculo.obterListaVeiculos(request)

        querylistaVeiculo.get()
            .addOnSuccessListener { document ->
                for (doc in document) {
                    var veic = doc.toObject(Veiculo::class.java);
                    Log.i("listaVeiculo", "não é nulo")
                    var tbLayout = findViewById<TableLayout>(R.id.TableConsulta)
                    var emptyView = findViewById<View>(R.id.emptyView)
                    var newRow = TableRow(this)
                    var newView = View(this)
                    newRow = adicionarRow(veic);
                    val params: ViewGroup.LayoutParams = emptyView.layoutParams
                    newView.setLayoutParams(params)
                    newView.setBackgroundResource(R.color.offWhite)
                    tbLayout.addView(newRow)
                    tbLayout.addView(newView)
                }
            }
            .addOnFailureListener {
                Log.i("Erro", it.toString())
            }
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
        val descricaoModelo = presenterModelo.obtemDescricaoModeloPorCodigo(veic.cod_modelo)
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
        val razaoSocial = presenterEmpresa.obtemNomeFilialPorCodigo(veic.filial)
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

        val lp = LinearLayout.LayoutParams(23, 23)

        var colAcaoEdit = ImageButton(this)
        colAcaoEdit.isClickable = true
        colAcaoEdit.setBackgroundColor(R.color.lightGrey);
        colAcaoEdit.setBackgroundResource(R.color.lightGrey)
        colAcaoEdit.scaleType = ImageView.ScaleType.CENTER
        colAcaoEdit.setImageResource(R.drawable.edit_icon)
        colAcaoEdit.setOnClickListener{
            val intent = Intent(this, Registro::class.java)
            startActivity(intent)
        }

        var colAcaoDelete = ImageButton(this)
        colAcaoDelete.isClickable = true
        colAcaoDelete.setBackgroundColor(R.color.lightGrey);
        colAcaoDelete.setBackgroundResource(R.color.lightGrey)
        colAcaoDelete.scaleType = ImageView.ScaleType.CENTER
        colAcaoDelete.setImageResource(R.drawable.delete_icon)
        colAcaoDelete.setOnClickListener{
            val intent = Intent(this, Cadastro::class.java)
            startActivity(intent)
        }


        tbRowVeiculos.addView(colModelo)
        tbRowVeiculos.addView(colAno)
        tbRowVeiculos.addView(colFilial)
        tbRowVeiculos.addView(colValor)
        tbRowVeiculos.addView(colAcaoEdit)
        tbRowVeiculos.addView(colAcaoDelete)

        return tbRowVeiculos;
    }

    override fun demonstrarListaVeiculos(veiculos: ArrayList<Veiculo>) {
        TODO("Not yet implemented")
    }

    override fun demonstrarVeiculo(veiculo: Veiculo) {
        TODO("Not yet implemented")
    }

    override fun demonstraFiliais(filiais: ArrayList<String>) {
        TODO("Not yet implemented")
    }

    override fun demonstrarFilialSelecionada(codFilial: Int, razaoSocial: String) {
        TODO("Not yet implemented")
    }

    override fun demonstraModelos(modelos: ArrayList<String>) {
        TODO("Not yet implemented")
    }

    override fun demonstrarModeloSelecionado(codModelo: Int, descricao: String) {
        TODO("Not yet implemented")
    }

    override fun demonstrarMsgErro(msg: String) {
        TODO("Not yet implemented")
    }

    override fun demonstrarMsgSucesso(msg: String) {
        TODO("Not yet implemented")
    }
}