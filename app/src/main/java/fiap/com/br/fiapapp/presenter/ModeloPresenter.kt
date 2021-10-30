package fiap.com.br.fiapapp.presenter

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import fiap.com.br.fiapapp.model.*
import kotlin.collections.ArrayList

class ModeloPresenter: ModeloContrato.ListaModeloPresenter {
//    var model: Marca, var view: Activity

    private var view: ModeloContrato.ListaModeloView?
    private lateinit var db: CollectionReference
    private var spinnerArrayList = ArrayList<String>()

    constructor(view: ModeloContrato.ListaModeloView){
        this.view = view
    }

     override fun obtemModelo(codMarca: Int) {
        spinnerArrayList.clear()
        db = FirebaseFirestore.getInstance().collection("modelo_veiculo")
        db.whereEqualTo("cod_marca", codMarca).get()

            .addOnCompleteListener(OnCompleteListener {

                for (dataObject in it.getResult()!!.documents){
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

    override fun destruirView() {
        this.view = null
    }



}