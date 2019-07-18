import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class WordCount {

    public static void main(String[] args) throws IOException {

        //Reading the file using buffered reader and file reader
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

        //replace all the numbers, full stops, double and single quotations, square brackets line breaks and tab spaces with empty strings
        everything = everything.replaceAll("[0-9,\\(.)\"\'\\[\\]\n\t]","");
        String[] words = everything.split("[\\s]");

        ArrayList<String> finalWords = new ArrayList<>();

        //add all the split words in the array to the array list
        for(int i =0; i< words.length; i++){
            finalWords.add(words[i]);
        }

        //remove all the null values in the array list
        finalWords.removeAll(Arrays.asList("", null));

        //Total count of words is the size of the words array
        System.out.println("Total number of words: "+finalWords.size());

        //Put the list to a set as the list doesn't store any duplicate values
        Set<String> uniqueWords = new HashSet<>(finalWords);
        System.out.println("Total number of unique words: "+ uniqueWords.size());

        //Put the values in the list to a hash map with values of 1
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

        //Print the number of occurrences of a word using the hash map
        System.out.println("Occurrence of each word in the file: ");
        map.entrySet().forEach(entry-> System.out.println(entry.getKey()+ " : "+ entry.getValue()));
    }
}
