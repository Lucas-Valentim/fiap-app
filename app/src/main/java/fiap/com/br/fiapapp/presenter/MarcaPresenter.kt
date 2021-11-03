package fiap.com.br.fiapapp.presenter

import android.content.Intent
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import fiap.com.br.fiapapp.model.*
import fiap.com.br.fiapapp.presenter.interfaces.MarcaContrato
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
        spinnerArrayList.add("Selecione");
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

    override fun obtemMarcaPorNome(nome: String) : Int? {

        var idMarca = -1;
        db = FirebaseFirestore.getInstance().collection("marca_veiculo")
        val doc = db.whereEqualTo("descricao", nome).get();
        var complete = doc.isComplete;
        do{
            complete = doc.isComplete;
        }while (!complete)

        val result = doc.getResult();
        val obj = result?.toObjects(Marca::class.java)?.first()
                /*.addOnSuccessListener { document ->
                    for (doc in document) {
                        var marca = doc.toObject(Marca::class.java);
                        idMarca = marca.codmarca;
                    }

                }*/

        return obj?.codmarca;
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

    override fun obterCodDescrMarca(marca: Marca): Query {
        db = FirebaseFirestore.getInstance().collection("marca_veiculo");

        var qry = db.whereGreaterThan("cod_marca", -1);

        if (marca.codmarca != null && marca.codmarca!! > 0) {
            qry = qry.whereEqualTo("ano", marca.codmarca);
        }

        if (marca.descricao != null && !marca.descricao.isNullOrEmpty()) {
            qry = qry.whereEqualTo("descricao", marca.descricao);
        }

        return qry

    }

    override fun destruirView() {
        this.view = null
    }



}