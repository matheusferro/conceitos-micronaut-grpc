package br.com.zup

import io.grpc.ManagedChannelBuilder

fun main() {

    val channel = ManagedChannelBuilder
        .forAddress("localhost", 50051)
        .usePlaintext() //Alterar para tls, https...
        .build()

    val client = FuncionarioServiceGrpc.newBlockingStub(channel)
    val request = FuncionarioRequest.newBuilder()
        .setNome("Matheus")
        .setCpf("00000000000")
        .setIdade(92)
        .setSalario(700.0)
        .setAtivo(true)
        .setCargo(Cargo.DEV)
        .addEndereco(Endereco.newBuilder()
            .setLogradouro("Rua do meio")
            .setCep("000000")
            .setComplemento("Bloco 7")
            .build()
        ).build()
    client.cadastrar(request)
}