package fiap.com.br.fiapapp.presenter

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.*
import fiap.com.br.fiapapp.model.*
import fiap.com.br.fiapapp.presenter.interfaces.ModeloContrato
import fiap.com.br.fiapapp.presenter.interfaces.VeiculoContrato
import java.util.*
import kotlin.collections.ArrayList

class VeiculoPresenter: VeiculoContrato.VeiculoPresenter {


    private var view: VeiculoContrato.VeiculoView?
    private lateinit var db: CollectionReference
    private lateinit var docRef: DocumentReference
    private final val ANO_INICIO = 1900

    constructor(view: VeiculoContrato.VeiculoView){
        this.view = view
    }

    override fun obterListaVeiculos() {
        var veiculos: ArrayList<Veiculo> = ArrayList()
        db = FirebaseFirestore.getInstance().collection("veiculo");
        db.get()
            .addOnSuccessListener { document ->
                for (doc in document) {
                    var veic = doc.toObject(Veiculo::class.java);
                    veic.id = doc.id;
                    veiculos.add(veic)
                }
                view?.carregarVeiculos(veiculos)
            }
            .addOnFailureListener { _ ->
            }
    }

    override fun obterVeiculo(idDoc: String) {

        var veiculo = Veiculo()
        var modelo = Modelo()
        var marca = Marca()
        var cor = Cor()
        var empresa = Empresa()

        db = FirebaseFirestore.getInstance().collection("veiculo")
        docRef = db.document(idDoc)

        docRef.get()
              .addOnSuccessListener { documents ->
              veiculo.placa = documents.data?.get("placa").toString()
              var valorVeic = documents.data?.get("valor").toString()
              veiculo.valor = valorVeic.toDouble()
               var codMod =   documents.data?.get("cod_modelo").toString()
              veiculo.cod_modelo = codMod.toInt()
              var codCorVeic =  documents.data?.get("cod_cor").toString()
              veiculo.cod_cor = codCorVeic.toInt()
              var cnpjEmpVeic =  documents.data?.get("cnpj").toString()
              veiculo.cnpj = cnpjEmpVeic.toLong()
              veiculo.detalhes = documents.data?.get("detalhes") as String
              var anoVeic =  documents.data?.get("ano").toString()
              veiculo.ano = anoVeic.toInt()
              var kmVeic =  documents.data?.get("km").toString()
              veiculo.km = kmVeic.toInt()

              view?.demonstrarVeiculo(veiculo)

                var dbModelo = FirebaseFirestore.getInstance().collection("modelo_veiculo");
                dbModelo.whereEqualTo("cod_modelo", veiculo.cod_modelo).get()
                    .addOnSuccessListener { document ->

                        for (doc in document){
                            modelo = doc.toObject(Modelo::class.java)

                        }

                        view?.demonstrarModeloVeiculo(modelo)

                        var dbMarca = FirebaseFirestore.getInstance().collection("marca_veiculo");
                        dbMarca.whereEqualTo("codmarca", modelo.cod_marca).get()
                            .addOnSuccessListener { documentMarca ->

                                for (doc in documentMarca){
                                    marca = doc.toObject(Marca::class.java)
                               }

                                view?.demonstrarMarcaVeiculo(marca)


                                var dbCor = FirebaseFirestore.getInstance().collection("cor_veiculo");
                                dbCor.whereEqualTo("cod_cor", veiculo.cod_cor).get()
                                    .addOnSuccessListener { documentCor ->

                                        for (doc in documentCor){
                                            cor = doc.toObject(Cor::class.java)

                                        }

                                        view?.demonstrarCorVeiculo(cor)

                                        var dbFilial = FirebaseFirestore.getInstance().collection("empresa");
                                        dbFilial.whereEqualTo("cnpj", veiculo.cnpj).get()
                                            .addOnSuccessListener { documentEmpresa ->

                                                for (doc in documentEmpresa){
                                                    empresa = doc.toObject(Empresa::class.java)

                                                }

                                                view?.demonstrarFilialVeiculo(empresa)

                                            } //quarto
                                    } // terceiro

                            } // segundo


                    } //primeiro


                } // aqui
    }

    override fun alterarVeiculo(
        idDoc: String,
        descrModelo: String,
        descrCor: String,
        razaoSocial: String,
        km: Int,
        valor: Double,
        placa: String,
        detalhes: String,
        ano: Int
    ) {

        var modelo = Modelo()
        var cor = Cor()
        var empresa = Empresa()
        var veiculo = Veiculo()

        veiculo.km = km
        veiculo.valor = valor
        veiculo.placa = placa
        veiculo.detalhes = detalhes
        veiculo.ano = ano


        var dbModelo = FirebaseFirestore.getInstance().collection("modelo_veiculo");
        dbModelo.whereEqualTo("descricao", descrModelo).get()
            .addOnSuccessListener { document ->

                for (doc in document){
                    modelo = doc.toObject(Modelo::class.java)

                    veiculo.cod_modelo = modelo.cod_modelo
                    veiculo.cod_marca = modelo.cod_marca
                }

                        var dbCor = FirebaseFirestore.getInstance().collection("cor_veiculo");
                        dbCor.whereEqualTo("descricao", descrCor).get()
                            .addOnSuccessListener { documentCor ->

                                for (doc in documentCor){
                                    cor = doc.toObject(Cor::class.java)

                                    veiculo.cod_cor = cor.cod_cor
                                }

                                var dbEmpresa = FirebaseFirestore.getInstance().collection("empresa");
                                dbEmpresa.whereEqualTo("razao_social", razaoSocial).get()
                                    .addOnSuccessListener { documentEmpresa ->

                                        for (doc in documentEmpresa) {
                                            empresa = doc.toObject(Empresa::class.java)

                                            veiculo.cnpj = empresa.cnpj
                                        }

                                        docRef = FirebaseFirestore.getInstance().collection("veiculo").document(idDoc)
                                        docRef.set(veiculo)
                                            .addOnSuccessListener {
                                                view?.demonstrarMsgSucesso("Alteração Realizada com Sucesso!")

                                            } // quarto

                                    } //terceiro

                            } // segundo

            } //primeiro


    }

    override fun cadastrarVeiculo(
        descrMarca: String,
        descrModelo: String,
        descrCor: String,
        razaoSocial: String,
        km: Int,
        valor: Double,
        placa: String,
        detalhes: String,
        ano: Int

    ) {


        var veiculo = Veiculo()
        var modelo = Modelo()
        var marca = Marca()
        var cor = Cor()
        var empresa = Empresa()
        veiculo.km = km
        veiculo.valor = valor
        veiculo.placa = placa
        veiculo.detalhes = detalhes
        veiculo.ano = ano


        var dbModelo = FirebaseFirestore.getInstance().collection("modelo_veiculo");
        dbModelo.whereEqualTo("descricao", descrModelo).get()
            .addOnSuccessListener { document ->

                for (doc in document){
                    modelo = doc.toObject(Modelo::class.java)

                    veiculo.cod_modelo = modelo.cod_modelo

                }

                var dbMarca = FirebaseFirestore.getInstance().collection("marca_veiculo");
                dbMarca.whereEqualTo("descricao", descrMarca).get()
                    .addOnSuccessListener { documentMarca ->

                        for (doc in documentMarca){
                            marca = doc.toObject(Marca::class.java)
                            veiculo.cod_marca = marca.codmarca
                        }

                        var dbCor = FirebaseFirestore.getInstance().collection("cor_veiculo");
                        dbCor.whereEqualTo("descricao", descrCor).get()
                            .addOnSuccessListener { documentCor ->

                                for (doc in documentCor){
                                    cor = doc.toObject(Cor::class.java)
                                    veiculo.cod_cor = cor.cod_cor

                                }

                                var dbFilial = FirebaseFirestore.getInstance().collection("empresa");
                                dbFilial.whereEqualTo("razao_social", razaoSocial).get()
                                    .addOnSuccessListener { documentEmpresa ->

                                        for (doc in documentEmpresa){
                                            empresa = doc.toObject(Empresa::class.java)
                                            veiculo.cnpj = empresa.cnpj

                                        }

                                        db = FirebaseFirestore.getInstance().collection("veiculo")


                                        db.add(veiculo)
                                            .addOnSuccessListener { _ ->
                                                view?.demonstrarMsgSucesso("Veiculo Cadastrado com sucesso!")
                                            }

                                    } //quarto
                            } // terceiro

                    } // segundo


            } //primeiro
    }


    override fun excluirVeiculo(idDoc: String) {
        docRef = FirebaseFirestore.getInstance().collection("veiculo").document(idDoc)
        docRef.delete()
            .addOnSuccessListener {
                view?.demonstrarMsgSucesso("Exclusão Realizada com Sucesso!")
            }
            .addOnFailureListener { _ ->
                view?.demonstrarMsgErro("Erro na Exclusão do Veículo!")
            }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun validaDadosVeiculo(
        marca: String,
        modelo: String,
        cor: String,
        razaoSocial: String,
        ano: String,
        km: String,
        valor: String,
        placa: String
    ): String {
        var msg: String = ""

        if (marca.isNullOrEmpty() || marca == "Selecione") {
            msg = "Marca do Veículo não Selecionados"

            return msg
        }
        if (modelo.isNullOrEmpty() || modelo == "Selecione") {
            msg = "Modelo do Veículo não Selecionados"

            return msg
        }

        if (cor.isNullOrEmpty()  || cor == "Selecione") {
            msg = "Cor do Veículo não Selecionados"

            return msg
        }

        if (razaoSocial.isNullOrEmpty()  || razaoSocial == "Selecione") {
            msg = "Filial não Selecionada"

            return msg
        }

        if (ano.isNullOrEmpty()) {
            msg = "Ano não informado"

            return msg
        }

        if (km.isNullOrEmpty()) {
            msg = "KM não informado"

            return msg
        }

        if (placa.isNullOrEmpty()) {
            msg = "Placa não informada"

            return msg
        }

        if (valor.isNullOrEmpty()) {
            msg = "Valor não informado"

            return msg
        } else {

            var aux = valor.toDouble()

            if (aux <= 0) {
                msg = "Valor menor ou igual a 0"

                return msg
            }

        }

        var formatData = SimpleDateFormat("dd/MM/yyyy" )
        var dataAtual = formatData.format(Date())
        var anoAtual = (dataAtual.substring(6)).toInt()

        var anoVeiculo = ano.toInt()

        if (anoVeiculo < ANO_INICIO || anoVeiculo > anoAtual ){

            msg = "Ano informado inválido"

        }


        return msg

    }

    override fun destruirView() {
        this.view = null
    }

}