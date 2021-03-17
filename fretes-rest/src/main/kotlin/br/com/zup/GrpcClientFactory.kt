package br.com.zup

import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClientFactory {

    /**
     * Método executado toda vez que injetarmos o stub FretesServiceBlockingStub
     * podemos anotar o @Bean para isso. Ou usar o @Singleton, mudando o scopo
     */
    @Singleton
    fun fretesClientStub(@GrpcChannel("fretes") channel: ManagedChannel): FretesServiceGrpc.FretesServiceBlockingStub? {
        /*
        Não precisamos controlar esse objeto.
        Podemos definir no parâmetro e ele será injetado pelo micronaut.

        val channel: ManagedChannel = ManagedChannelBuilder
            .forAddress("localhost", 50051)
            .usePlaintext()
            .maxRetryAttempts(10)
            .build()*/
        return FretesServiceGrpc.newBlockingStub(channel)
    }
}