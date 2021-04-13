package part_2and3;

public class Part_3 {
    public boolean valueCheck(int [] arr){
        int firstValueForCheck = 1;
        int secondValueForCheck = 4;
        boolean isAnyFirstValue = false;
        boolean isAnySecondValue = false;

        if(arr.length!=0){
            for (int value : arr) {
                if (value == firstValueForCheck) isAnyFirstValue = true;
                if (value == secondValueForCheck) isAnySecondValue = true;
            }
            return isAnyFirstValue && isAnySecondValue;
        } return false;
    }
}
