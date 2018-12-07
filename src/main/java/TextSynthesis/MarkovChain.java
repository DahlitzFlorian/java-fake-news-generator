package TextSynthesis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static java.util.Arrays.copyOfRange;

class MarkovChain {
    private final static int EXTRA_WEIGTH = 3;

    private int order;
    private int length;
    private String[] words;
    private String[] keywords;
    private Map<String, Map<String, Integer>> nGrams = new HashMap<>();
    private Random rnd = new Random();

    MarkovChain(String corpus, int order, int length, String[] keywords) {
        this.order = order;
        this.words = corpus.split("\\s");
        this.length = length;
        this.keywords = keywords;
    }

    String markovify() {
        generateNGrams();
        //Choose starting gram, for now choose the first word sequence
        String currentGram = getRandomStartingNGram();
        StringBuilder result = new StringBuilder();
        result.append(currentGram);

        for (int i = 0; i < (int) length/order; i++) {
            Map<String, Integer> possibilities = nGrams.get(currentGram);

            if (possibilities == null) {
                possibilities = nGrams.get(getRandomStartingNGram());
                result.append("\n\n");
            }
            Map<String, Integer> backup = new HashMap<>(possibilities);
            possibilities.replaceAll((k, v) -> {
                for(String keyword: keywords) {
                    if(k.toLowerCase().contains(keyword.toLowerCase())) { v+=EXTRA_WEIGTH; }
                }
                return v;
            });

            String next = getRandomNextEntry(possibilities);
            while(next == null) {
                next = getRandomNextEntry(possibilities);
            }
            //restore old values
            possibilities.putAll(backup);
            currentGram = next;
            result.append(" ");
            result.append(next);
            //some simple formatting
            if(i%100 == 0) result.append("\n\n");
            else if(i%10 == 0) result.append("\n");

        }
        return result.toString();
    }

    private String getRandomNextEntry(Map<String, Integer> probabilities) {
        int totalSum = 0;
        Set<String> keySet = probabilities.keySet();
        for (String key : keySet) {
            totalSum += probabilities.get(key);
        }
        int index = rnd.nextInt(totalSum);
        if (index == 0) index = 1;
        int sum = 0;
        String[] keySetAsString = keySet.toArray(new String[keySet.size()]);
        int i = 0;
        while (sum < index) {
            sum += probabilities.get(keySetAsString[i++]);
        }
        return keySetAsString[i-1];
    }

    //TODO english comments
    private void generateNGrams() {
        int wordCount = words.length;

        for (int i = 0; i < wordCount - order; i++) {
            //Wörterketten der größe 'order' aus array rausnehmen
            String[] gramArray = copyOfRange(words, i, i + order);
            String gram = arrayToString(gramArray);
            String followingGram = arrayToString(copyOfRange(words, order + i, order * 2 + i));

            //if gram is not yet stored in map
            if (!nGrams.containsKey(gram)) {
                nGrams.put(gram, new HashMap<>());
            }

            //if following words not in following grams -> add it else increase occurrence
            if (!nGrams.get(gram).containsKey(followingGram)) {
                nGrams.get(gram).put(followingGram, 1);
            } else {
                int occurrence = nGrams.get(gram).get(followingGram);
                nGrams.get(gram).put(followingGram, ++occurrence);
            }
        }
    }

    //TODO make this work for different orders, only works for order 2. Or implement a better method
    private String arrayToString(String[] array) {
        String temp = Arrays.toString(array).replace("[", "").replace("]", "");
        for(int i = 1; i < order; i++) {
            temp = temp.replaceFirst(",", "");
        }
        return temp;
    }

    //TODO implement this
    private List<String> generateStartingNGrams() {
        List<String> startingGrams = new ArrayList<>();
        for (String key: nGrams.keySet()) {
            if(key.matches("^[A-Z](.*)")) startingGrams.add(key);
        }
        return startingGrams;
    }

    private String getRandomStartingNGram() {
        List<String> startingGrams = generateStartingNGrams();
        int rndGram = rnd.nextInt(startingGrams.size());
        return startingGrams.get(rndGram);
    }
}
