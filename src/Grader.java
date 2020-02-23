import java.util.*;
import java.io.*;

// WHERES ADAM? OHPER
class Grader {
    /** Directories of the two test files */
    public static final String PHASE1 = "data/grademefirst.txt";
    public static final String PHASE2 = "data/grademesecond.txt";

    /** Your provided dictionary of words, parsed into sets */
    public static Set<String> nouns = makeSet("words/nouns.txt");
    public static Set<String> verbs = makeSet("words/verbs.txt");
    public static Set<String> preps = makeSet("words/prepositions.txt");
    public static Set<String> determ = makeSet("words/determiners.txt");

//    /** Symbols to represent items in the grammars */
//    private static char START               = 'S';
//    private static char NOUN                = 'N';
//    private static char NOUN_PHRASE         = 'n';
//    private static char VERB                = 'V';
//    private static char VERB_PHRASE         = 'v';
//    private static char PREP                = 'P';
//    private static char PREP_PHRASE         = 'p';
//    private static char DETERMINER          = 'D';

    /**
     * Attempts to apply a specific grammar rule given the parser's current
     * stack. This involves matching the top items on the stack with the
     * RHS of a grammar rule and replacing it with the LHS of the corresponding
     * rule (if possible).
     *
     * @param stack the current working stack of the parser
     * @param LHS the left hand side of a given grammar rule
     * @param RHS the right hand side of a given grammar rule
     * @return whether the specific rule was successfully applied
     */
    private static boolean applyRule(StringBuilder stack, char LHS, String RHS) {
        int len = RHS.length();

        if (stack.length() < len) return false;

        String match = stack.substring(stack.length() - len);

        if (!match.equals(RHS)) return false;

        stack.setLength(stack.length() - len);
        stack.append(LHS);
        return true;
    }

    /**
     * Returns true if the given string has a valid parentheses matching
     * @param s String to check parentheses matching
     * @return true if valid parens matching, false otherwise
     */
    public static boolean parensParser(String s) {
        StringBuilder stack = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            stack.append(c);

            while (true) {
                if (!applyRule(stack, 'S', "()")
                        && !applyRule(stack, 'S', "(S)")
                        && !applyRule(stack, 'S', "SS")) break;
            }
        }
        return stack.toString().equals("S");
    }

    /**
     * Converts a sentence of words into a String of Characters
     * representing each word's speech (noun, verb, det, or prep)
     * @param sentence to convert
     * @return String where each Character represents a word
     */
    private static String sentenceToWordTypes(String sentence) {
        // TODO: IMPLEMENT THIS!
        return null;
    }

    /**
     * Returns true if the given string follows the given English Grammar
     * @param s String to check English Grammar
     * @return true if valid, false otherwise
     */
    public static boolean englishParser(String s) {
        String[] words = s.split(" ");

        StringBuilder stack = new StringBuilder();

        for (String word : words) {
            stack.append(word);

            while (true) {
                boolean result = applyRule(stack, 'S', "NV")
                        || applyRule(stack, 'N', "dn")
                        || applyRule(stack, 'V', "vN")
                        || applyRule(stack, 'V', "vP")
                        || applyRule(stack, 'P', "pn")
                        || applyRule(stack, 'P', "pN");

                for (String noun : nouns) {
                    result |= applyRule(stack, 'n', noun);
                }
                for (String verb : verbs) {
                    result |= applyRule(stack, 'v', verb);
                }
                for (String prep : preps) {
                    result |= applyRule(stack, 'p', prep);
                }
                for (String det : determ) {
                    result |= applyRule(stack, 'd', det);
                }
                if (!result) break;
            }
        }

        return stack.toString().equals("S");
    }

    /**
     * Given a dictionary filename, makes a set of strings
     * @param filename dictionary filename
     * @return Set of Strings containing dictionary words
     */
    private static Set<String> makeSet(String filename) {
        Scanner in = null;
        Set<String> wordSet = new TreeSet<>(Comparator.reverseOrder());

        try {
            in = new Scanner(new File(filename));
        } catch (FileNotFoundException f) {
            System.out.println("Cannot find file " + filename);
            System.exit(0);
        }
        while (in.hasNextLine()) {
            wordSet.add(in.nextLine().trim());
        }
        return wordSet;
    }

    /**
     * Tests matchParens() against the test set
     * @return half of the decoded message
     */
    public static String testMatchParens() {
        Scanner phase1 = null;
        StringBuilder hidden_value = new StringBuilder();

        try {
          phase1 = new Scanner(new File(PHASE1));
        } catch (FileNotFoundException f) {
          System.out.println("Error: Phase 1 File Not Found");
          System.exit(0);
        }

        while (phase1.hasNextLine()) {
            String line = phase1.nextLine();
            String[] results = line.split(":");

            if (parensParser(results[1].trim())) {
                hidden_value.append(results[0]);
            }
        }
        return hidden_value.toString();
    }

    /**
     * Tests reduceParser() against the test set
     * @return the other half of the decoded message
     */
    public static String testReduceParser() {
        Scanner phase2 = null;
        StringBuilder hidden_value = new StringBuilder();

        try {
          phase2 = new Scanner(new File(PHASE2));
        } catch (FileNotFoundException f) {
          System.out.println("Error: Phase 2 File Not Found");
          System.exit(0);
        }

        while (phase2.hasNextLine()) {
            String line = phase2.nextLine();
            String[] results = line.split(":");

            if (englishParser(results[1].trim())) {
                hidden_value.append(results[0]);
            }
        }
        return hidden_value.toString();
    }

    public static void main(String[] args) {
//        System.out.println(testMatchParens());

        System.out.println(testMatchParens() + " " + testReduceParser());
    }
}
