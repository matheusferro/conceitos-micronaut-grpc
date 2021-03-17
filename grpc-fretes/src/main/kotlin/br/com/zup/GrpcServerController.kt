package br.com.zup

import io.micronaut.grpc.server.GrpcEmbeddedServer
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/grpc-server")
class GrpcServerController (val grpcServer: GrpcEmbeddedServer) {

    @Get("/stop")
    fun stopServer(): HttpResponse<String> {
        grpcServer.stop()
        return HttpResponse.ok("is-running? ${grpcServer.isRunning}")
    }
}