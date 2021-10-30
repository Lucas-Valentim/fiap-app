package fiap.com.br.fiapapp.model

data class Veiculo (
    var cod_veiculo: Int = 0,
    var cod_modelo: Int = 0,
    var cod_cor: Int = 0,
    var cnpj: Long = 0,
    var km: Long = 0,
    var valor: Double = 0.00,
    var placa: String = "",
    var detalhes: String = ""

)