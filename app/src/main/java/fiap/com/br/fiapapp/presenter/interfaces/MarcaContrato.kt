package fiap.com.br.fiapapp.presenter.interfaces

import android.content.Intent

interface MarcaContrato {


    interface ListaMarcaView{
        fun demonstraMarcas(marcas: ArrayList<String>)
        fun demonstrarMarcaSelecionada (codMarca: Int, descricao: String)
        fun demonstrarMsgErro(msg: String)

    }

    interface ListaMarcaPresenter{
         fun obtemMarca()
         fun obtemMarcaPorNome(nome: String) : Int?
         fun destruirView()
         fun obterMarcaSelecionada(descricao: String)
    }
}