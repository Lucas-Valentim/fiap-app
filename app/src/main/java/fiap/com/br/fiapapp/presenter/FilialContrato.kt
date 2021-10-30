package fiap.com.br.fiapapp.presenter

interface FilialContrato {


    interface ListaFilialView{
        fun demonstraFiliais(filiais: ArrayList<String>)
        fun demonstrarFilialSelecionada (codFilial: Int, razaoSocial: String)
        fun demonstrarMsgErro(msg: String)

    }

    interface ListaFilialPresenter{
        fun obtemFilial()
        fun destruirView()
        fun obterFilialSelecionada(descricao: String)
    }
}