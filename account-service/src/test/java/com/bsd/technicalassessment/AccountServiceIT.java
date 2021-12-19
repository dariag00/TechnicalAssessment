package com.bsd.technicalassessment;

import com.bsd.technicalassessment.model.Account;
import com.bsd.technicalassessment.model.AccountCreationRequest;
import com.bsd.technicalassessment.model.Transfer;
import com.bsd.technicalassessment.model.TransferRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountServiceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_PATH = "http://localhost:9090";

    @Test
    void transferTest() throws Exception {
        //First we create the accounts
        AccountCreationRequest firstAccountCreationRequest = new AccountCreationRequest("ES7921000813610123456789", BigDecimal.valueOf(1000));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH + "/accounts")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(firstAccountCreationRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        Account firstAccount = objectMapper.readValue(result.getResponse().getContentAsString(), Account.class);
        Assertions.assertEquals(firstAccountCreationRequest.getIban(), firstAccount.getIban());
        Assertions.assertEquals(firstAccountCreationRequest.getInitialBalance(), firstAccount.getBalance());

        AccountCreationRequest secondAccountCreationRequest = new AccountCreationRequest();
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH + "/accounts")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(secondAccountCreationRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        secondAccountCreationRequest.setIban("ES7921000813610123456700");
        result = mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH + "/accounts")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(secondAccountCreationRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        Account secondAccount = objectMapper.readValue(result.getResponse().getContentAsString(), Account.class);
        Assertions.assertEquals(secondAccountCreationRequest.getIban(), secondAccount.getIban());
        Assertions.assertEquals(BigDecimal.ZERO, secondAccount.getBalance());

        //Then we perform the transfer
        TransferRequest transferRequest = new TransferRequest(firstAccount.getIban(), secondAccount.getIban(), BigDecimal.valueOf(500));
        result = mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH + "/transfer")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        Transfer resultTransfer = objectMapper.readValue(result.getResponse().getContentAsString(), Transfer.class);
        Assertions.assertEquals(firstAccount.getId(), resultTransfer.getFromAccount().getId());
        Assertions.assertEquals(secondAccount.getId(), resultTransfer.getToAccount().getId());
        Assertions.assertEquals(BigDecimal.valueOf(500), resultTransfer.getAmount());

        //Finally, we check the accounts balance
        result = mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/accounts/" + firstAccount.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        firstAccount = objectMapper.readValue(result.getResponse().getContentAsString(), Account.class);

        Assertions.assertEquals(1, firstAccount.getSentTransfers().size());
        Assertions.assertEquals(0, firstAccount.getReceivedTransfers().size());

        result = mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + "/accounts/" + secondAccount.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        secondAccount = objectMapper.readValue(result.getResponse().getContentAsString(), Account.class);

        Assertions.assertEquals(0, secondAccount.getSentTransfers().size());
        Assertions.assertEquals(1, secondAccount.getReceivedTransfers().size());
    }

}
