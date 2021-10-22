package fiap.com.br.fiapapp.business

import android.R
import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Spinner
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import fiap.com.br.fiapapp.data.*
import java.util.*
import kotlin.collections.ArrayList
import android.widget.*
import android.widget.ArrayAdapter as ArrayAdapter

class Functions() {

    private lateinit var db: CollectionReference
    private var spinnerArrayList = ArrayList<String>()
    private  lateinit var  adapter: ArrayAdapter<String>



   public fun carregarMarca(): ArrayList<String>{
        //*****carregando a combo marca*****//

        db = FirebaseFirestore.getInstance().collection("marca_veiculo")
        db.get().addOnCompleteListener(OnCompleteListener {

            for (dataObject in it.getResult()!!.documents){
                var marca = dataObject.toObject(Marca::class.java)!!
                 Log.i("Carga Combo Marca", marca.descricao)
                spinnerArrayList.add(marca.descricao)

            }

        })
            .addOnFailureListener { e -> Log.e("Carga Combo Marca", "onFailure()", e) }
        //*****Fim carregando a combo marca****//

       return spinnerArrayList
    }


}