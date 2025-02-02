package retail.com;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import retail.com.Services.OrderService;
import retail.com.controllers.dtos.NewOrderDTO;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)

public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @Mock
    private JmsTemplate jmsTemplate;

    public OrderControllerTest(MockMvc mockMvc, OrderService orderService, JmsTemplate jmsTemplate) {
        this.mockMvc = mockMvc;
        this.orderService = orderService;
        this.jmsTemplate = jmsTemplate;
    }

    @Test
    void testSaveOrder() throws Exception {
        NewOrderDTO newOrderDTO = new NewOrderDTO();
        newOrderDTO.setProductId("123");
        newOrderDTO.setCustomerId("456");
        newOrderDTO.setDiscount(10.0);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(newOrderDTO);

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated());

        verify(orderService, times(1)).newOrder(any(NewOrderDTO.class));
        verify(jmsTemplate, times(1)).convertAndSend(eq("orders"), anyString());
    }
}
