
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author mangohero
 * @create-time Nov 11, 2015 7:59:51 PM
 */
public class EnglishSegment {
    private static final int List = 0;
    private static String inputPath;
    private static int max = 20;
    private static Map<String, Double> dictMap = new java.util.HashMap<String, Double>();
    private static double Frequency = 0.0;
    private static List<List<String>> ruleList = new ArrayList<List<String>>();
    private static int flag = 0;

    public EnglishSegment() {

    }

    public EnglishSegment(String inputPath) {
        String readLine = "";
        IOhandle iOhandle = new IOhandle();
        try {
            BufferedReader br = iOhandle.FileReader(inputPath);
            while ((readLine = br.readLine()) != null) {
                String[] temp = readLine.split("\t");
                dictMap.put(temp[0], Double.parseDouble(temp[1]));
                Frequency += Double.parseDouble(temp[1]);
            }
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static List<String> SegmentWithProb(String word) {
        List<String> segmented = segment(word);
        GetSequence(segmented);
        return segmented;
    }

    public static List<String> segment(String word) {
        Map<List<String>, Double> comparatorMap = new HashMap<List<String>, Double>();
        if (word.isEmpty()) {
            return null;
        }
        else {
            word = word.toLowerCase();
            List<List<String>> allSegmentationList = new ArrayList<List<String>>();
            for (int i = 0; i < EnglishSegment.WordCombination(word).size(); i++) {
                final List<String> tupleList = EnglishSegment.WordCombination(word).get(i);
                // System.out.println(tupleList);
                final List<String> firstList = new ArrayList<String>();
                firstList.add(tupleList.get(0));

                allSegmentationList.add(new ArrayList<String>() {
                    {
//                      if (flag == 0) {
                            if (!tupleList.get(1).isEmpty()) {
                                add(firstList.get(0).toString());
                                List<String> result = segment(tupleList.get(1).toString());
                                for(String r:result){
                                    add(r);
                                }
                            }
                            else {
                                add(firstList.get(0).toString());
                            }
//                      }
//                      else {
//                          if (!tupleList.get(tupleList.size() - 1).equals("")) {
//                              for (String tuple : tupleList) {
//                                  add(tuple);
//                              }
//                          }
//                          else {
//                              for (int k = 0; k <= tupleList.size() - 2; k++) {
//                                  add(tupleList.get(k).toString());
//                              }
//                          }
//                      }

                    }
                });
            }
            // load allSegmentationList and score into map and get
            // maximum value.

            for (List<String> unit : allSegmentationList) {

                List<String> key = unit;
                Double value = GetSequence(unit);
                comparatorMap.put(key, value);
            }

            flag++;
            //System.out.println(allSegmentationList);
            List<String> result = Comparator(comparatorMap);
            //System.out.println(result);
            return result;
        }
    }

    public static List<List<String>> WordCombination(String word) {
        int lengh = (word.length() >= max) ? word.length() : max;
        List<List<String>> combinationList = new ArrayList<List<String>>();
        for (int i = 0; i < lengh; i++) {
            List<String> tupleList = new ArrayList<String>();
            if (i < word.length()) {
                String leftPart = word.substring(0, i + 1);
                String rightPart = word.substring(i + 1, word.length());
                tupleList.add(leftPart);
                tupleList.add(rightPart);
            }
            else {
                tupleList.add(word);
                tupleList.add("");
            }
            combinationList.add(tupleList);
        }
        // System.out.println(combinationList);
        return combinationList;
    }

    public static double GetSequence(List<String> wordsList) {
        Double sum = 0.0;
        for (String w : wordsList) {
            sum += Math.log10(OneWordProbability(w));
        }
        return sum;
    }

    public static double OneWordProbability(String key) {
        if (dictMap.get(key) != null) {
            double result = dictMap.get(key) / Frequency;
            return result;
        }
        else {
            return 1.0 / (Frequency * Math.pow(10, (key.length() - 2)));
        }
    }

    // Comparator by value
    private static class ValueComparator implements Comparator<Map.Entry<List<String>, Double>> {
        public int compare(Map.Entry<List<String>, Double> m, Map.Entry<List<String>, Double> n) {
            double difference = n.getValue() - m.getValue();
            if (difference > 0) {
                return 1;
            }
            else {
                return -1;
            }
        }
    }

    public static List<String> Comparator(Map<List<String>, Double> map) {

        List<Map.Entry<List<String>, Double>> list = new ArrayList<Entry<List<String>, Double>>();
        list.addAll(map.entrySet());
        ValueComparator vc = new ValueComparator();
        Collections.sort(list, vc);
        Entry entry = list.get(0);
        List<String> key = (java.util.List<String>) entry.getKey();
        return key;

    }

    public static void main(String[] args) {

        String word = "akb48tokyolive";

        EnglishSegment englishSegment = new EnglishSegment("/Users/wchen/Documents/Eclipseworkspace/UDFPractise/src/main/java/jp/co/yahoo/querysuggest/gossip/one-grams.txt");
        System.out.println(SegmentWithProb(word));
    }

}
