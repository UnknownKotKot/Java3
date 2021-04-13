package part_2and3.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import part_2and3.Part_2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class Part_2Test {
    private static Part_2 p;
    private static int[] arr1 = new int[0];
    private static int[] arr2 = {0,0,5,6};
    private static int[] arr3 = {0,4,5,6};
    private static int[] arr4 = {0,0,5,6,4};
    private static int[] expectedArr1 = {5,6};
    private static int[] expectedArr2 = {};

    @BeforeAll
    static void allInit(){
        p = new Part_2();
    }

    @ParameterizedTest
    @MethodSource("argMass1")
    void testExceptionParametrized(int[] a){
        Assertions.assertThrows(RuntimeException.class,()-> p.splitAfter4(a));
    }
    public static Stream<Arguments> argMass1(){
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(arr1));
        out.add(Arguments.arguments(arr2));
        return out.stream();
    }

    @ParameterizedTest
    @MethodSource("argMass2")
    void testArrParametrized(int[] a, int[] b){
        Assertions.assertArrayEquals(b, p.splitAfter4(a));
    }
    public static Stream<Arguments> argMass2(){
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(arr3, expectedArr1));
        out.add(Arguments.arguments(arr4, expectedArr2));
        return out.stream();
    }

    @ParameterizedTest
    @EmptySource
    void testExceptParametrized(int[] empty){
        Assertions.assertThrows(RuntimeException.class,()-> p.splitAfter4(empty));
    }

    @ParameterizedTest
    @CsvSource({
            "1, 2, 3, 5, 1",
            "3, 8, 5, 1, 2",
            "0, 0, 0, 7, 9"
                })
    void testWithCsvSource(int a, int b, int c, int d, int e) {
        int[] mass = {a, b, c, d, e};
        Assertions.assertThrows(RuntimeException.class,()-> p.splitAfter4(mass));
    }

    @ParameterizedTest
    @CsvFileSource(files = {"src\\test\\java\\part_2and3\\tests\\testData.csv"})
    void testWithCsvFileSource(int a, int b, int c, int d, int e) {
        int[] mass = {a, b, c, d, e};
        Assertions.assertThrows(RuntimeException.class,()-> p.splitAfter4(mass));
    }

    @Test
    void testException1() {
        Assertions.assertThrows(RuntimeException.class,()-> p.splitAfter4(arr1));
    }
    @Test
    void testException2() {
        Assertions.assertThrows(RuntimeException.class,()-> p.splitAfter4(arr2));
    }
    @Test
    void testArr1() {
        Assertions.assertArrayEquals(expectedArr1, p.splitAfter4(arr3));
    }
    @Test
    void testArr2() {
        Assertions.assertArrayEquals(expectedArr2, p.splitAfter4(arr4));
    }
}