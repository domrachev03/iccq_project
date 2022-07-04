import org.apache.commons.lang.WordUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Renamer {
    private static String[] tokenize(String str){
        /*
        Splits variable name on the list of tokens.

        Tokens are separated by any sign (which is not letter or number) or by
        capital letters. Signs are ignored, capitals - not.

                Also token could contain only letters or digits.

                Example of work: IWant2showHow_it_works ->
                            -> ['I', 'Want', '2', 'show', 'How', 'it', 'works']

        */
        int token_begin = 0;
        List<String> tokens = new ArrayList<String>();
        boolean word_processed = true;
        boolean number_processed = false;

        for (int idx = 1; idx < str.length(); ++idx) {
            boolean is_symb_alpha = Character.isLetter(str.charAt(idx));
            boolean is_symb_digit = Character.isDigit(str.charAt(idx));

            if (is_symb_digit && !number_processed) {
                number_processed = true;
                if (word_processed) {
                    tokens.add(str.substring(token_begin, idx));
                    token_begin = idx;
                    word_processed = false;
                }
            } else if (is_symb_alpha && !word_processed) {
                word_processed = true;
                if (number_processed) {
                    tokens.add(str.substring(token_begin, idx));
                    token_begin = idx;
                    number_processed = false;
                }
            } else if (!(is_symb_alpha || is_symb_digit)){
                tokens.add(str.substring(token_begin, idx));
                token_begin = idx + 1;
                word_processed = false;
                number_processed = false;
            } else if (Character.isUpperCase(str.charAt(idx))) {
                tokens.add(str.substring(token_begin, idx));
                token_begin = idx;
                word_processed = true;
                number_processed = false;
            }
        }
        tokens.add(str.substring(token_begin));
        String[] result = new String[tokens.size()];
        tokens.toArray(result);
        return result;
    }

    private static String[] regTokenize(String str) {
        String regexpr = "(([A-Z][a-z]*)|[a-z]+|(\\d+))";
        Pattern pattern = Pattern.compile(regexpr);
        Matcher matcher = pattern.matcher(str);
        List<String> match = new ArrayList<String>();
        while(matcher.find()){
            match.add(str.substring(matcher.start(), matcher.end()));
        }
        String[] result = new String[match.size()];
        match.toArray(result);
        return result;
    }

    public static String toCamelCase(String name) {
        /*
        Takes any variable and transforms it to camelCase.

        First, it separates variable name into tokens(kinda words) and then it
        collects them together according to camelCase rules
        */

        if (!Character.isLetter(name.charAt(0))) {
            return null;
        }
        String[] tokens = tokenize(name);
        tokens[0] = tokens[0].toLowerCase();
        for (int i = 1; i < tokens.length; ++i){
            tokens[i] = WordUtils.capitalize(tokens[i]);
        }
        return String.join("", tokens);
    }

    public static String toCamelCaseReg(String name) {
        /*
        Takes any variable and transforms it to camelCase.

        First, it separates variable name into tokens(kinda words) and then it
        collects them together according to camelCase rules
        */

        if (!Character.isLetter(name.charAt(0))) {
            return null;
        }
        String[] tokens = regTokenize(name);
        tokens[0] = tokens[0].toLowerCase();
        for (int i = 1; i < tokens.length; ++i){
            tokens[i] = WordUtils.capitalize(tokens[i]);
        }
        return String.join("", tokens);
    }
}
