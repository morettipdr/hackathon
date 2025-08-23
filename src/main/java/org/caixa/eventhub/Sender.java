package org.caixa.eventhub;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import org.caixa.model.dto.loan.LoanResponseDTO;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;

@ApplicationScoped
public class Sender {
    @ConfigProperty(name = "eventhub.connection.string")
    private String connectionString;

    @ConfigProperty(name = "eventhub.name")
    private String eventHubName;

    public void publishEvents(LoanResponseDTO loanResponseDTO) {
        ObjectMapper objectMapper = new ObjectMapper();
        try (EventHubProducerClient producer = new EventHubClientBuilder()
                .connectionString(connectionString, eventHubName)
                .buildProducerClient()) {

            String json = objectMapper.writeValueAsString(loanResponseDTO);
            EventData eventData = new EventData(json);
            eventData.setContentType("application/json");

            producer.send(List.of(eventData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
