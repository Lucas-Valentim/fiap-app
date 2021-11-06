package fiap.com.br.fiapapp.presenter.interfaces

interface FilialContrato {


    interface FilialView{
        fun demonstraFiliais(filiais: ArrayList<String>)
        fun demonstraRazaoSocial(filiais: ArrayList<String>)
        fun demonstrarFilialSelecionada (codFilial: Int, razaoSocial: String)
        fun demonstrarMsgErro(msg: String)

    }

    interface FilialPresenter{
        fun obtemFilial()
        fun obtemRazaoSocial()
        fun obtemFilialPorNome(Nome : String) : Int?
        fun obtemNomeFilialPorCodigo(Codigo : Int?) : String?
        fun destruirView()
        fun obterFilialSelecionada(descricao: String)
    }
}