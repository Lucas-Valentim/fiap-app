package fiap.com.br.fiapapp.presenter

import android.content.Intent
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import fiap.com.br.fiapapp.model.*
import kotlin.collections.ArrayList

class MarcaPresenter: MarcaContrato.ListaMarcaPresenter {
//    var model: Marca, var view: Activity

    private var view: MarcaContrato.ListaMarcaView?
    private lateinit var db: CollectionReference
    private var spinnerArrayList = ArrayList<String>()

   constructor(view: MarcaContrato.ListaMarcaView){
        this.view = view
    }

    override fun obtemMarca() {

        db = FirebaseFirestore.getInstance().collection("marca_veiculo")
        db.get().addOnCompleteListener(OnCompleteListener {

            for (dataObject in it.getResult()!!.documents){
                var marca = dataObject.toObject(Marca::class.java)!!
                spinnerArrayList.add(marca.descricao)
                // marca.add(dataObject.toObject(Marca::class.java)!!)
                Log.i("Carga Combo Marca", marca.descricao.toString())
            }
            view?.demonstraMarcas(spinnerArrayList)
        })
            .addOnFailureListener { e ->
                Log.e("Carga Combo Marca", "onFailure()", e)
                view?.demonstrarMsgErro("Erro ao carregas as marcas disponíveis")
            }
    }

    override fun obterMarcaSelecionada(descricao: String) {

        var marca: Marca = Marca()
        db = FirebaseFirestore.getInstance().collection("marca_veiculo")
        db.whereEqualTo("descricao", descricao).get()

            .addOnCompleteListener(OnCompleteListener {

                for (dataObject in it.getResult()!!.documents){
                    marca = dataObject.toObject(Marca::class.java)!!
                    Log.i("Consulta Codigo Marca", marca.codmarca.toString())
                    view?.demonstrarMarcaSelecionada(marca.codmarca, marca.descricao)

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