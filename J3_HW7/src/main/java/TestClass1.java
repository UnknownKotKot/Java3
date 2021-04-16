
public class TestClass1 {

    @BeforeSuite
    public void testB1(){
        System.out.println("test3");
    }

    @Test(value = 20)
    public void test11(){
        System.out.println("test3");
    }
    @Test(value = -30)
    public void test12(){
        System.out.println("test3");
    }
    @Test(value = 5)
    public void test1(){
        System.out.println("test1");
    }
    @Test(value = 5)
    public void test6(){
        System.out.println("test1");
    }
    @Test(value = 5)
    public void test7(){
        System.out.println("test1");
    }
    @Test(value = 5)
    public void test2(){
        System.out.println("test2");
    }
    @Test(value = 9)
    public void test3(){
        System.out.println("test3");
    }
    @Test(value = 0)
    public void test13(){
        System.out.println("test3");
    }

    @AfterSuite
    public void testA1(){
        System.out.println("test3");
    }
}
