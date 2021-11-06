package fiap.com.br.fiapapp.presenter.interfaces

import fiap.com.br.fiapapp.model.Empresa

interface FilialContrato {


    interface FilialView{
        fun demonstraRazaoSocial(filiais: ArrayList<String>)
        fun demonstrarMsgErro(msg: String)
        fun carregarFiliais(filiais: ArrayList<Empresa>)

    }

    interface FilialPresenter{
        fun obtemTodasFiliais()
        fun obtemRazaoSocial()
        fun destruirView()
    }
}