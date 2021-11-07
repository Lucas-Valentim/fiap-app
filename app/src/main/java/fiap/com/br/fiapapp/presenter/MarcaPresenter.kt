package fiap.com.br.fiapapp.presenter

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import fiap.com.br.fiapapp.model.*
import fiap.com.br.fiapapp.presenter.interfaces.MarcaContrato
import kotlin.collections.ArrayList

class MarcaPresenter: MarcaContrato.ListaMarcaPresenter {

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
            }
            view?.demonstraMarcas(spinnerArrayList)
        })
            .addOnFailureListener { _ ->
                view?.demonstrarMsgErro("Erro ao carregas as marcas disponíveis")
            }
    }

    override fun obtemMarcaPorNome(nome: String) : Int? {

        db = FirebaseFirestore.getInstance().collection("marca_veiculo")
        val doc = db.whereEqualTo("descricao", nome).get();
        var complete: Boolean;
        do{
            complete = doc.isComplete;
        }while (!complete)

        val result = doc.getResult();
        val obj = result?.toObjects(Marca::class.java)?.first()

        return obj?.codmarca;
    }

    override fun obterMarcaSelecionada(descricao: String) {

        var marca = Marca()
        db = FirebaseFirestore.getInstance().collection("marca_veiculo")
        db.whereEqualTo("descricao", descricao).get()

            .addOnCompleteListener(OnCompleteListener {

                for (dataObject in it.getResult()!!.documents){
                    marca = dataObject.toObject(Marca::class.java)!!
                    view?.demonstrarMarcaSelecionada(marca.codmarca, marca.descricao)

                }
            })
            .addOnFailureListener {

            }
    }

    override fun destruirView() {
        this.view = null
    }
}