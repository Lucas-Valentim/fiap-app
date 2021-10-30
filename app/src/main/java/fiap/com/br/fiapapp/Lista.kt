package fiap.com.br.fiapapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import fiap.com.br.fiapapp.data.model.Marca
import java.util.*
import fiap.com.br.fiapapp.data.*
import fiap.com.br.fiapapp.data.model.*
import fiap.com.br.fiapapp.data.usecases.VeiculoRepository
import fiap.com.br.fiapapp.domain.usecases.veiculos.VeiculoContract
import kotlin.collections.ArrayList
import android.view.ViewGroup




class Lista : AppCompatActivity() {

    // private var firestoreDB = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        Log.i("Consulta", "Iniciou onCreate")

        var veiculoRepository = VeiculoRepository();

        //Extraindo o valores selecionados na tela anterior para consulta
        /*val intent = Intent(this, Lista::class.java)*/
        val marca = intent.getIntExtra("marca", -1);
        val modelo = intent.getIntExtra("modelo", -1);
        val cor = intent.getIntExtra("cor", -1);
        val ano = intent.getIntExtra("ano", -1);
        var km = intent.getLongExtra("km", -1);
        val filial = intent.getIntExtra("filial", -1);

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

        var request = Veiculo(0, modelo, marca, cor, filial, km, 0.00, ano, 0, "", "")

        //busca o veiculo no banco
        var querylistaVeiculo = veiculoRepository.GetVeiculo(request)

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
    }

    @SuppressLint("ResourceAsColor")
    private fun adicionarRow(veic: Veiculo) : TableRow{

        val label = findViewById<TextView>(R.id.lblMarca)
        val firstRow = findViewById<TableRow>(R.id.firstRow)
        val tbRowVeiculos = TableRow(this)
        tbRowVeiculos.setBackgroundResource(R.color.lightGrey)
        val params: ViewGroup.LayoutParams = firstRow.layoutParams
        tbRowVeiculos.setLayoutParams(params)

        var colMarca = TextView(this)
        colMarca.setBackgroundResource(R.color.lightGrey)
        colMarca.textAlignment = View.TEXT_ALIGNMENT_CENTER
        colMarca.setTextColor(R.color.black)
        val paramsLabel: ViewGroup.LayoutParams = label.layoutParams
        paramsLabel.width = label.width
        paramsLabel.height = label.height
        colMarca.gravity = Gravity.CENTER_VERTICAL
        colMarca.setLayoutParams(paramsLabel)
        colMarca.setText(veic.cod_marca.toString())

        var colModelo = TextView(this)
        colModelo.setBackgroundResource(R.color.lightGrey)
        colModelo.textAlignment = View.TEXT_ALIGNMENT_CENTER
        colModelo.setText(veic.cod_modelo.toString())

        var colAno = TextView(this)
        colAno.setBackgroundResource(R.color.lightGrey)
        colAno.textAlignment = View.TEXT_ALIGNMENT_CENTER
        colAno.setText(veic.ano.toString())

        var colFilial = TextView(this)
        colFilial.setBackgroundResource(R.color.lightGrey)
        colFilial.textAlignment = View.TEXT_ALIGNMENT_CENTER
        colFilial.setText(veic.filial.toString())

        var colValor = TextView(this)
        colValor.setBackgroundResource(R.color.lightGrey)
        colValor.textAlignment = View.TEXT_ALIGNMENT_CENTER
        colValor.setText(veic.valor.toString())

        /*var colAcaoEdit = ImageButton(this)
        colAcaoEdit.isClickable = true
        colAcaoEdit.setBackgroundColor(R.color.lightGrey);
        colAcaoEdit.setPadding(1, 1, 1, 1)
        colAcaoEdit.scaleType = ImageView.ScaleType.FIT_CENTER
        colAcaoEdit.adjustViewBounds = true
        colAcaoEdit.setBackgroundResource(R.drawable.pencil_icon)

        var colAcaoDelete = ImageButton(this)
        colAcaoDelete.isClickable = true
        colAcaoDelete.setBackgroundColor(R.color.lightGrey);
        colAcaoDelete.setPadding(1, 1, 1, 1)
        colAcaoDelete.scaleType = ImageView.ScaleType.FIT_CENTER
        colAcaoDelete.adjustViewBounds = true
        colAcaoDelete.setBackgroundResource(R.drawable.pencil_icon)*/

        tbRowVeiculos.addView(colMarca)
        tbRowVeiculos.addView(colModelo)
        tbRowVeiculos.addView(colAno)
        tbRowVeiculos.addView(colFilial)
        tbRowVeiculos.addView(colValor)
        /*tbRowVeiculos.addView(colAcaoEdit, 5)
        tbRowVeiculos.addView(colAcaoDelete, 5)*/

        //  tbRowVeiculos.addView(colAcao, 4 )

        //   var colCabecAcao = TextView(this)
        //   configLinha(colCabecAcao)

        return tbRowVeiculos;
    }
}