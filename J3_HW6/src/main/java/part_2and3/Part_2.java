package part_2and3;

import java.util.Arrays;

public class Part_2 {
    public int [] splitAfter4(int [] arr) throws RuntimeException{
        int splitNumber = 4;
        int indexLast = -1;

        if(arr.length==0){
            throw new RuntimeException();
        }else{
            for (int i = 0; i < arr.length; i++) {
                if(arr[i]==splitNumber) indexLast=i;
            }
            if(indexLast==-1){
                throw new RuntimeException();
            }else return Arrays.copyOfRange(arr, indexLast+1, arr.length);
        }
    }
}
