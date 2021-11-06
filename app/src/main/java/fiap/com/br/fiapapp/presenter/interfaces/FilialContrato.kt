package fiap.com.br.fiapapp.presenter.interfaces

import fiap.com.br.fiapapp.model.Empresa

interface FilialContrato {


    interface FilialView{
        fun demonstraFiliais(filiais: ArrayList<String>)
        fun demonstraRazaoSocial(filiais: ArrayList<String>)
        fun demonstrarFilialSelecionada (codFilial: Int, razaoSocial: String)
        fun demonstrarMsgErro(msg: String)
        fun carregarFiliais(modelos: ArrayList<Empresa>)

    }

    interface FilialPresenter{
        fun obtemFilial()
        fun obtemTodasFiliais()
        fun obtemRazaoSocial()
        fun obtemFilialPorNome(Nome : String) : Int?
        fun obtemNomeFilialPorCodigo(Codigo : Int?) : String?
        fun destruirView()
        fun obterFilialSelecionada(descricao: String)
    }
}