package fiap.com.br.fiapapp.data.usecases

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.ArrayList
import android.widget.*
import fiap.com.br.fiapapp.data.model.Marca
import fiap.com.br.fiapapp.domain.usecases.combos.GetCombosDataContratact
import android.widget.ArrayAdapter as ArrayAdapter

class GetCombosData() : GetCombosDataContratact {

    private lateinit var db: CollectionReference
    private var spinnerArrayList = ArrayList<String>()
    private  lateinit var  adapter: ArrayAdapter<String>

   public override fun carregarMarca(): ArrayList<String>{
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