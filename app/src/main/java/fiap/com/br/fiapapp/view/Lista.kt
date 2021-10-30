package fiap.com.br.fiapapp.view

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.setMargins
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import fiap.com.br.fiapapp.R
import fiap.com.br.fiapapp.model.Marca
import fiap.com.br.fiapapp.model.*
import fiap.com.br.fiapapp.presenter.MarcaContrato
import fiap.com.br.fiapapp.presenter.MarcaPresenter
import fiap.com.br.fiapapp.presenter.VeiculoContrato
import fiap.com.br.fiapapp.presenter.VeiculoPresenter
import java.io.Serializable
import kotlin.collections.ArrayList
import kotlin.math.log

class Lista : AppCompatActivity(), VeiculoContrato.VeiculoView {


    private var listaVeiculo: ArrayList<Veiculo> = ArrayList()
    private var presenterVeiculo: VeiculoContrato.VeiculoPresenter = VeiculoPresenter(this)
    private var lParamsTabl: TableLayout.LayoutParams = TableLayout.LayoutParams()
    private lateinit var tbLayout: TableLayout
    private lateinit var tbRowCabec: TableRow
    private var lParmsTabR:TableRow.LayoutParams  = TableRow.LayoutParams()
    private var codMarca: Serializable? = 0
    private var nomeMarca: Serializable? = ""
    private var codModelo: Serializable? = 0
    private var nomeModelo: Serializable = ""
    private var codCor: Serializable? = 0
    private var nomeCor: Serializable = ""
    private var codFilial: Serializable? = 0
    private var nomeFilial: Serializable = ""
    private var ano: Serializable = 0
    private var km: Serializable = 0

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.M)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        codModelo = intent.getSerializableExtra("codModelo")
        val nomeModelo = intent.getSerializableExtra("nomeModelo")

        Log.i("Lista", "Modelo = $nomeModelo")

        //val tbLayout = findViewById<TableLayout>(R.id.tblVeiculo)

        //Criando o Table Layout
            tbLayout = TableLayout(this)
            lParamsTabl = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        )
        lParamsTabl.topMargin = 1
        lParamsTabl.bottomMargin = 1

        tbLayout.layoutParams = lParamsTabl
        tbLayout.setBackgroundColor(this.getColor(R.color.white))
        tbLayout.orientation = LinearLayout.VERTICAL
        tbLayout.setColumnStretchable(0, true)
        tbLayout.setColumnStretchable(1, true)
        tbLayout.setColumnStretchable(2, true)
        tbLayout.setColumnStretchable(3, true)
        tbLayout.setColumnStretchable(4, true)
        tbLayout.setColumnStretchable(5, true)
        tbLayout.setColumnStretchable(6, true)
        //tbLayout.setPadding(10)

        //Criando o Table Row para acomodar o Cabeçalho
            tbRowCabec = TableRow(this)
            lParmsTabR = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.MATCH_PARENT
        )
        lParmsTabR.setMargins(1)
        tbRowCabec.layoutParams = lParmsTabR
        tbRowCabec.setBackgroundColor(this.getColor(R.color.white))
        //val marcas = resources.getStringArray(R.array.marcas)

        // Criando as labels do Cabeçalho
        var colCabecMarca = TextView(this)
        configLinha(colCabecMarca)

        var colCabecAno = TextView(this)
        configLinha(colCabecAno)

        var colCabecModelo = TextView(this)
        configLinha(colCabecModelo)

        var colCabecFilial = TextView(this)
        configLinha(colCabecFilial)

        var colCabecValor = TextView(this)
        configLinha(colCabecValor)

        var colCabecEditar = TextView(this)
        configLinha(colCabecEditar)

        var colCabecDeletar = TextView(this)
        configLinha(colCabecDeletar)

        colCabecMarca.setText("Marca")
        colCabecAno.setText("Ano")
        colCabecModelo.setText("Modelo")
        colCabecValor.setText("Valor")
       // colCabecAcao.setText("Ação")

        //adicionando o cabeçalho a Table Row
        tbRowCabec.addView(colCabecMarca, 0)
        tbRowCabec.addView(colCabecAno, 1)
        tbRowCabec.addView(colCabecModelo, 2)
        tbRowCabec.addView(colCabecFilial, 3)
        tbRowCabec.addView(colCabecValor, 4)
        tbRowCabec.addView(colCabecEditar, 5)
        tbRowCabec.addView(colCabecDeletar, 6)


        //adicionamento a Tabel Row a Table Layout
        tbLayout.addView(tbRowCabec)


        //var anoVeiculo = ano as Int
        //var kmVeiculo = km as Long

        presenterVeiculo.obterListaVeiculosModelo(codModelo as Int)


    }

    // Configurando o padrão de texto do cabeçalho

    @RequiresApi(Build.VERSION_CODES.M)
    private fun configLinha(objeto: TextView): TextView {

        objeto.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        objeto.setBackgroundColor(this.getColor(R.color.white))
        objeto.gravity = Gravity.CENTER
        objeto.setTextColor(this.getColor(R.color.defaultRed))
        objeto.setTypeface(Typeface.DEFAULT_BOLD)

        return objeto

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun configImageButton (objeto: ImageButton, funcao: String): ImageButton{

        objeto.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        objeto.layoutParams = TableRow.LayoutParams(27, TableRow.LayoutParams.MATCH_PARENT, 0.1f)
        objeto.isClickable = true
        objeto.setBackgroundColor(this.getColor(R.color.design_default_color_background))
        objeto.setPadding(1, 1, 1, 1)
        objeto.scaleType = ImageView.ScaleType.FIT_CENTER
        objeto.adjustViewBounds = true
        objeto.left = 0
        objeto.top = 0
        objeto.right = 0
        objeto.bottom = 0


        if (funcao == "edit"){
            objeto.layoutParams.height = 45
            objeto.layoutParams.width = 1
            objeto.setBackgroundResource(R.drawable.pencil_icon)
        }
        if (funcao == "delete"){
            objeto.layoutParams.height = 45
            objeto.layoutParams.width = 10
            objeto.setBackgroundResource(R.drawable.trash_icon)
        }


        return objeto
    }

    override fun demonstrarVeiculo(veiculo: Veiculo) {
        TODO("Not yet implemented")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun demonstrarListaVeiculos(veiculos: ArrayList<Veiculo>) {

        listaVeiculo = veiculos

        Log.i("listaVeiculo", listaVeiculo.toString())

        var listaVazia = listaVeiculo.isEmpty()

        Log.i("listaVeiculo - size", listaVazia.toString())

        if (listaVeiculo != null) {
            Log.i("listaVeiculo", "não é nulo")

            if (!listaVazia) {

                //for ( vc in listaVeiculo) {

                for ( a in 1..2) {
                    val tbRowVeiculos = TableRow(this)
                    tbRowVeiculos.layoutParams = lParmsTabR
                    tbRowVeiculos.setBackgroundColor(this.getColor(R.color.white))

                    var colMarca = TextView(this)
                    configLinha(colMarca)
                   // colMarca.setText(nomeMarca.toString())
                    colMarca.setText("marca 01")

                    var colAno = TextView(this)
                    configLinha(colAno)
                   // colAno.setText(ano.toString())
                    colAno.setText("2005")

                    var colModelo = TextView(this)
                    configLinha(colModelo)
                    // colModelo.setText(modelo.toString())
                    colModelo.setText(nomeModelo.toString())

                    var colValor = TextView(this)
                    configLinha(colValor)
                    // colModelo.setText(veiculo.valor.toString())
                  //  colModelo.setText(listaVeiculo[0].valor.toString())

                    var btnAlterar = ImageButton(this)
                    configImageButton(btnAlterar, "edit")

                    var btnExcluir = ImageButton(this)
                    configImageButton(btnExcluir, "delete")

                    tbRowVeiculos.addView(colMarca, 0)
                    tbRowVeiculos.addView(colAno, 1)
                    tbRowVeiculos.addView(colModelo, 2)
                    tbRowVeiculos.addView(colValor, 3)
                    tbRowVeiculos.addView(btnAlterar, 4)
                    tbRowVeiculos.addView(btnExcluir, 5)

                    //  tbRowVeiculos.addView(colAcao, 4 )

                    //   var colCabecAcao = TextView(this)
                    //   configLinha(colCabecAcao)

                    tbLayout.addView(tbRowVeiculos)
                }

            }

            setContentView(tbLayout)
        }

    }

    override fun demonstrarMsgErro(msg: String) {
        TODO("Not yet implemented")
    }

    override fun demonstrarMsgSucesso(msg: String) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        presenterVeiculo.destruirView()
        super.onDestroy()
    }

}