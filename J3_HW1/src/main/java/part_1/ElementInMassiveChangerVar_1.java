package part_1;

public class ElementInMassiveChangerVar_1<T> {
        private T[] tArr;
        private T t;

    public ElementInMassiveChangerVar_1(T[] arr) {
        this.tArr = arr;
    }

    public void eIMC( ){
            for (int i = 0; i < tArr.length-1 ; i++) {
                t = tArr[i];
                tArr[i] = tArr[i+1];
                tArr[i+1] = t;
            }
    }

    public T[] getTarr() {
        return tArr;
    }
}
