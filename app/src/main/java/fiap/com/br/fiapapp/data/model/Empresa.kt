package fiap.com.br.fiapapp.data.model

data class Empresa (

     var cod_empresa: Int = 0,
     var cnpj: Long = 0,
     var estado: String = "",
     var cidade: String = "",
     var razao_social: String = ""
)