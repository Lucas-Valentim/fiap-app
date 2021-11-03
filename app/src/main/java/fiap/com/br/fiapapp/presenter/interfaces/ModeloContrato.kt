package fiap.com.br.fiapapp.presenter.interfaces

import com.google.firebase.firestore.Query
import fiap.com.br.fiapapp.model.Marca
import fiap.com.br.fiapapp.model.Modelo

interface ModeloContrato {


    interface ListaModeloView{
        fun demonstraModelos(modelos: ArrayList<String>)
        fun demonstrarModeloSelecionado (codModelo: Int, descricao: String)
        fun demonstrarMsgErro(msg: String)

    }

    interface ListaModeloPresenter{
        fun obtemModelo(codMarca: Int?)
        fun obtemModeloPorNome(Nome: String) : Int?
        fun obterModeloSelecionado(descricao: String)
        fun obterCodDescrModelo(modelo: Modelo): Query
        fun destruirView()
    }
}