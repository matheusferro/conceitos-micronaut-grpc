package br.com.zup

import com.google.rpc.Code
import io.grpc.Status
import io.grpc.protobuf.StatusProto
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class FretesGrpcServer : FretesServiceGrpc.FretesServiceImplBase() {

    private val logger = LoggerFactory.getLogger(FretesGrpcServer::class.java)

    override fun calculaFrete(request: CalculoFreteRequest?, responseObserver: StreamObserver<CalculoFreteResponse>?) {

        logger.info("Calculando frete para: ${request}")

        val cep = request?.cep

        if(cep == null || cep.isBlank()){
            var excp = Status.INVALID_ARGUMENT
                .withDescription("cep deve ser informado")
                .asRuntimeException()

            responseObserver?.onError(excp)
        }

        if(!cep!!.matches("[0-9]{5}-[0-9]{3}".toRegex())){
            var excp = Status.INVALID_ARGUMENT
                .withDescription("cep inválido. ")
                .augmentDescription("O cep deve ser no formato 00000-000")
                .asRuntimeException()

            responseObserver?.onError(excp)
        }

        var valor = 0.0
        try{
            //lógica para definir o valor do frete
            valor = Random.nextDouble(from = 0.0, until = 140.0)

            if(valor > 100.0){
                throw IllegalStateException("Erro inesperado.")
            }
        }catch (e: Exception){
            val error = Status.INTERNAL
                .withDescription(e.message)
                .withCause(e)
                .asRuntimeException()
            responseObserver?.onError(error)
        }

        //SIMULAR UMA VERIFICAÇÃO DE SEGURANÇA
        if(cep.endsWith("666")){
            val statusProto = com.google.rpc.Status.newBuilder()
                .setCode(Code.PERMISSION_DENIED.number)
                .setMessage("Usuário não pode acessar esse recurso")
                .addDetails(
                    com.google.protobuf.Any.pack(ErrorDetails.newBuilder()
                    .setCode(401)
                    .setMessage("token expirado")
                    .build())
                )
                .build()
            val statusError = StatusProto.toStatusRuntimeException(statusProto)
            responseObserver?.onError(statusError)
        }

        val response = CalculoFreteResponse
            .newBuilder()
            .setCep(request.cep)
            .setValor(valor)
            .build()

        logger.info("Frete calculado: $response")

        responseObserver!!.onNext(response)
        responseObserver.onCompleted()
    }
}