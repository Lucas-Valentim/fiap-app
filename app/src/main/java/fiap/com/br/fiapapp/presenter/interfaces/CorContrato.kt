package fiap.com.br.fiapapp.presenter.interfaces

interface CorContrato {


    interface ListaCorView{

        fun demonstraCores(cores: ArrayList<String>)
        fun demonstrarCorSelecionada (codCor: Int, descricao: String)
        fun demonstrarMsgErro(msg: String)

    }

    interface ListaCorPresenter{
        fun obtemCor()
        fun obtemCorPorNome(Nome : String) : Int?
        fun destruirView()
        fun obterCorSelecionada(descricao: String)
    }
}