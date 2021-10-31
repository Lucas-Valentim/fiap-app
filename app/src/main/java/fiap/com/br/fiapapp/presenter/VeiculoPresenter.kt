package fiap.com.br.fiapapp.presenter

import android.os.Binder
import android.util.Log
import android.view.View
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.*
import fiap.com.br.fiapapp.model.*
import kotlin.collections.ArrayList

class VeiculoPresenter: VeiculoContrato.VeiculoPresenter {


    private var view: VeiculoContrato.VeiculoView?
    private lateinit var db: CollectionReference
    private lateinit var docRef: DocumentReference
    private var arrayList: ArrayList<Veiculo> = ArrayList()
   // private var codigoCor: Int = 0
    private var codigoModelo: Int = 0
 //   private var cor: Cor = Cor()
   // var i = 0

    constructor(view: VeiculoContrato.VeiculoView){
        this.view = view
    }

    override fun obterListaVeiculos(veiculo: Veiculo): Query {

        db = FirebaseFirestore.getInstance().collection("veiculo");
        var qry = db.whereGreaterThan("cod_veiculo", -1);

        if (veiculo.ano != null && veiculo.ano!! > 0) {
            qry = qry.whereEqualTo("ano", veiculo.ano);
        }
        if (veiculo.cod_cor != null && veiculo.cod_cor!! > 0) {
            qry = qry.whereEqualTo("cod_cor", veiculo.cod_cor);
        }
        if (veiculo.cnpj != null && veiculo.cnpj!! > 0) {
            qry = qry.whereEqualTo("cnpj", veiculo.cnpj);
        }
        if (veiculo.cod_modelo != null && veiculo.cod_modelo!! > 0) {
            qry = qry.whereEqualTo("cod_modelo", veiculo.cod_modelo);
        }
        if (veiculo.cod_marca != null && veiculo.cod_marca!! > 0) {
            qry = qry.whereEqualTo("cod_modelo", veiculo.cod_marca);
        }
        if (veiculo.placa != null && !veiculo.placa.isNullOrEmpty()) {
            qry = qry.whereEqualTo("placa", veiculo.placa);
        }
        if (veiculo.valor != null && veiculo.valor!! > 0.00) {
            qry = qry.whereEqualTo("valor", veiculo.valor);
        }
        if (veiculo.km != null && veiculo.km!! > 0) {
            qry = qry.whereEqualTo("km", veiculo.km);
        }

        return qry;
    }

    override fun obterVeiculo(idDoc: String) {

        var veiculo: Veiculo = Veiculo()
        db = FirebaseFirestore.getInstance().collection("veiculo")
        docRef = db.document(idDoc)
        docRef.get()
            .addOnSuccessListener { documents ->
              veiculo.cod_veiculo = documents.data?.get("cod_veiculo") as Int
              veiculo.placa = documents.data?.get("placa") as String
              veiculo.valor = documents.data?.get("valor") as Double
              veiculo.km = documents.data?.get("km") as Long
              veiculo.cod_modelo = documents.data?.get("cod_modelo") as Int
              veiculo.cod_cor = documents.data?.get("cod_cor") as Int
              veiculo.cnpj = documents.data?.get("cnpj") as Long
              veiculo.detalhes = documents.data?.get("detalhes") as String
              view?.demonstrarVeiculo(veiculo)

                }
            .addOnFailureListener {
                    e -> Log.e("Consulta Veiculo", "onFailure()", e)

            }
    }

    override fun obterListaVeiculosModelo(codModelo: Int) {
        var veiculo = Veiculo()
        var arrayVeiculo: ArrayList<Veiculo> = ArrayList()
        db = FirebaseFirestore.getInstance().collection("veiculo")
        val docRef = db.whereEqualTo("cod_modelo", codModelo)

        docRef.get().addOnCompleteListener(OnCompleteListener {

            for (dataObject in it.getResult()!!.documents) {
                veiculo = dataObject.toObject(Veiculo::class.java)!!

                Log.i("Valor BuscarVeiculo 1 ", veiculo.valor.toString())

                arrayVeiculo.add(veiculo)

                Log.i("Valor BuscarVeiculo 2", veiculo.valor.toString())

            }

            view?.demonstrarListaVeiculos(arrayVeiculo)

        })
            .addOnFailureListener { e -> Log.e("onItemSelected", "onFailure()", e) }

    }

    override fun cadastrarVeiculo(
        codModelo: Int,
        codCor: Int,
        cnpjEmpresa: Long,
        km: Long,
        valor: Double,
        placa: String,
        detalhes: String

    ) {

        //obterCodCor(descrCor)
        var veiculo: Veiculo = Veiculo()
        veiculo.cod_cor = codCor
        veiculo.cnpj = cnpjEmpresa
        veiculo.cod_modelo = codModelo
        veiculo.km = km
        veiculo.valor = valor
        veiculo.placa = placa
        veiculo.detalhes = detalhes

        db = FirebaseFirestore.getInstance().collection("veiculo")

        db.add(veiculo)
            .addOnSuccessListener { documentReference ->
            view?.demonstrarMsgSucesso("Veiculo Cadastrado com sucesso!")
            Log.i("cadastro veiculo", "documento gerado: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                view?.demonstrarMsgErro("Erro na gravação do Veículo!")
                Log.w("cadastro veiculo", "Erro na gravação", e)
            }
    }

    override fun alterarVeiculo(
        idDoc: String,
        codModelo: Int,
        codCor: Int,
        cnpjEmpresa: Long,
        km: Long,
        valor: Double,
        placa: String,
        detalhes: String
    ) {
        var veiculo: Veiculo = Veiculo()
        veiculo.cod_cor = codCor
        veiculo.cnpj = cnpjEmpresa
        veiculo.cod_modelo = codModelo
        veiculo.km = km
        veiculo.valor = valor
        veiculo.placa = placa
        veiculo.detalhes = detalhes

        docRef = FirebaseFirestore.getInstance().collection("veiculo").document(idDoc)
        docRef.set(veiculo)
            .addOnSuccessListener {
            view?.demonstrarMsgSucesso("Alteração Realizada com Sucesso!")
            }
            .addOnFailureListener { e ->
                view?.demonstrarMsgErro("Erro na Alteração do Veículo!")
                Log.w("alterar veiculo", "Erro na alteração", e)
            }
    }

    override fun excluirVeiculo(idDoc: String) {
        docRef = FirebaseFirestore.getInstance().collection("veiculo").document(idDoc)
        docRef.delete()
            .addOnSuccessListener {
                view?.demonstrarMsgSucesso("Exclusão Realizada com Sucesso!")
            }
            .addOnFailureListener { e ->
                view?.demonstrarMsgErro("Erro na Exclusão do Veículo!")
                Log.w("excluir veiculo", "Erro na exclusão", e)
            }
    }

    override fun destruirView() {
        this.view = null
    }

}