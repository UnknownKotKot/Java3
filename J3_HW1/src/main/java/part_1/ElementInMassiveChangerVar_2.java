package part_1;

public class ElementInMassiveChangerVar_2<T> {

    public T[] eIMC2(T[] arr ){
        for (int i = 0; i < arr.length-1 ; i++) {
            T t = arr[i];
            arr[i] = arr[i+1];
            arr[i+1] = t;
        }
        return arr;
    }
}
