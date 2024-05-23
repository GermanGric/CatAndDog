import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class CrptApi {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final Semaphore semaphore;
    private final ScheduledExecutorService scheduler;

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        httpClient = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper();
        semaphore = new Semaphore(requestLimit);
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(semaphore::release, 0, 1, timeUnit);
    }

    public String createDocument(Object document, String signature) throws IOException, InterruptedException {
        semaphore.acquire();

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.set("description", objectMapper.valueToTree(document));
        rootNode.put("doc_id", "string");
        rootNode.put("doc_status", "string");
        rootNode.put("doc_type", "LP_INTRODUCE_GOODS");
        rootNode.put("importRequest", true);
        rootNode.put("owner_inn", "string");
        rootNode.put("participant_inn", "string");
        rootNode.put("producer_inn", "string");
        rootNode.put("production_date", "2020-01-23");
        rootNode.put("production_type", "string");
        rootNode.putArray("products")
                .addObject()
                .put("certificate_document", "string")
                .put("certificate_document_date", "2020-01-23")
                .put("certificate_document_number", "string")
                .put("owner_inn", "string")
                .put("producer_inn", "string")
                .put("production_date", "2020-01-23")
                .put("tnved_code", "string")
                .put("uit_code", "string")
                .put("uitu_code", "string");
        rootNode.put("reg_date", "2020-01-23");
        rootNode.put("reg_number", "string");

        String requestBody = objectMapper.writeValueAsString(rootNode);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://ismp.crpt.ru/api/v3/lk/documents/create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        CrptApi api = new CrptApi(TimeUnit.SECONDS, 5);

        // Example document
        Object document = new Object() {
            public String participantInn = "string";
            // add other fields as necessary
        };

        // Example signature
        String signature = "example_signature";

        String response = api.createDocument(document, signature);
        System.out.println(response);
    }
}
