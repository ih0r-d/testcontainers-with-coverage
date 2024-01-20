package com.example;

import com.example.dto.ApplianceDto;
import com.example.entities.Appliance;
import com.example.repository.ApplianceRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.List;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConfiguration(proxyBeanMethods = false)
class ApplianceIntegrationTest extends AbstractTestContainersIntegrationTest {

    @Autowired
    ApplianceRepository repository;

    @LocalServerPort
    protected Integer port;

    private final String apiUrl = "/api/v1/appliances";

    @BeforeEach
    void setUp() {
        postgres.start();
        RestAssured.baseURI = "http://localhost:" + port;
        repository.deleteAll();
    }

    @Test
    void test_IfPostgresContainerIsUp() {
        Assertions.assertNotNull(postgres);
        Assertions.assertTrue(postgres.isRunning());
    }

    @Test
    void test_ifEmptyGetAll() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(apiUrl)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(".", Matchers.empty());
    }

    @Test
    void test_ifNotEmptyGetAllAfterSavedData() {
        var appliances = List.of(
                new Appliance(null, 0, "Siemens", "Hair dryer"),
                new Appliance(null, 0, "Bosch", "Television"),
                new Appliance(null, 0, "Whirlpool", "Cold-pressed juicer")
        );
        var savedAppliances = repository.saveAll(appliances);

        Assertions.assertNotNull(savedAppliances);
        Assertions.assertFalse(savedAppliances.isEmpty());
        Assertions.assertEquals(appliances.size(), savedAppliances.size());

        given()
                .contentType(ContentType.JSON)
                .when()
                .get(apiUrl)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(".", Matchers.hasSize(3));
    }

    @Test
    void test_ifNotFoundItemById() {

        given()
                .contentType(ContentType.JSON)
                .when()
                .get(apiUrl + "/{id}", 100)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("detail", Matchers.containsString("not found"));
    }

    @Test
    void test_ifNotEmptyGetByIdAfterSaveNewDataItem() {
        var appliance = new Appliance(null, 0, "LG", "Vacuum cleaner");
        var savedAppliance = repository.save(appliance);

        Assertions.assertNotNull(savedAppliance);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get(apiUrl + "/{id}", savedAppliance.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", Matchers.equalTo(savedAppliance.getId().intValue()))
                .body("amount", Matchers.equalTo(savedAppliance.getAmount()))
                .body("brand", Matchers.equalTo(savedAppliance.getBrand()))
                .body("equipment", Matchers.equalTo(savedAppliance.getEquipment()));
    }

    @Test
    void test_IfAmountIncrementInLoop() {
        var appliance = new Appliance(null, 0, "Blue Star", "Appliance plug");

        var savedAppliance = repository.save(appliance);
        Assertions.assertNotNull(savedAppliance);


        int counter = 5;
        for (int i = 0; i < counter; i++) {
            given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get(apiUrl + "/{id}", savedAppliance.getId())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("amount", Matchers.equalTo(i));
        }
    }

    @Test
    void test_IfCreateAndSaveCorrectly() {
        var dto = new ApplianceDto(null, 0, "Blue Star", "Appliance plug");


        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post(apiUrl)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", Matchers.notNullValue())
                .body("amount", Matchers.equalTo(0));
    }

    @Test
    void test_IfNotCreatedAndCatchError() {
        var dto = new ApplianceDto(null, 0, null, "Water heater");

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post(apiUrl)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("detail", Matchers.containsString("cannot be left blank"))
                .log();
    }

    @Test
    void test_IfDeletedCorrectlyById() {

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete(apiUrl + "/{id}", 1L)
                .then()
                .statusCode( HttpStatus.NO_CONTENT.value());
    }
}
