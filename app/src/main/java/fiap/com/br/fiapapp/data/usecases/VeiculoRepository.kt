package fiap.com.br.fiapapp.data.usecases

import android.content.ContentValues.TAG
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import fiap.com.br.fiapapp.data.model.Marca
import fiap.com.br.fiapapp.data.model.Veiculo
import fiap.com.br.fiapapp.domain.usecases.veiculos.VeiculoContract
import java.util.*

class VeiculoRepository : VeiculoContract {
    private val db = FirebaseFirestore.getInstance();

    public override fun SaveNewVeiculo(veiculo: Veiculo) {
        val docVeiculo = hashMapOf(
            "cod_veiculo" to veiculo.cod_veiculo,
            "cod_modelo" to veiculo.cod_modelo,
            "cod_cor" to veiculo.cod_cor,
            "cod_empresa" to veiculo.cod_empresa,
            "km" to veiculo.km,
            "valor" to veiculo.valor,
            "ano" to veiculo.ano,
            "filial" to veiculo.filial,
            "placa" to veiculo.placa,
            "detalhes" to veiculo.detalhes
        )
        db.collection("veiculo").document("veiculo").set(docVeiculo)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) };
    }

    public override fun UpdateVeiculo(veiculo: Veiculo) {
        val docVeiculo = hashMapOf(
            "cod_veiculo" to veiculo.cod_veiculo,
            "cod_modelo" to veiculo.cod_modelo,
            "cod_cor" to veiculo.cod_cor,
            "cod_empresa" to veiculo.cod_empresa,
            "km" to veiculo.km,
            "valor" to veiculo.valor,
            "ano" to veiculo.ano,
            "filial" to veiculo.filial,
            "placa" to veiculo.placa,
            "detalhes" to veiculo.detalhes
        )
        db.collection("veiculo").document("veiculo").set(docVeiculo)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) };
    }

    //public override fun GetVeiculo(veiculo: Veiculo): ArrayList<Veiculo> {
    public override fun GetVeiculo(veiculo: Veiculo): Query {
        var qry = db.collection("veiculo").whereGreaterThan("cod_veiculo", -1);

        if (veiculo.ano != null && veiculo.ano!! > 0) {
            qry = qry.whereEqualTo("ano", veiculo.ano);
        }
        if (veiculo.cod_cor != null && veiculo.cod_cor!! > 0) {
            qry = qry.whereEqualTo("cod_cor", veiculo.cod_cor);
        }
        if (veiculo.cod_empresa != null && veiculo.cod_empresa!! > 0) {
            qry = qry.whereEqualTo("cod_empresa", veiculo.cod_empresa);
        }
        if (veiculo.cod_modelo != null && veiculo.cod_modelo!! > 0) {
            qry = qry.whereEqualTo("cod_modelo", veiculo.cod_modelo);
        }
        if (veiculo.cod_marca != null && veiculo.cod_marca!! > 0) {
            qry = qry.whereEqualTo("cod_modelo", veiculo.cod_marca);
        }
        if (veiculo.placa != null && !veiculo.placa.isNullOrEmpty()) {
            qry = qry.whereEqualTo("placa", veiculo.placa);
        }
        if (veiculo.valor != null && veiculo.valor!! > 0.00) {
            qry = qry.whereEqualTo("valor", veiculo.valor);
        }
        if (veiculo.km != null && veiculo.km!! > 0) {
            qry = qry.whereEqualTo("km", veiculo.km);
        }

        //var result = ArrayList<Veiculo>();
        return qry;
        /*qry.get()
            .addOnSuccessListener { document ->
                for (doc in document) {
                    var veic = doc.toObject(Veiculo::class.java);
                    result.add(veic);
                }
            }
            .addOnFailureListener { document ->
                db.collection("veiculo").document("veiculo").get()
                    .addOnSuccessListener { document2 ->
                        var veic = document2.data?.forEach {
                            Log.d("AAAAAAAAAVeiculo", "DocumentSnapshot data: ${it}")
                            /*var veic = it.value.javaClass<Veiculo>();
                            result.add(veic);*/
                        }
                    }
            }*/

        //var temp = Veiculo(10, 15, 20, 25, 30, 30000, 0.00, 2021, 0, "ABC-1234", "Teste")
        //result.add(temp);
        //return result;
    }
}