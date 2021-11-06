package fiap.com.br.fiapapp.model

import java.math.BigInteger

data class Veiculo (
    var id: String = "",
    var cod_modelo: Int? = -1,
    var cod_marca: Int? = -1,
    var cod_cor: Int? = -1,
    var cnpj: Long? = -1,
    var km: Int? = -1,
    var valor: Double? = -1.00,
    var ano: Int? = -1,
    var placa: String? = "",
    var detalhes: String? = ""
){
}