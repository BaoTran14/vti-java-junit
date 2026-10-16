package vn.vti;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedClass;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@ParameterizedClass
//@CsvSource({"apple, 1$", "orange, 2$" })
public class CalculatorTest {
    static final Logger logger = LogManager.getLogger(CalculatorTest.class);
    static Calculator calculator;
//    String fruit, price;
//    CalculatorTest(String fruit, String price) {
//        this.fruit = fruit;
//        this.price = price;
//    }

    @BeforeAll
    // Run before any test case (Run before 1st test case)
    // Run only once in a class
    static void prepareTestEnvironment() {
        calculator = new Calculator();
        logger.debug("Initialize Calculator object {}", calculator);
    }

    @BeforeAll
    static void prepareData() {
        logger.info("2nd precondition");
    }

    @AfterAll
    // Run after last test case
    // Run only once
    static void afterAll() {
        calculator = null;
        logger.debug("Remove Calculator object {}", calculator);
    }

    @AfterAll
    // Run after last test case
    // Run only once
    static void afterAll_1() {
        logger.info("2nd post condition");
    }

    @BeforeEach
        // Run before every test case
    void beforeEach(TestInfo testInfo) {
        logger.info("--------START TESTING CASE {}--------", testInfo.getTestMethod());
    }

    @AfterEach
        // Run after every test case
    void afterEach(TestInfo testInfo) {
        logger.info("--------FINISH TESTING CASE {}--------", testInfo.getTestMethod());
    }

    @Test
    void testAdd() {
        //Assert result == 2
        Assertions.assertEquals(3, calculator.add(0, 2));
    }

    @Test
    void testMinus() {
        //Assert result == 2
        Assertions.assertEquals(-2, calculator.minus(0, 2));
        Assumptions.assumeTrue(false);
        calculator.add(1,2);
    }

    @ParameterizedTest
    @ValueSource(ints = {-2, 0, 6, 8, 10})
    void testAddFunctionWithZero(int arg) {
        int result = calculator.add(0, arg);
//        Assertions.assertEquals(0, result);
        Assumptions.assumeTrue(result >0);
        logger.info("Add function works correctly when adding 0 with {}. Result is: {}", arg, result);
    }

    @ParameterizedTest
    @NullSource // Add extra null data
    @EmptySource // Add extra empty data
    @ValueSource(strings = {"-2", "0", "6", "8", "10"})
    void testAddFunctionWithZeroUsingString(String arg) {
        int result = calculator.add(0, Integer.parseInt(arg));
        Assertions.assertEquals(Integer.parseInt(arg), result);
        logger.info("Add function works correctly when adding 0 with {}. Result is: {}", arg, result);
    }

    @ParameterizedTest
    @ValueSource(classes = {Calculator.class})
    void testAddFunctionWithZero_1(Class<?> arg) {
        int result = calculator.add(0, 2);
        Assertions.assertEquals(arg, result);
        logger.info("Add function works correctly when adding 0 with {}. Result is: {}", arg, result);
    }

    @ParameterizedTest
    @EnumSource(names = {"ADD", "MULTIPLY", "DIVIDE", "MINUS"})
    public void testEnumSource(MathOperator arg) {
        Assertions.assertTrue(arg == MathOperator.ADD || arg == MathOperator.MINUS);
    }

    @ParameterizedTest
    // Best used for key-value pair
    @CsvSource(value = {
            "fruit name, discount price, fixed price",
            "apple, 1$, 3$",
            "orange, 2$, 5$",
            "cucumber, 1$, 6$"
    }, useHeadersInDisplayName = true)
    void testCsvSource(String fruit, String discountPrice, String fixedPrice) {
        Assertions.assertFalse(fruit.isEmpty());
        // Validate sell price is not negative (fixed - discount)
        int fixed = Integer.parseInt(fixedPrice.replaceAll("\\$", ""));
        int discount = Integer.parseInt(discountPrice.replaceAll("\\$", ""));
        Assertions.assertTrue(calculator.minus(fixed, discount) >= 0);
    }

    @ParameterizedTest
    // Best used for key-value pair
    @CsvSource(value = {
            "fruit name | discount price | fixed price",
            "apple      | 1              | 3",
            "orange     | 2              | 5",
            "cucumber   | 1              | 6"
    }, useHeadersInDisplayName = true, ignoreLeadingAndTrailingWhitespace = false, delimiter = '|')
    void testCsvSource_1(String fruit, int discountPrice, int fixedPrice) {
        Assertions.assertFalse(fruit.isEmpty());
        // Validate sell price is not negative (fixed - discount)
        Assertions.assertTrue(calculator.minus(fixedPrice, discountPrice) >= 0);
    }

    @ParameterizedTest
    // Best used for key-value pair
    @CsvFileSource(resources = "/fruit_1.csv", useHeadersInDisplayName = true)
    void testCsvFileSource(String fruit, int discountPrice, int fixedPrice) {
        Assertions.assertFalse(fruit.isEmpty());
        // Validate sell price is not negative (fixed - discount)
        Assertions.assertTrue(calculator.minus(fixedPrice, discountPrice) >= 0);
    }

    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/fruit_1.csv", numLinesToSkip = 1)
    void testWithCsvFileSourceFromFile(String country, int reference, int a) {
        assertNotNull(country);
        assertNotEquals(0, reference);
    }

    @ParameterizedTest
    // Call external method source: package.className#methodName
    @MethodSource(value = "vn.vti.DataProviderMethod#students")
    void testWithMethodSource(String student) {
        assertFalse(student.isEmpty());
    }

    @ParameterizedTest
    // Call interner: inject methodName
    @MethodSource(value = "students")
    void testWithMethodSourceInternal(String student) {
        assertFalse(student.isEmpty());
    }

    static List<String> students() {
        return List.of("Long", "Kim", "Bang");
    }

    @ParameterizedTest
    @ArgumentsSource(value = MyDataProvider.class)
    void testWithArgumentSource(int number) {
//        assertFalse(fruit.isEmpty());
        assertTrue(number > 0);
    }

    @ParameterizedTest
    @ArgumentsSource(value = MyDataProvider.class)
    @MethodSource("students")
    void testWithMultipleSources(String value) {
        assertFalse(value.isEmpty());
    }
}
