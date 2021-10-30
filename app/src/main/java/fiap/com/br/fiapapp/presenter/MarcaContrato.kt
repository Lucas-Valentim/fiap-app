package fiap.com.br.fiapapp.presenter

import android.content.Intent

interface MarcaContrato {


    interface ListaMarcaView{
        fun demonstraMarcas(marcas: ArrayList<String>)
        fun demonstrarMarcaSelecionada (codMarca: Int, descricao: String)
        fun demonstrarMsgErro(msg: String)

    }

    interface ListaMarcaPresenter{
         fun obtemMarca()
         fun destruirView()
         fun obterMarcaSelecionada(descricao: String)
    }
}