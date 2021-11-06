package fiap.com.br.fiapapp.presenter.interfaces

import android.view.View
import com.google.firebase.firestore.Query
import fiap.com.br.fiapapp.model.*

interface VeiculoContrato {

    interface VeiculoView{

        fun demonstrarListaVeiculos(veiculos: ArrayList<Veiculo>)
        fun demonstrarVeiculo (veiculo: Veiculo)
        fun demonstrarMsgErro(msg: String)
        fun demonstrarMsgSucesso(msg: String)
        fun demonstrarModeloVeiculo(modelo: Modelo)
        fun demonstrarMarcaVeiculo(marca: Marca)
        fun demonstrarCorVeiculo(cor: Cor)
        fun demonstrarFilialVeiculo (empresa: Empresa)

    }

    interface VeiculoPresenter{
        //Lista todos os veículos cadastrados
        fun obterListaVeiculos(veiculo: Veiculo): Query

        //Lista um veículo pela chave
        fun obterVeiculo(idDoc: String)

        //Lista os Veículo dados um modelo/marca
        fun obterListaVeiculosModelo(codModelo: Int)

        //cadastra um veículo
 //       fun cadastrarVeiculo(codModelo: Int, codCor: Int, cnpjEmpresa: Long, km: Int, valor: Double, placa: String, detalhes: String, ano: Int)
        fun cadastrarVeiculo(descrMarca: String, descrModelo: String, descrCor: String, razaoSocial: String, km: Int, valor: Double, placa: String, detalhes: String, ano: Int)

        //altera um veiculo
        fun alterarVeiculo(idDoc: String, descrModelo: String, descrCor: String, razaoSocial: String, km: Int, valor: Double, placa: String, detalhes: String, ano: Int)

        //efetua a execução de um veículo dado uma chave (idoc do Firestore)
        fun excluirVeiculo(idDoc: String)

        fun validaDadosVeiculo(marca: String, modelo: String, cor: String, razaoSocial: String, ano: String, km: String, valor: String,
                               placa: String): String

        fun destruirView()
    }
}