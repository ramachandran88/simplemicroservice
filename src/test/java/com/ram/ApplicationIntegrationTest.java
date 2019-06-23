package com.ram;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Ravi on 22-Jun-19.
 */
public class ApplicationIntegrationTest extends TestHelper {

    @AfterClass
    public static void tearDown() {
        Spark.stop();
        Spark.awaitStop();
    }


    @BeforeClass
    public static void setup() {
      Application.main(null);
      Spark.awaitInitialization();
    }

    @Test
    public void shouldTransferMoney() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        //create source account
        String openAccountRequest = "{\"name\":\"ram\",\"balance\":100.00}";
        UrlResponse response = postMethod("/api/open-account", openAccountRequest);

        Map map = mapper.readValue(response.body, Map.class);
        int sourceAccountId = (Integer) map.get("accountId");

        //create destination account
        openAccountRequest = "{\"name\":\"raj\",\"balance\":100.00}";
        response = postMethod("/api/open-account", openAccountRequest);

        map = mapper.readValue(response.body, Map.class);
        int destinationAccountId = (Integer) map.get("accountId");

        //transfer money from source account to destination account
        String moneyTransferRequest = "{\"sourceAccountId\":" + sourceAccountId + ",\"destinationAccountId\" : " + destinationAccountId + " ,\"transferAmount\":50.00}";
        response = postMethod("/api/money-transfer", moneyTransferRequest);

        assertThat(response).isNotNull();
        assertThat(response.body).isNotNull();
        assertThat(response.status).isEqualTo(200);

        //verify source account balance
        response = getMethod("/api/accounts/"+sourceAccountId);
        map = mapper.readValue(response.body, Map.class);
        assertThat(map.get("balance")).isEqualTo(50.00);

        //verify destination account balance
        response = getMethod("/api/accounts/"+destinationAccountId);
        map = mapper.readValue(response.body, Map.class);
        assertThat(map.get("balance")).isEqualTo(150.00);
    }

}

