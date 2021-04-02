package ClientSide;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LogSave {

    public void logSaveFull(String inputMessages) throws IOException {
        File file = new File("src/main/java/ClientSide/test.txt");
        if(!file.exists()){
            file.createNewFile();
        }
        try (FileWriter fw = new FileWriter(file, true)){
            fw.write(inputMessages + "\n");
        }
    }

    public List<String> logGet100() throws IOException {
        ArrayList<String> arrStr = new ArrayList<>();
        String str;
        File file = new File("src/main/java/ClientSide/test.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            while((str = br.readLine())!=null) {
                arrStr.add(str);
            }
        }
        if(arrStr.size()>100){
            return arrStr.stream()
                    .skip(arrStr.size()-100)
                    .limit(100)
                    .collect(Collectors.toList());
        }else{
            return arrStr;
        }
    }
}




