package fiap.com.br.fiapapp.presenter

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import fiap.com.br.fiapapp.model.*
import fiap.com.br.fiapapp.presenter.interfaces.FilialContrato
import kotlin.collections.ArrayList

class FilialPresenter: FilialContrato.FilialPresenter {

    private var view: FilialContrato.FilialView?
    private lateinit var db: CollectionReference
    private var spinnerArrayList = ArrayList<String>()

    constructor(view: FilialContrato.FilialView){
        this.view = view
    }

    override fun obtemRazaoSocial() {
        db = FirebaseFirestore.getInstance().collection("empresa")
        spinnerArrayList.add("Selecione")
        db.get().addOnCompleteListener(OnCompleteListener {

            for (dataObject in it.getResult()!!.documents){
                var filial = dataObject.toObject(Empresa::class.java)!!
                spinnerArrayList.add(filial.razao_social)
                Log.i("Carga Combo Filiais", filial.razao_social)
            }
            view?.demonstraRazaoSocial(spinnerArrayList)
        })
            .addOnFailureListener { e ->
                Log.e("Carga Combo Filiais", "onFailure()", e)
                view?.demonstrarMsgErro("Erro ao carregar as Filiais")
            }
    }

    override fun obtemTodasFiliais() {
        var filiais: ArrayList<Empresa> = ArrayList()
        db = FirebaseFirestore.getInstance().collection("empresa")
        db.get()
            .addOnSuccessListener { document ->
                for (doc in document) {
                    var filial = doc.toObject(Empresa::class.java);
                    filiais.add(filial)
                }
                view?.carregarFiliais(filiais)
            }
            .addOnFailureListener { e ->
                Log.e("Carga Combo Filiais", "onFailure()", e)
            }

    }

    override fun destruirView() {
        this.view = null
    }



}