package fiap.com.br.fiapapp.presenter

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.*
import fiap.com.br.fiapapp.model.*
import fiap.com.br.fiapapp.presenter.interfaces.CorContrato
import fiap.com.br.fiapapp.presenter.interfaces.ModeloContrato
import fiap.com.br.fiapapp.presenter.interfaces.VeiculoContrato
import java.util.*
import kotlin.collections.ArrayList

class VeiculoPresenter: VeiculoContrato.VeiculoPresenter {


    private var view: VeiculoContrato.VeiculoView?
    private lateinit var db: CollectionReference
    private lateinit var docRef: DocumentReference
    private var arrayList: ArrayList<Veiculo> = ArrayList()
    private final val ANO_INICIO = 1900
   // private var codigoCor: Int = 0
    private var codigoModelo: Int = 0
 //   private var cor: Cor = Cor()
   // var i = 0

    private lateinit var presenterModelo: ModeloContrato.ModeloPresenter

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
            .addOnFailureListener { e ->
                Log.e("Carga Combo Filiais", "onFailure()", e)
            }
    }

    override fun obterVeiculo(idDoc: String) {

        var veiculo: Veiculo = Veiculo()
        var modelo: Modelo = Modelo()
        var marca: Marca = Marca()
        var cor: Cor = Cor()
        var empresa: Empresa = Empresa()

        db = FirebaseFirestore.getInstance().collection("veiculo")
        docRef = db.document(idDoc)

        docRef.get()
              .addOnSuccessListener { documents ->
            //  veiculo.cod_veiaddOnSuccessListenerculo = documents.data?.get("cod_veiculo") as Int
              veiculo.placa = documents.data?.get("placa") as String
              var valorVeic = documents.data?.get("valor").toString()
              veiculo.valor = valorVeic.toDouble()
              Log.i("cod_modelo", documents.data?.get("cod_modelo").toString())
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
                            modelo = doc.toObject(Modelo::class.java)!!
                            Log.i("MOdelo Alteração", modelo.descricao.toString())

                        }

                        view?.demonstrarModeloVeiculo(modelo)

                        var dbMarca = FirebaseFirestore.getInstance().collection("marca_veiculo");
                        dbMarca.whereEqualTo("codmarca", modelo.cod_marca).get()
                            .addOnSuccessListener { document ->

                                for (doc in document){
                                    marca = doc.toObject(Marca::class.java)!!
                                    Log.i("Marca Alteração", marca.descricao.toString())
                               }

                                view?.demonstrarMarcaVeiculo(marca)


                                var dbCor = FirebaseFirestore.getInstance().collection("cor_veiculo");
                                dbCor.whereEqualTo("cod_cor", veiculo.cod_cor).get()
                                    .addOnSuccessListener { document ->

                                        for (doc in document){
                                            cor = doc.toObject(Cor::class.java)!!
                                            Log.i("Cor Alteração", cor.descricao.toString())

                                        }

                                        view?.demonstrarCorVeiculo(cor)

                                        var dbFilial = FirebaseFirestore.getInstance().collection("empresa");
                                        dbFilial.whereEqualTo("cnpj", veiculo.cnpj).get()
                                            .addOnSuccessListener { document ->

                                                for (doc in document){
                                                    empresa = doc.toObject(Empresa::class.java)!!
                                                    Log.i("Empresa Alteração", empresa.razao_social.toString())

                                                }

                                                view?.demonstrarFilialVeiculo(empresa)

                                            } //quarto
                                    } // terceiro

                            } // segundo


                    } //primeiro


                } // aqui
                /*
            .addOnFailureListener {
                    e -> Log.e("Consulta Veiculo", "onFailure()", e)

            } */
    }

    override fun obterListaVeiculosModelo(codModelo: Int) {
        var veiculo = Veiculo()
        var arrayVeiculo: ArrayList<Veiculo> = ArrayList()
        db = FirebaseFirestore.getInstance().collection("veiculo")
        val docRef = db.whereEqualTo("cod_modelo", codModelo)

        docRef.get().addOnCompleteListener(OnCompleteListener {

            for (dataObject in it.getResult()!!.documents) {
                veiculo = dataObject.toObject(Veiculo::class.java)!!

                Log.i("Valor BuscarVeiculo 1 ", veiculo.valor.toString())

                arrayVeiculo.add(veiculo)

                Log.i("Valor BuscarVeiculo 2", veiculo.valor.toString())

            }

            view?.demonstrarListaVeiculos(arrayVeiculo)

        })
            .addOnFailureListener { e -> Log.e("onItemSelected", "onFailure()", e) }

    }

    /*
    override fun cadastrarVeiculo(
        codModelo: Int,
        codCor: Int,
        cnpjEmpresa: Long,
        km: Int,
        valor: Double,
        placa: String,
        detalhes: String,
        ano: Int

    ) {

        //obterCodCor(descrCor)
        var veiculo: Veiculo = Veiculo()
        veiculo.cod_cor = codCor
        veiculo.cnpj = cnpjEmpresa
        veiculo.cod_modelo = codModelo
        veiculo.km = km
        veiculo.valor = valor
        veiculo.placa = placa
        veiculo.detalhes = detalhes
        veiculo.ano = ano

        db = FirebaseFirestore.getInstance().collection("veiculo")


        db.add(veiculo)
            .addOnSuccessListener { documentReference ->
            view?.demonstrarMsgSucesso("Veiculo Cadastrado com sucesso!")
            Log.i("cadastro veiculo", "documento gerado: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                view?.demonstrarMsgErro("Erro na gravação do Veículo!")
                Log.w("cadastro veiculo", "Erro na gravação", e)
            }
    } */

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

        var modelo: Modelo = Modelo()
        var marca: Marca = Marca()
        var cor: Cor = Cor()
        var empresa: Empresa = Empresa()
        var veiculo: Veiculo = Veiculo()

        veiculo.km = km
        veiculo.valor = valor
        veiculo.placa = placa
        veiculo.detalhes = detalhes
        veiculo.ano = ano


        var dbModelo = FirebaseFirestore.getInstance().collection("modelo_veiculo");
        dbModelo.whereEqualTo("descricao", descrModelo).get()
            .addOnSuccessListener { document ->

                for (doc in document){
                    modelo = doc.toObject(Modelo::class.java)!!
                    Log.i("MOdelo Alteração2", modelo.descricao.toString())

                    veiculo.cod_modelo = modelo.cod_modelo
                }

                        var dbCor = FirebaseFirestore.getInstance().collection("cor_veiculo");
                        dbCor.whereEqualTo("descricao", descrCor).get()
                            .addOnSuccessListener { document ->

                                for (doc in document){
                                    cor = doc.toObject(Cor::class.java)!!
                                    Log.i("Cor Alteração", cor.descricao.toString())

                                    veiculo.cod_cor = cor.cod_cor
                                }

                                var dbEmpresa = FirebaseFirestore.getInstance().collection("empresa");
                                dbEmpresa.whereEqualTo("razao_social", razaoSocial).get()
                                    .addOnSuccessListener { document ->

                                        for (doc in document) {
                                            empresa = doc.toObject(Empresa::class.java)!!
                                            Log.i(
                                                "Empresa Alterção", empresa.razao_social.toString()
                                            )

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


        var veiculo: Veiculo = Veiculo()
        var modelo: Modelo = Modelo()
        var marca: Marca = Marca()
        var cor: Cor = Cor()
        var empresa: Empresa = Empresa()
        //  veiculo.cod_cor = codCor
        //  veiculo.cnpj = cnpjEmpresa
        //  veiculo.cod_modelo = codModelo
        veiculo.km = km
        veiculo.valor = valor
        veiculo.placa = placa
        veiculo.detalhes = detalhes
        veiculo.ano = ano


        var dbModelo = FirebaseFirestore.getInstance().collection("modelo_veiculo");
        dbModelo.whereEqualTo("descricao", descrModelo).get()
            .addOnSuccessListener { document ->

                for (doc in document){
                    modelo = doc.toObject(Modelo::class.java)!!
                    Log.i("MOdelo Inclusao", modelo.descricao.toString())

                    veiculo.cod_modelo = modelo.cod_modelo

                }

                var dbMarca = FirebaseFirestore.getInstance().collection("marca_veiculo");
                dbMarca.whereEqualTo("descricao", descrMarca).get()
                    .addOnSuccessListener { document ->

                        for (doc in document){
                            marca = doc.toObject(Marca::class.java)!!
                            Log.i("Marca Inclusao", marca.descricao.toString())
                            veiculo.cod_marca = marca.codmarca
                        }

                        var dbCor = FirebaseFirestore.getInstance().collection("cor_veiculo");
                        dbCor.whereEqualTo("descricao", descrCor).get()
                            .addOnSuccessListener { document ->

                                for (doc in document){
                                    cor = doc.toObject(Cor::class.java)!!
                                    Log.i("Cor Inclusao", cor.descricao.toString())
                                    veiculo.cod_cor = cor.cod_cor

                                }

                                var dbFilial = FirebaseFirestore.getInstance().collection("empresa");
                                dbFilial.whereEqualTo("razao_social", razaoSocial).get()
                                    .addOnSuccessListener { document ->

                                        for (doc in document){
                                            empresa = doc.toObject(Empresa::class.java)!!
                                            Log.i("Empresa Inclusao", empresa.razao_social.toString())
                                            veiculo.cnpj = empresa.cnpj

                                        }

                                        db = FirebaseFirestore.getInstance().collection("veiculo")


                                        db.add(veiculo)
                                            .addOnSuccessListener { documentReference ->
                                                view?.demonstrarMsgSucesso("Veiculo Cadastrado com sucesso!")
                                                Log.i("cadastro veiculo", "documento gerado: ${documentReference.id}")
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
            .addOnFailureListener { e ->
                view?.demonstrarMsgErro("Erro na Exclusão do Veículo!")
                Log.w("excluir veiculo", "Erro na exclusão", e)
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

        var formatData: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy" )
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