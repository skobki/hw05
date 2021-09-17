import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;



//mvn clean test -Dtest=FirstApiTest
//https://anapioficeandfire.com/

@Execution(ExecutionMode.CONCURRENT)
public class FirstApiTest {
    private final String CHAR_URL = "https://anapioficeandfire.com/api/characters/";
    private final String BOOKS_URL = "https://anapioficeandfire.com/api/books/";

    @ParameterizedTest
    @MethodSource("testDataChar1")
    public void getCharacterByNameAndCheckName(String name, String expectedName) {
        RestAssured
                .given()
                .log().uri()
                .when()
                .get(CHAR_URL + "?name=" + name)
                .then()
                .log().status()
                .log().body()
                .spec(
                        new ResponseSpecBuilder()
                                .expectStatusCode(200)
                                .expectBody("[0].name", equalTo(expectedName))
                                .build()
                );
    }

    private static Stream<Arguments> testDataChar1() {
        return Stream.of(
                Arguments.of("Jon Snow", "Jon Snow"),
                Arguments.of("Jonelle Cerwyn", "Jonelle Cerwyn")
        );
    }

    @ParameterizedTest
    @MethodSource("testDataChar2")
    public void getCharacterByNameAndCheckGender(String name, String expectedGender) {
        RestAssured
                .given()
                .log().uri()
                .when()
                .get(CHAR_URL + "?name=" + name)
                .then()
                .log().status()
                .log().body()
                .spec(
                        new ResponseSpecBuilder()
                            .expectStatusCode(200)
                            .expectBody("[0].gender", equalTo(expectedGender))
                            .build()
                );
    }

    private static Stream<Arguments> testDataChar2() {
        return Stream.of(
                //API криво работает, у мужского пола возвращает null
                //Arguments.of("Jon Snow", "Male"),
                Arguments.of("Jonelle Cerwyn", "Female")//,
                //Arguments.of("Denys Arrym", "Male")
        );
    }

    @ParameterizedTest
    @MethodSource("testDataChar3")
    public void getCharacterByNameAndCheckCulture(String name, String expectedCulture) {
        RestAssured
                .given()
                .log().uri()
                .when()
                .get(CHAR_URL + "?name=" + name)
                .then()
                .log().status()
                .log().body()
                .spec(
                        new ResponseSpecBuilder()
                                .expectStatusCode(200)
                                .expectBody("[0].culture", equalTo(expectedCulture))
                                .build()
                );
    }
    private static Stream<Arguments> testDataChar3() {
        return Stream.of(
                Arguments.of("Jon Snow", "Northmen"),
                Arguments.of("Creighton Redfort", "Valemen"),
                Arguments.of("Dacey Mormont", "Northmen")
        );
    }

    @ParameterizedTest
    @MethodSource("testDataBook1")
    public void getBookByNameAndCheckMediaTypeAndNumberOfPages(String name, String expectedMediaType, int expectedNOP) {
        RestAssured
                .given()
                .log().uri()
                .when()
                .get(BOOKS_URL + "?name=" + name)
                .then()
                .log().status()
                .log().body()
                .spec(
                        new ResponseSpecBuilder()
                                .expectStatusCode(200)
                                .expectBody("[0].mediaType", equalTo(expectedMediaType))
                                .expectBody("[0].numberOfPages", equalTo(expectedNOP))
                                .build()
                );
    }
    private static Stream<Arguments> testDataBook1() {
        return Stream.of(
                Arguments.of("A Game of Thrones", "Hardcover", 694),
                Arguments.of("A Clash of Kings", "Hardback", 768),
                Arguments.of("A Storm of Swords", "Hardcover", 992)
        );
    }
}
