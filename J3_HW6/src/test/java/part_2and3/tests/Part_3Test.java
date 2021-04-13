package part_2and3.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import part_2and3.Part_3;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class Part_3Test {
    private static Part_3 p;
    private static int[] arr1 = new int[0];
    private static int[] arr2 = {0,0,5,6};
    private static int[] arr3 = {1,4,5,6};
    private static int[] arr4 = {0,0,5,6,4};
    private static int[] arr5 = {0,0,5,6,1};

    @BeforeAll
    static void init(){
        p = new Part_3();
    }

    @ParameterizedTest
    @EmptySource
    void valueEmptyCheck(int [] empty){
        Assertions.assertFalse(()->p.valueCheck(empty));
    }

    @ParameterizedTest
    @MethodSource("massFactory1")
    void valueFromMass1Check(int [] arr){
        Assertions.assertFalse(()->p.valueCheck(arr));
    }
    static Stream<Arguments> massFactory1 (){
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(arr1));
        out.add(Arguments.arguments(arr2));
        out.add(Arguments.arguments(arr4));
        out.add(Arguments.arguments(arr5));
        return out.stream();
    }

    @ParameterizedTest
    @CsvSource({
            "1, 4, 0, 9",
            "1, 9, 5, 4",
            "4, 5, 5, 1",
            "5, 5, 1, 4"
    })
    void testWithCsvSource(int a, int b, int c, int d){
        int[] mass = {a, b, c, d};
        Assertions.assertTrue(()->p.valueCheck(mass));
    }

    @ParameterizedTest
    @CsvFileSource(files = {"src\\test\\java\\part_2and3\\tests\\testData.csv"})
    void testWithCsvFileSource(int a, int b, int c, int d){
        int[] mass = {a, b, c, d};
        Assertions.assertFalse(()->p.valueCheck(mass));
    }

    @Test
    void valueCheck1() {
        Assertions.assertFalse(()->p.valueCheck(arr1));
    }
    @Test
    void valueCheck2() {
        Assertions.assertFalse(()->p.valueCheck(arr2));
    }
    @Test
    void valueCheck3() {
        Assertions.assertTrue(()->p.valueCheck(arr3));
    }
    @Test
    void valueCheck4() {
        Assertions.assertFalse(()->p.valueCheck(arr4));
    }
    @Test
    void valueCheck5() {
        Assertions.assertFalse(()->p.valueCheck(arr5));
    }
}