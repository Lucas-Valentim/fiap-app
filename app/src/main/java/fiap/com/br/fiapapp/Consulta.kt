package fiap.com.br.fiapapp

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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import fiap.com.br.fiapapp.data.model.*
import fiap.com.br.fiapapp.data.usecases.*


class Consulta : AppCompatActivity() {

    private var functions = GetCombosData()
    var veiculoRepository = VeiculoRepository();
    private var spinnerArrayMarca = ArrayList<String>()
    private var spinnerArrayModelo = ArrayList<String>()
    private var spinnerArrayCor = ArrayList<String>()
    private var spinnerArrayEmpresa = ArrayList<String>()

    private var marca = Marca()
    private var modelo = Modelo()
    private var cor = Cor()
    private var empresa = Empresa()

    private var idMarcaSel: Number = 0

    // private var firestoreDB = FirebaseFirestore.getInstance()
    private var docMarca = FirebaseFirestore.getInstance().collection("marca_veiculo")
    private var docModelo = FirebaseFirestore.getInstance().collection("modelo_veiculo")
    private var docCor = FirebaseFirestore.getInstance().collection("cor_veiculo")
    private var docEmpresa = FirebaseFirestore.getInstance().collection("empresa")
    private lateinit var cmbMarca: Spinner

    @RequiresApi(Build.VERSION_CODES.O)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consulta)

        val btnFiltrar = findViewById<Button>(R.id.btnFiltrar)
        cmbMarca = findViewById<Spinner>(R.id.cmbMarca)
        val cmbModelo = findViewById<Spinner>(R.id.cmbModelo)
        val cmbCor = findViewById<Spinner>(R.id.cmbCor)
        val cmbFilial = findViewById<Spinner>(R.id.cmbFilial)
        val dtaAno = findViewById<EditText>(R.id.dtaAno)
        val etnKm = findViewById<EditText>(R.id.etnKm)

        //*****carregando a combo marca*****//

        /* solução final, só não habilitei pois a combo carrega, mas ao selecionar um item não aparece

        spinnerArrayMarca = functions.carregarMarca()
        var adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerArrayMarca)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapter.notifyDataSetChanged()
        cmbMarca.adapter = adapter  */

        docMarca.get().addOnCompleteListener(OnCompleteListener {

            for (dataObject in it.getResult()!!.documents) {
                marca = dataObject.toObject(Marca::class.java)!!
                // marca.idmarca = dataObject.id
                Log.i("Consulta", marca.descricao)
                // Log.i("Consulta", marca.idmarca)
                //   Log.i("Consulta", marca.cod_marca as String)

                spinnerArrayMarca.add(marca.descricao)

            }

            val adapterMarca =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerArrayMarca)
            cmbMarca.adapter = adapterMarca


        })
            .addOnFailureListener { e -> Log.e("Carga Combo Marca", "onFailure()", e) }


        //*****Fim carregando a combo marca****//


        //*****carregando a combo cores*****//
        docCor.get().addOnCompleteListener(OnCompleteListener {

            for (dataObject in it.getResult()!!.documents) {
                cor = dataObject.toObject(Cor::class.java)!!
                Log.i("Consulta", cor.descricao)
                Log.i("Consulta", cor.cod_cor.toString())

                spinnerArrayCor.add(cor.descricao)

            }

            val adapterCor =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerArrayCor)
            cmbCor.adapter = adapterCor


        })
            .addOnFailureListener { e -> Log.e("Carga Combo Marca", "onFailure()", e) }
        //*****Fim carregando a combo cores****//


        //*****carregando a combo Filiais*****//
        docEmpresa.get().addOnCompleteListener(OnCompleteListener {

            for (dataObject in it.getResult()!!.documents) {
                empresa = dataObject.toObject(Empresa::class.java)!!
                Log.i("Consulta", empresa.razao_social)
                Log.i("Consulta", empresa.cod_empresa.toString())

                spinnerArrayEmpresa.add(
                    empresa.cod_empresa.toString() + ";" + empresa.cidade +
                            ";" + empresa.estado + ";" + empresa.razao_social
                )

            }

            val adapterEmpresa =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerArrayEmpresa)
            cmbFilial.adapter = adapterEmpresa


        })
            .addOnFailureListener { e -> Log.e("Carga Combo Marca", "onFailure()", e) }
        //*****Fim carregando a combo Filiais****//


        //Responsável por montar a lista de modelos vinculados a marca selecionada

        fun carregarModelo(idMarcaSel: Number): SpinnerAdapter {

            spinnerArrayModelo.clear()

            docModelo.whereEqualTo("cod_marca", idMarcaSel).get()

                .addOnCompleteListener(OnCompleteListener {

                    for (dataObject in it.getResult()!!.documents) {
                        modelo = dataObject.toObject(Modelo::class.java)!!
                        Log.i("Consulta Modelos", modelo.descricao)
                        spinnerArrayModelo.add(modelo.descricao)
                    }

                })
                .addOnFailureListener { e -> Log.e("Combo MOdelo", "onFailure()", e) }

            val adapterModelo = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                spinnerArrayModelo
            )
            adapterModelo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            adapterModelo.notifyDataSetChanged()
            return adapterModelo

        }


        //Obtendo o id do documento selecionado na combo Marcas, para popular a combo Modelo

        cmbMarca.onItemSelectedListener = object :

            AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val marcaSelected = spinnerArrayMarca[p2]

                Log.i("Marca Selecionada", spinnerArrayMarca[p2])

                val docRef = docMarca.whereEqualTo("descricao", marcaSelected)

                docRef.get().addOnCompleteListener(OnCompleteListener {

                    for (dataObject in it.getResult()!!.documents) {
                        marca = dataObject.toObject(Marca::class.java)!!
                        idMarcaSel = marca.codmarca
                        Log.i("Código da Marca", idMarcaSel.toString())

                    }

                    val adapter = carregarModelo(idMarcaSel)
                    cmbModelo.adapter = adapter

                })
                    .addOnFailureListener { e -> Log.e("onItemSelected", "onFailure()", e) }


            }

        }//Fim Obtendo o id do documento selecionado na combo Marcas


        fun validaFiltro(): String {

            var msg: String = ""

            //problema: não está selecionando os itens na combo modelo, está ficando sempre como nulo
            // if (cmbMarca.selectedItem == null || cmbModelo.selectedItem == null) {
            if (cmbMarca.selectedItem == null) {
                msg = "Marca/Modelo do Veículo não Selecionados"
            }
            return msg
        }

        // Aplicação filtro de pesquisa
        btnFiltrar.setOnClickListener {

            /*var msg = validaFiltro()

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
                var nomeMarca: Int = -1;
                var nomeModelo: Int = -1;
                var cor: Int = -1;
                var filial: Int = -1;
                var anoModelo: Int = -1;
                var km: Long = -1;

                if (cmbMarca.selectedItem != null) {
                    nomeMarca = cmbMarca.selectedItem.toString().toInt()
                }
                if (cmbModelo.selectedItem != null) {
                    nomeModelo = cmbModelo.selectedItem.toString().toInt()
                }
                if (cmbCor.selectedItem != null) {
                    cor = cmbCor.selectedItem.toString().toInt()
                }
                if (cmbFilial.selectedItem != null) {
                    filial = cmbFilial.selectedItem.toString().toInt()
                }

                var checkAno = dtaAno.text
                if (!checkAno.isNullOrEmpty()) {
                    anoModelo = dtaAno.text.toString().toInt()
                }

                var checkKm = etnKm.text
                if (!checkKm.isNullOrEmpty()) {
                    km = etnKm.text.toString().toLong()
                }*/

                var intent = Intent(this, Lista::class.java)
                intent.putExtra("marca", 1)
                intent.putExtra("modelo", 1)
                intent.putExtra("cor", 1)
                intent.putExtra("filial", 1)
                intent.putExtra("ano", 1)
                intent.putExtra("km", 1.0)

                //var veiculo = Veiculo(0, 0, 0, 0, 0, 0.00, 2020, 0, "0", "0");

                //var retorno = veiculoRepository.GetVeiculo((veiculo));
                //intent.putExtra("marca", retorno);
                startActivity(intent)
            }
        }
    } // fim do on create
/*}*/


