package fiap.com.br.fiapapp.presenter

import fiap.com.br.fiapapp.model.Marca

interface ModeloContrato {


    interface ListaModeloView{
        fun demonstraModelos(modelos: ArrayList<String>)
        fun demonstrarModeloSelecionado (codModelo: Int, descricao: String)
        fun demonstrarMsgErro(msg: String)

    }

    interface ListaModeloPresenter{
        fun obtemModelo(codMarca: Int)
        fun obterModeloSelecionado(descricao: String)
        fun destruirView()
    }
}