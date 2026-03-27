package pl.mati.taskintelligenceapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.CompletableFuture;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NotificationDispatcherTest {
    @LocalServerPort
    private int port;

    private WebSocketStompClient stompClient;

    @BeforeEach
    void shouldReciveMessageFromDispatcher() throws Exception{

    }
}
