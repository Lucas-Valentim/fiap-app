package fiap.com.br.fiapapp.presenter.interfaces

import android.view.View
import com.google.firebase.firestore.Query
import fiap.com.br.fiapapp.model.Empresa
import fiap.com.br.fiapapp.model.Veiculo

interface VeiculoContrato {

    interface VeiculoView{

        fun demonstrarListaVeiculos(veiculos: ArrayList<Veiculo>)
        fun demonstrarVeiculo (veiculo: Veiculo)
        fun demonstrarMsgErro(msg: String)
        fun demonstrarMsgSucesso(msg: String)
        fun carregarVeiculos(veiculos: ArrayList<Veiculo>)

    }

    interface VeiculoPresenter{
        //Lista todos os veículos cadastrados
        fun obterListaVeiculos()

        //Lista um veículo pela chave
        fun obterVeiculo(idDoc: String)

        //Lista os Veículo dados um modelo/marca
        fun obterListaVeiculosModelo(codModelo: Int)

        //cadastra um veículo
        fun cadastrarVeiculo(codModelo: Int, codCor: Int, cnpjEmpresa: Long, km: Int, valor: Double, placa: String, detalhes: String)

        //altera um veiculo
        fun alterarVeiculo(idDoc: String, codModelo: Int, codCor: Int, cnpjEmpresa: Long, km: Int, valor: Double, placa: String, detalhes: String)

        //efetua a execução de um veículo dado uma chave (idoc do Firestore)
        fun excluirVeiculo(idDoc: String)

        fun destruirView()
    }
}