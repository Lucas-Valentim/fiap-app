package fiap.com.br.fiapapp.domain.usecases.veiculos

import com.google.firebase.firestore.Query
import fiap.com.br.fiapapp.data.model.Veiculo
import java.util.ArrayList

interface VeiculoContract {
    fun SaveNewVeiculo(veiculo: Veiculo);
    fun UpdateVeiculo(veiculo: Veiculo);
    //fun GetVeiculo(veiculo: Veiculo): ArrayList<Veiculo>;
    fun GetVeiculo(veiculo: Veiculo): Query;
}