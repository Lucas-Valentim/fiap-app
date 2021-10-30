package fiap.com.br.fiapapp.presenter

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
    }
}