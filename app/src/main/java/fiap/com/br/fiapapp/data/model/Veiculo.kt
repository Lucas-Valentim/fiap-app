package fiap.com.br.fiapapp.data.model

import fiap.com.br.fiapapp.data.usecases.VeiculoRepository
import java.lang.reflect.Constructor
import java.util.*

data class Veiculo (

    var cod_veiculo: Int? = -1,
    var cod_modelo: Int? = -1,
    var cod_marca: Int? = -1,
    var cod_cor: Int? = -1,
    var cod_empresa: Int? = -1,
    var km: Long? = -1,
    var valor: Double? = -1.00,
    var ano: Int? = -1,
    var filial: Int? = -1,
    var placa: String? = "",
    var detalhes: String? = ""
){
    companion object Factory {
        fun create() : VeiculoRepository = VeiculoRepository()
        var COLLECTION = "veiculo"
    }
}