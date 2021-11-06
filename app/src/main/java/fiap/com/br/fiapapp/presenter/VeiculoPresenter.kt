package fiap.com.br.fiapapp.presenter

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.*
import fiap.com.br.fiapapp.model.*
import fiap.com.br.fiapapp.presenter.interfaces.VeiculoContrato
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

    override fun obterListaVeiculos() {
        var veiculos: ArrayList<Veiculo> = ArrayList()
        db = FirebaseFirestore.getInstance().collection("veiculo");
        db.get()
            .addOnSuccessListener { document ->
                for (doc in document) {
                    var veic = doc.toObject(Veiculo::class.java);
                    veic.id = doc.id;
                    veiculos.add(veic)
                }
                view?.carregarVeiculos(veiculos)
            }
            .addOnFailureListener { e ->
                Log.e("Carga Combo Filiais", "onFailure()", e)
            }
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
              veiculo.km = documents.data?.get("km") as Int
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
        km: Int,
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
        km: Int,
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