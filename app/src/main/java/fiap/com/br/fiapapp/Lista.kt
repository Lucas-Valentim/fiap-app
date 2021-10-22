package fiap.com.br.fiapapp

import android.annotation.SuppressLint
import android.app.ActionBar
import android.graphics.Typeface
import android.icu.lang.UCharacter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import android.text.Layout
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.core.View
import fiap.com.br.fiapapp.R
import fiap.com.br.fiapapp.data.Marca
import org.w3c.dom.Text
import java.util.*
import kotlin.math.log
import fiap.com.br.fiapapp.data.*
import kotlin.collections.ArrayList

class Lista : AppCompatActivity() {

    // private var firestoreDB = FirebaseFirestore.getInstance()
    //private var docMarca = FirebaseFirestore.getInstance().collection("marca_veiculo")
    private var docModelo = FirebaseFirestore.getInstance().collection("modelo_veiculo")
    private var docCor = FirebaseFirestore.getInstance().collection("cor_veiculo")
    private var docEmpresa = FirebaseFirestore.getInstance().collection("empresa")
    private var docVeiculo = FirebaseFirestore.getInstance().collection("veiculo")

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.M)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        val intent = intent
        val marca = intent.getSerializableExtra("marca")
        val modelo = intent.getSerializableExtra("modelo")
        val cor = intent.getSerializableExtra("cor")
        val ano = intent.getSerializableExtra("ano")
        var km = intent.getSerializableExtra("km")
        var codMarca2: Int

        //Extraindo o código da empresa selecionada na tela anterior
        val filial = intent.getSerializableExtra("filial")
        var textArray: List<String> = filial.toString().split(";")
        var cod_empresa = textArray[0].toInt()


        Log.i("Lista", "Marca  = $marca")
        Log.i("Lista", "Modelo = $modelo")
        Log.i("Lista", "Cor = $cor")
        Log.i("Lista", "Filial = $filial")

        //val tbLayout = findViewById<TableLayout>(R.id.tblVeiculo)

        //Criando o Table Layout
        val tbLayout = TableLayout(this)
        val lParamsTabl = TableLayout.LayoutParams(
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
        //tbLayout.setPadding(10)

        //Criando o Table Row para acomodar o Cabeçalho
        val tbRowCabec = TableRow(this)
        val lParmsTabR = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.MATCH_PARENT
        )
        lParmsTabR.setMargins(1)
        tbRowCabec.layoutParams = lParmsTabR
        tbRowCabec.setBackgroundColor(this.getColor(R.color.white))
        val marcas = resources.getStringArray(R.array.marcas)


        // Configurando o padrão de texto do cabeçalho

        fun configLinha(objeto: TextView): TextView {

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

        fun configImageButton (objeto: ImageButton, funcao: String): ImageButton{

            objeto.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            objeto.layoutParams = TableRow.LayoutParams(27, TableRow.LayoutParams.MATCH_PARENT, 1.0f)
            objeto.isClickable = true
            objeto.setBackgroundColor(this.getColor(R.color.design_default_color_background))
            objeto.setPadding(1, 1, 1, 1)
            objeto.scaleType = ImageView.ScaleType.FIT_CENTER
            objeto.adjustViewBounds = true
            objeto.left = 0
            objeto.top = 0
            objeto.right = 0
            objeto.bottom = 0

            if (funcao == "edit"){ objeto.setBackgroundResource(R.drawable.pencil_icon)}
            if (funcao == "delete"){ objeto.setBackgroundResource(R.drawable.trash_icon)}


            return objeto
        }

        fun getCodMarca(descrMarca: String): Int {
            var codMarca: Int = 0
            var docMarca = FirebaseFirestore.getInstance().collection("marca_veiculo")
            val docRef = docMarca.whereEqualTo("descricao", descrMarca).get()
                //val docRef = docMarca.whereArrayContains("descricao", descrMarca).get()
                .addOnCompleteListener(OnCompleteListener {

                    for (dataObject in it.getResult()!!.documents) {
                        val marca = dataObject.toObject(Marca::class.java)!!
                        codMarca = marca.codmarca
                        Log.i("Código da Marca", codMarca.toString())

                    }

                })
                .addOnFailureListener { e -> Log.e("onItemSelected", "onFailure()", e) }

            return codMarca

        }


        fun getCodModelo(descrModelo: String): Int {

            var codModelo: Int = 0
            docModelo.whereEqualTo("descricao", descrModelo).get()

                .addOnCompleteListener(OnCompleteListener {

                    for (dataObject in it.getResult()!!.documents) {
                        val modelo = dataObject.toObject(Modelo::class.java)!!
                        codModelo = modelo.cod_modelo
                        Log.i("Código da Modelo", codModelo.toString())

                    }

                })
                .addOnFailureListener { e -> Log.e("onItemSelected", "onFailure()", e) }
            return codModelo

        }


        fun getCodCor(descrCor: String): Int {
            var codCor: Int = 0
            // val docRef = docCor.whereEqualTo("descricao", "$descr_cor")
            docCor.whereEqualTo("descricao", descrCor).get()

                .addOnCompleteListener(OnCompleteListener {

                    for (dataObject in it.getResult()!!.documents) {
                        val cor = dataObject.toObject(Cor::class.java)!!
                        codCor = cor.cod_cor
                        Log.i("Código da Cor", codCor.toString())

                    }

                })
                .addOnFailureListener { e -> Log.e("onItemSelected", "onFailure()", e) }
            return codCor

        }

        fun bucaVeiculos(
            cod_marca: Int, cod_modelo: Int, cod_cor: Int,
            ano: Int, km: Long, cod_empresa: Int
        ): ArrayList<Veiculo> {

            var veiculo = Veiculo()
            var arrayVeiculo: ArrayList<Veiculo> = ArrayList()

            val docRef = docVeiculo.whereEqualTo("cod_modelo", 1)
            //  val docRef = docVeiculo.whereEqualTo("cod_marca", cod_marca)
            // .whereGreaterThanOrEqualTo("cod_modelo", cod_modelo)
            //  .whereGreaterThanOrEqualTo("cod_cor", cod_cor)
            //  .whereGreaterThanOrEqualTo("ano", ano)
            //  .whereGreaterThanOrEqualTo("km", km)
            //  .whereGreaterThanOrEqualTo("cod_empresa", cod_empresa)


            docRef.get().addOnCompleteListener(OnCompleteListener {

                for (dataObject in it.getResult()!!.documents) {
                    veiculo = dataObject.toObject(Veiculo::class.java)!!

                    Log.i("Valor BuscarVeiculo 1 ", veiculo.valor.toString())

                    arrayVeiculo.add(veiculo)

                     Log.i("Valor BuscarVeiculo 2", veiculo.valor.toString())

                }

            })
                .addOnFailureListener { e -> Log.e("onItemSelected", "onFailure()", e) }

            // while (arrayVeiculo.size)

            return arrayVeiculo
        }


        // Criando as labels do Cabeçalho
        var colCabecMarca = TextView(this)
        configLinha(colCabecMarca)

        var colCabecAno = TextView(this)
        configLinha(colCabecAno)

        var colCabecModelo = TextView(this)
        configLinha(colCabecModelo)

        var colCabecValor = TextView(this)
        configLinha(colCabecValor)

        var colCabecAcao = TextView(this)
        configLinha(colCabecAcao)

        colCabecMarca.setText("Marca")
        colCabecAno.setText("Ano")
        colCabecModelo.setText("Modelo")
        colCabecValor.setText("Valor")
        colCabecAcao.setText("Ação")

        //adicionando o cabeçalho a Table Row
        tbRowCabec.addView(colCabecMarca, 0)
        tbRowCabec.addView(colCabecAno, 1)
        tbRowCabec.addView(colCabecModelo, 2)
        tbRowCabec.addView(colCabecValor, 3)
        tbRowCabec.addView(colCabecAcao, 4)

        //adicionamento a Tabel Row a Table Layout
        tbLayout.addView(tbRowCabec)


        var cod_marca = getCodMarca(marca.toString())
        var cod_modelo = getCodModelo(modelo.toString())
        var cod_cor = getCodCor(cor.toString())

        var anoVeiculo = ano as Int
        var kmVeiculo = km as Long

        var listaVeiculo = bucaVeiculos(cod_marca, cod_modelo, cod_cor, anoVeiculo, kmVeiculo, cod_empresa)

        var sizeLista = listaVeiculo.size

        Log.i("listaVeiculo - size", sizeLista.toString())

        if (listaVeiculo != null) {
            Log.i("listaVeiculo", "não é nulo")

            if (sizeLista >= 0) {

                //for ( vc in listaVeiculo) {

                  for ( a in 1..2) {
                    val tbRowVeiculos = TableRow(this)
                    tbRowVeiculos.layoutParams = lParmsTabR
                    tbRowVeiculos.setBackgroundColor(this.getColor(R.color.white))

                    var colMarca = TextView(this)
                    configLinha(colMarca)
                    colMarca.setText(marca.toString())

                    var colAno = TextView(this)
                    configLinha(colAno)
                    colAno.setText(ano.toString())

                    var colModelo = TextView(this)
                    configLinha(colModelo)
                   // colModelo.setText(modelo.toString())
                      colModelo.setText("Fiat Uno")

                    var colValor = TextView(this)
                    configLinha(colValor)
                   // colModelo.setText(veiculo.valor.toString())
                    colModelo.setText("2.500,00")

                    var btnAlterar = ImageButton(this)
                    configImageButton(btnAlterar, "edit")

                    var btnExcluir = ImageButton(this)
                    configImageButton(btnAlterar, "delete")

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
        }

        setContentView(tbLayout)
    }
}