package fiap.com.br.fiapapp.presenter

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import fiap.com.br.fiapapp.model.*
import fiap.com.br.fiapapp.presenter.interfaces.FilialContrato
import fiap.com.br.fiapapp.view.Lista
import kotlin.collections.ArrayList

class FilialPresenter: FilialContrato.FilialPresenter {

    private var view: FilialContrato.FilialView?
    private lateinit var db: CollectionReference
    private var spinnerArrayList = ArrayList<String>()

    constructor(view: FilialContrato.FilialView){
        this.view = view
    }

    override fun obtemFilial() {

        db = FirebaseFirestore.getInstance().collection("empresa")
        spinnerArrayList.add("Selecione")
        db.get().addOnCompleteListener(OnCompleteListener {

            for (dataObject in it.getResult()!!.documents){
                var filial = dataObject.toObject(Empresa::class.java)!!
                /*spinnerArrayList.add(filial.cnpj.toString() + "/" + filial.cidade +
                        "/" + filial.estado + "/" + filial.razao_social)*/
                spinnerArrayList.add(filial.cidade)
                Log.i("Carga Combo Filiais", filial.razao_social)
            }
            view?.demonstraFiliais(spinnerArrayList)
        })
            .addOnFailureListener { e ->
                Log.e("Carga Combo Filiais", "onFailure()", e)
                view?.demonstrarMsgErro("Erro ao carregar as Filiais")
            }

    }

    override fun obtemFilialPorNome(Nome : String) : Int? {
        db = FirebaseFirestore.getInstance().collection("empresa")
        val doc = db.whereEqualTo("cidade", Nome).get();
        var complete = doc.isComplete;
        do{
            complete = doc.isComplete;
        }while (!complete)

        val result = doc.getResult();
        val obj = result?.toObjects(Empresa::class.java)?.first()

        return obj?.cod_empresa;
    }

    override fun obtemNomeFilialPorCodigo(Codigo : Int?) : String?{
        db = FirebaseFirestore.getInstance().collection("empresa")
        val doc = db.whereEqualTo("cod_empresa", Codigo).get();
        var complete: Boolean;
        do{
            complete = doc.isComplete;
        }while (!complete)

        val result = doc.getResult();
        val obj = result?.toObjects(Empresa::class.java)
        val empresa: Empresa

        if(obj!!.count() > 0){
            empresa = obj.first()
            return empresa.razao_social
        }

        return ""
    }

    override fun obterFilialSelecionada(descricao: String) {

        var filial: Empresa = Empresa()
        db = FirebaseFirestore.getInstance().collection("empresa")
        spinnerArrayList.add("Selecione")
        db.whereEqualTo("descricao", descricao).get()

            .addOnCompleteListener(OnCompleteListener {

                for (dataObject in it.getResult()!!.documents){
                    filial = dataObject.toObject(Empresa::class.java)!!
                    Log.i("Consulta Codigo Filial", filial.cod_empresa.toString())

                    view?.demonstrarFilialSelecionada(filial.cod_empresa, filial.razao_social)

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