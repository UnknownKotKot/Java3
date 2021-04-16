import java.lang.reflect.Method;
import java.util.*;

public class ClassForTesting {

    public static void start(Class c){
        Method[] methods = c.getDeclaredMethods();
        Map<Integer, String> map = new HashMap<>();
        List<Integer> listP = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        int beforeCount =0;
        int afterCount = 0;
        for (Method o: methods) {
            if ((o.getAnnotation(BeforeSuite.class) != null)) {
                beforeCount++;
                if(beforeCount>1){
                    throw new RuntimeException();
                }
                System.out.println(o);
            }
        }
        for (Method o: methods) {
            if(o.getAnnotation(Test.class)!=null) {
                String str = String.valueOf(o);
                int index = str.indexOf(".");
                str = str.trim().substring(index + 1, str.length() - 2);
                try {
                    Method m = c.getDeclaredMethod(str, null);
                    Test annotation = m.getAnnotation(Test.class);
                    if(annotation.value()>0&&annotation.value()<=10) {
                        if(listP.contains(annotation.value())){
                            listP.add((annotation.value()+10)+listP.size());
                            map.put((annotation.value()+10)+(listP.size()-1), str);
                        }else {
                            listP.add(annotation.value());
                            map.put(annotation.value(), str);
                        }
                    }else {
                        list.add(annotation.value());
                        map.put(annotation.value(), str);
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        Collections.sort(listP);
        for (int i = 0; i <listP.size() ; i++) {
            try {
                Method m = c.getDeclaredMethod(map.get(listP.get(i)), null);
                System.out.println(m);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i <list.size() ; i++) {
            try {
                Method m = c.getDeclaredMethod(map.get(list.get(i)), null);
                System.out.println(m);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        for (Method o: methods) {
            if ((o.getAnnotation(AfterSuite.class) != null)) {
                afterCount++;
                if (afterCount > 1){
                    throw new RuntimeException();
                }
                System.out.println(o);
            }
        }
    }
}
