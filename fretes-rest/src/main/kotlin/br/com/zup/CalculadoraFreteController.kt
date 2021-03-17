package br.com.zup

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.protobuf.StatusProto
import io.grpc.protobuf.StatusProto.*
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.exceptions.HttpStatusException

@Controller("/api/frete")
class CalculadoraFreteController(val grpc: FretesServiceGrpc.FretesServiceBlockingStub) {

    @Get
    fun calcula(@QueryValue cep: String): FreteResponse{

        val request = CalculoFreteRequest
            .newBuilder()
            .setCep(cep)
            .build()

        try{
            val response = grpc.calculaFrete(request)

            return FreteResponse(cep, response.valor)
        }catch (e: StatusRuntimeException){
            val statusCode = e.status.code
            val description = e.status.description

            if(statusCode == Status.Code.INVALID_ARGUMENT){
                throw HttpStatusException(HttpStatus.BAD_REQUEST, description)
            }

            if(statusCode == Status.Code.PERMISSION_DENIED){
                val statusProto = fromThrowable(e)
                if(statusProto == null){
                    throw HttpStatusException(HttpStatus.FORBIDDEN, description)
                }

                val errorDetails = statusProto.detailsList.get(0).unpack(ErrorDetails::class.java)
                throw HttpStatusException(HttpStatus.FORBIDDEN, "${errorDetails.code}: ${errorDetails.message}")
            }

            throw  HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }

    }
}

data class FreteResponse(val cep: String, val valor: Double)