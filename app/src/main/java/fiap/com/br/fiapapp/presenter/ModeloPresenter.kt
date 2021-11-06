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
//    var model: Marca, var view: Activity

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
        var idModelo = -1
        db = FirebaseFirestore.getInstance().collection("modelo_veiculo")
        val doc = db.whereEqualTo("descricao", Nome).get();
        var complete = doc.isComplete;
        do{
            complete = doc.isComplete;
        }while (!complete)

        val result = doc.getResult();
        val obj = result?.toObjects(Modelo::class.java)?.first()

        return obj?.cod_modelo;
    }

    override fun obtemDescricaoModeloPorCodigo(Codigo: Int?) : String? {
        db = FirebaseFirestore.getInstance().collection("modelo_veiculo")
        val doc = db.whereEqualTo("cod_modelo", Codigo).get();
        var complete: Boolean;
        do{
            complete = doc.isComplete;
        }while (!complete)

        val result = doc.getResult();
        val obj = result?.toObjects(Modelo::class.java)
        val modelo: Modelo

        if(obj!!.count() > 0){
            modelo = obj.first()
            return modelo.descricao
        }

        return "";
    }

    override fun obterModeloSelecionado(descricao: String) {
        var modelo: Modelo = Modelo()
        db = FirebaseFirestore.getInstance().collection("modelo_veiculo")
        db.whereEqualTo("descricao", descricao).get()

            .addOnCompleteListener(OnCompleteListener {

                for (dataObject in it.getResult()!!.documents){
                    modelo = dataObject.toObject(Modelo::class.java)!!
                    Log.i("Consulta Codigo Modelo", modelo.cod_modelo.toString())
                    view?.demonstrarModeloSelecionado(modelo.cod_modelo, modelo.descricao)

                }
            })
            .addOnFailureListener {
                    e -> Log.e("Combo MOdelo", "onFailure()", e)

            }

    }

    override fun obterCodDescrModelo(modelo: Modelo): Query {
        db = FirebaseFirestore.getInstance().collection("modelo_veiculo");
        var qry: Query = db

        //var qry = db.whereGreaterThan("cod_modelo", -1);


        if ((modelo.cod_modelo == null || modelo.cod_modelo <=0) &&
            (modelo.descricao == null || modelo.descricao.isNullOrEmpty())  &&
            (modelo.ano == null || modelo.ano >= 0)   ) {
            qry = db.whereGreaterThan("cod_modelo", -1);
            qry = qry.orderBy("descricao")

        }
        if (modelo.cod_modelo != null && modelo.cod_modelo!! > 0) {
            qry = db.whereEqualTo("cod_modelo", modelo.cod_modelo)
          }

        if (modelo.descricao != null && !modelo.descricao.isNullOrEmpty()) {

            if (qry != null){qry = qry.whereEqualTo("descricao",  modelo.descricao);}
            else {qry = db.whereEqualTo("descricao", modelo.descricao)}
        }

        if (modelo.ano != null && modelo.ano > 0) {
            if (qry != null){
                qry = qry.whereEqualTo("ano",  modelo.ano);
            }
            else {
                qry = db.whereEqualTo("ano", modelo.ano)
                qry = qry.orderBy("descricao")
            }
        }
        return qry

    }

    override fun destruirView() {
        this.view = null
    }



}