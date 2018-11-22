package TextSynthesis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//TODO make this more efficient
//TODO add a weight function making chains containing keywords more frequent
//TODO implement function generating starting grams (having texts start with different words)
//TODO add context free grammar(maybe) -> text makes more sense
class MarkovChain {
    private int order;
    private int length;
    private String[] words;

    MarkovChain(String corpus, int order, int length) {
        this.order = order;
        //TODO let quotes stay intact
        this.words = corpus.split("\\s");
        this.length = length;
    }

    String morkovify() {
        Map<String, List<String>> nGrams = generateNGrams();
        //Choose starting gram, for now choose the first word sequence
        String currentGram = arrayToString(Arrays.copyOfRange(words, 0, order));
        StringBuilder result = new StringBuilder();
        result.append(currentGram);

        //TODO generate length based on user's input
        for (int i = 0; i < length; i++) {
            List<String> possibilities = nGrams.get(currentGram);
            if (possibilities == null) {
                //TODO choose new starting gram when reaching null
                System.err.println("null");
                return result.toString();
            }
            //Math.floor needed?
            int next = (int) Math.floor(Math.random() * possibilities.size());
            currentGram = possibilities.get(next);
            result.append(" ");
            result.append(possibilities.get(next));
        }
        return result.toString();
    }

    //TODO english comments
    private Map<String, List<String>> generateNGrams() {
        Map<String, List<String>> nGrams = new HashMap<>();
        int wordCount = words.length;

        for (int i = 0; i < wordCount - order; i++) {
            //Wörterketten der größe 'order' aus array rausnehmen
            String[] gramArray = Arrays.copyOfRange(words, i, i + order);
            String gram = arrayToString(gramArray);
            if (!nGrams.containsKey(gram)) {
                nGrams.put(gram, new ArrayList<>());
            }
            //Get the arrayList
            List<String> possibleWords = nGrams.get(gram);
            //Push the words that follow the gram into the List thus creating a "weight" for every word sequence
            possibleWords.add(arrayToString(Arrays.copyOfRange(words, order + i, order * 2 + i)));
            nGrams.put(gram, possibleWords);
        }
        return nGrams;
    }

    //TODO make this work for different orders, only works for order 2. Or implement a better method
    private String arrayToString(String[] array) {
        return Arrays.toString(array).replace("[","").replace("]", "").replaceFirst(",", "");
    }

    //TODO implement this
    private List<String> generateStartingNGrams() {
        return null;
    }
}
