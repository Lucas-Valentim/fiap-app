package fiap.com.br.fiapapp.presenter.interfaces

interface FilialContrato {


    interface ListaFilialView{
        fun demonstraFiliais(filiais: ArrayList<String>)
        fun demonstrarFilialSelecionada (codFilial: Int, razaoSocial: String)
        fun demonstrarMsgErro(msg: String)

    }

    interface ListaFilialPresenter{
        fun obtemFilial()
        fun obtemFilialPorNome(Nome : String) : Int?
        fun destruirView()
        fun obterFilialSelecionada(descricao: String)
    }
}