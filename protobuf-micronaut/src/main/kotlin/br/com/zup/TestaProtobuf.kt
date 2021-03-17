package br.com.zup

import java.io.FileInputStream
import java.io.FileOutputStream

fun main() {
    val request = FuncionarioRequest.newBuilder()
        .setNome("Matheus")
        .setCpf("00000000000")
        .setSalario(700.0)
        .setIdade(37)
        .setAtivo(true)
        .setCargo(Cargo.DEV)
        .addEndereco(Endereco.newBuilder()
            .setLogradouro("Rua do meio")
            .setCep("000000")
            .setComplemento("Bloco 7")
            .build()
        ).build()

    println(request)
    request.writeTo(FileOutputStream("funcionario-request.bin"))

    val request2 = FuncionarioRequest.newBuilder()
        .mergeFrom(FileInputStream("funcionario-request.bin"))

    request2.setCargo(Cargo.GERENTE).build()

    println(request2)
}