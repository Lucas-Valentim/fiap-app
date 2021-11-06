package fiap.com.br.fiapapp.presenter.interfaces

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

interface LoginContrato {

    interface AutenticaView{
        fun demonstrarUsuarioLogado(user: FirebaseUser?)
        fun demonstrarMsgErro(msg: String)

    }


    interface AutenticaPresenter{
        fun AutenticarUsuario(email: String, senha: String)
        fun CadastrarUsuario(email: String, senha: String)
        fun destruirView()

    }
}