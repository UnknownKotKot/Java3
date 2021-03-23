package part_2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MassiveToArrayListRebuilder<T> {

    public ArrayList<T> eTALR(T[] arr ){

        return Arrays.stream(arr).collect(Collectors.toCollection(ArrayList::new));

    }

}
