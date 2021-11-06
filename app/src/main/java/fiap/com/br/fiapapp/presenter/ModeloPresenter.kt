package fiap.com.br.fiapapp.presenter

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import fiap.com.br.fiapapp.model.*
import fiap.com.br.fiapapp.presenter.interfaces.ModeloContrato
import kotlin.collections.ArrayList

class ModeloPresenter: ModeloContrato.ModeloPresenter {

    private var view: ModeloContrato.ModeloView?
    private lateinit var db: CollectionReference
    private var spinnerArrayList = ArrayList<String>()

    constructor(view: ModeloContrato.ModeloView){
        this.view = view
    }

     override fun obtemModelo(codMarca: Int?) {
        spinnerArrayList.clear()
        db = FirebaseFirestore.getInstance().collection("modelo_veiculo")
        spinnerArrayList.add("Selecione");
         if(codMarca != null) {
             db.whereEqualTo("cod_marca", codMarca).get()
                 .addOnCompleteListener(OnCompleteListener {

                     for (dataObject in it.getResult()!!.documents) {
                         var modelo = dataObject.toObject(Modelo::class.java)!!
                         Log.i("Consulta Modelos", modelo.descricao)
                         spinnerArrayList.add(modelo.descricao)
                         view?.demonstraModelos(spinnerArrayList)
                     }

                 })
                 .addOnFailureListener { e ->
                     Log.e("Combo MOdelo", "onFailure()", e)
                     view?.demonstrarMsgErro("Erro ao carregar os modelos disponíveis")
                 }
         }
         else{
             db.get()
                 .addOnCompleteListener(OnCompleteListener {

                     for (dataObject in it.getResult()!!.documents) {
                         var modelo = dataObject.toObject(Modelo::class.java)!!
                         Log.i("Consulta Modelos", modelo.descricao)
                         spinnerArrayList.add(modelo.descricao)
                         view?.demonstraModelos(spinnerArrayList)
                     }

                 })
                 .addOnFailureListener { e ->
                     Log.e("Combo MOdelo", "onFailure()", e)
                     view?.demonstrarMsgErro("Erro ao carregar os modelos disponíveis")
                 }
         }
    }

    override fun obtemModeloMarcaSel(codMarca: Int?, descrModeloSel: String) {
        spinnerArrayList.clear()
        db = FirebaseFirestore.getInstance().collection("modelo_veiculo")
        spinnerArrayList.add("Selecione");
        if(codMarca != null) {
            db.whereEqualTo("cod_marca", codMarca).get()
                .addOnCompleteListener(OnCompleteListener {

                    for (dataObject in it.getResult()!!.documents) {
                        var modelo = dataObject.toObject(Modelo::class.java)!!
                        Log.i("Consulta Modelos", modelo.descricao)
                        spinnerArrayList.add(modelo.descricao)

                    }
                    view?.demonstrarModelosMarcaSel(spinnerArrayList, descrModeloSel)

                })
                .addOnFailureListener { e ->
                    Log.e("Combo MOdelo", "onFailure()", e)
                    view?.demonstrarMsgErro("Erro ao carregar os modelos disponíveis")
                }
        }
        else{
            db.get()
                .addOnCompleteListener(OnCompleteListener {

                    for (dataObject in it.getResult()!!.documents) {
                        var modelo = dataObject.toObject(Modelo::class.java)!!
                        Log.i("Consulta Modelos", modelo.descricao)
                        spinnerArrayList.add(modelo.descricao)
                        view?.demonstrarModelosMarcaSel(spinnerArrayList, descrModeloSel)
                    }

                })
                .addOnFailureListener { e ->
                    Log.e("Combo MOdelo", "onFailure()", e)
                    view?.demonstrarMsgErro("Erro ao carregar os modelos disponíveis")
                }
        }
    }

    override fun obtemTodosModelos() {
        var modelos: ArrayList<Modelo> = ArrayList()
        db = FirebaseFirestore.getInstance().collection("modelo_veiculo")
        db.get()
            .addOnSuccessListener { document ->
                for (doc in document) {
                    var modelo = doc.toObject(Modelo::class.java);
                    modelos.add(modelo)
                }
                view?.carregarModelos(modelos)
            }
            .addOnFailureListener { e ->
                Log.e("Carga Combo Filiais", "onFailure()", e)
            }
    }

    override fun obtemModeloPorNome(Nome: String) : Int? {
        db = FirebaseFirestore.getInstance().collection("modelo_veiculo")
        val doc = db.whereEqualTo("descricao", Nome).get();
        var complete: Boolean;
        do{
            complete = doc.isComplete;
        }while (!complete)

        val result = doc.getResult();
        val obj = result?.toObjects(Modelo::class.java)?.first()

        return obj?.cod_modelo;
    }

    override fun destruirView() {
        this.view = null
    }



}