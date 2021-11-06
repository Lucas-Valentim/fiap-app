package fiap.com.br.fiapapp.presenter.interfaces

import com.google.firebase.firestore.Query
import fiap.com.br.fiapapp.model.Cor

interface CorContrato {


    interface ListaCorView{

        fun demonstraCores(cores: ArrayList<String>)
        fun demonstrarCorSelecionada (codCor: Int, descricao: String)
        fun demonstrarMsgErro(msg: String)

    }

    interface ListaCorPresenter{
        fun obtemCor()
        fun destruirView()
        fun obterCorSelecionada(descricao: String)
        fun obterCodDescrCor(cor: Cor): Query
    }
}