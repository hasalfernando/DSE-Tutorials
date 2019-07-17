import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class WordCount {

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader("in/Java Task 01.txt"));
        String everything = "";
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();
        }
        finally {
            br.close();
        }

        everything = everything.replaceAll("[0-9,\\(.)\"\'\\[\\]\n\t]","");
        String[] words = everything.split("[\\s-]");

        ArrayList<String> finalWords = new ArrayList<>();

        for(int i =0; i< words.length; i++){
            finalWords.add(words[i]);
        }

        finalWords.removeAll(Arrays.asList("", null));

        System.out.println("Total number of words: "+finalWords.size());
        Set<String> uniqueWords = new HashSet<>(finalWords);

        System.out.println("Total number of unique words: "+ uniqueWords.size());
        HashMap<String, Integer> map = new HashMap<>();

        for(String s : finalWords){
            if(map.containsKey(s)) {
                int val = map.get(s);
                map.put(s, val + 1);
            }
            else{
                map.put(s, 1);
            }
        }

        System.out.println("Occurrence of each word in the file: ");
        map.entrySet().forEach(entry-> System.out.println(entry.getKey()+ " : "+ entry.getValue()));
    }
}
