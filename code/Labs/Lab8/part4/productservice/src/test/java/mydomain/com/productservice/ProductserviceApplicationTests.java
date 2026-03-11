package mydomain.com.productservice;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "management.tracing.enabled=false"
)
class ProductserviceApplicationTests {
    private static final AtomicInteger stockRequestCount = new AtomicInteger();
    private static final HttpServer stockServiceStub = startStubServer();

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("stockservice.url", () -> "http://localhost:" + stockServiceStub.getAddress().getPort());
    }

    @BeforeEach
    void resetStubCounter() {
        stockRequestCount.set(0);
    }

    @AfterAll
    static void stopStubServer() {
        stockServiceStub.stop(0);
    }

    @Test
    void circuitBreakerOpensAfterTwoFailuresAndSkipsTheThirdRemoteCall() {
        String productNumber = "101";

        for (int attempt = 0; attempt < 3; attempt++) {
            String response = restTemplate.getForObject(
                    "http://localhost:" + port + "/product/{productNumber}",
                    String.class,
                    productNumber
            );

            assertThat(response).isEqualTo("Product " + productNumber + " stock information is unavailable");
        }

        assertThat(stockRequestCount.get()).isEqualTo(2);
    }

    private static HttpServer startStubServer() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
            server.createContext("/stock/101", ProductserviceApplicationTests::alwaysFail);
            server.start();
            return server;
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to start stock service stub", exception);
        }
    }

    private static void alwaysFail(HttpExchange exchange) throws IOException {
        stockRequestCount.incrementAndGet();
        byte[] responseBody = "stock service unavailable".getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(503, responseBody.length);
        try (OutputStream responseStream = exchange.getResponseBody()) {
            responseStream.write(responseBody);
        }
    }
}
