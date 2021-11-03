package fiap.com.br.fiapapp.presenter

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import fiap.com.br.fiapapp.model.*
import fiap.com.br.fiapapp.presenter.interfaces.CorContrato
import kotlin.collections.ArrayList

class CorPresenter: CorContrato.ListaCorPresenter {

    private var view: CorContrato.ListaCorView?
    private lateinit var db: CollectionReference
    private var spinnerArrayList = ArrayList<String>()

    constructor(view: CorContrato.ListaCorView){
        this.view = view
    }

    override fun obtemCor() {

        db = FirebaseFirestore.getInstance().collection("cor_veiculo")
        spinnerArrayList.add("Selecione")
        db.get().addOnCompleteListener(OnCompleteListener {

            for (dataObject in it.getResult()!!.documents){
                var cor = dataObject.toObject(Cor::class.java)!!
                spinnerArrayList.add(cor.descricao)
                Log.i("Carga Combo Marca", cor.descricao.toString())
            }
            view?.demonstraCores(spinnerArrayList)
        })
            .addOnFailureListener { e ->
                Log.e("Carga Combo Cores", "onFailure()", e)
                view?.demonstrarMsgErro("Erro ao carregas as cores disponíveis")
            }

    }

    override fun obtemCorPorNome(Nome : String) : Int? {
        var codCor = -1
        db = FirebaseFirestore.getInstance().collection("cor_veiculo")
        val doc = db.whereEqualTo("descricao", Nome).get();
        var complete = doc.isComplete;
        do{
            complete = doc.isComplete;
        }while (!complete)

        val result = doc.getResult();
        val obj = result?.toObjects(Cor::class.java)?.first()

        return obj?.cod_cor;
    }

    override fun obterCorSelecionada(descricao: String) {

        var cor: Cor = Cor()
        db = FirebaseFirestore.getInstance().collection("cor_veiculo")
        db.whereEqualTo("descricao", descricao).get()

            .addOnCompleteListener(OnCompleteListener {

                for (dataObject in it.getResult()!!.documents){
                    cor = dataObject.toObject(Cor::class.java)!!
                    Log.i("Consulta Codigo Cor", cor.cod_cor.toString())
                    view?.demonstrarCorSelecionada(cor.cod_cor, cor.descricao)

                }
            })
            .addOnFailureListener {
                    e -> Log.e("Combo Cor", "onFailure()", e)

            }



    }

    override fun destruirView() {
        this.view = null
    }



}