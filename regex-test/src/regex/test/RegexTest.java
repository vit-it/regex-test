package regex.test;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {

    public static void main(String[] args) {

        test("([0-9]{2})/([0-9]{2})/([0-9]{4})", "a15/15/2048z17/17/2049", "<$3.$2.$1>");
        // https://stackoverflow.com/questions/2708833/examples-of-regex-matcher-g-the-end-of-the-previous-match-in-java-would-be-ni
        test("(?<=\\G\\d{3})(?=\\d)" + "|" + "(?<=^-?\\d{1,3})(?=(?:\\d{3})+(?!\\d))", "-1,234,567,890.1234567890", "x");

        predicate("t(?:i|o)p", "tip");
        predicate("t(?:i|o)p", "tpp");

        replaceAll("([0-9]{2})/([0-9]{2})/([0-9]{4})", "a15/15/2048z17/17/2049", "<$3.$2.$1>");

        appendReplacement("t(?:i|o)p", "xtipo dfsg dfgtoptail", "<$0>", true);

        split("t(?:i|o)p", "", 0);
        split("t(?:i|o)p", "x", 0);
        split("t(?:i|o)p", "tipp topxztop", 0);
        split("t(?:i|o)p", "tip0top1top3top4top5top6top7top8", 1);
        split("t(?:i|o)p", "tip0top1top3top4top5top6top7top8", 2);
        split("t(?:i|o)p", "tip0top1top3top4top5top6top7top8", 3);
        split("t(?:i|o)p", "tip0top1top3top4top5top6top7top8", 255);

        //TODO: scanner
    }

    private static void test(String regex, CharSequence input, String replacement) {
        test(regex, input, replacement, 0);
    }

    private static void test(String regex, CharSequence input, String replacement, int flags) {
        Pattern p = getPattern(regex, flags);
        Matcher m = p.matcher(input);
        boolean b;
        int i;
        String s;

        out("");
        out("---------------    test    ---------------");
        out("Regular Expression: " + regex);
        out("Target string: " + input.toString());

        b = m.matches();
        out("matches: " + b);
        b = m.lookingAt();
        out("lookingAt: " + b);
        i = m.groupCount();
        out("groupCount: " + i);

        m.reset();

        i = 0;
        while (b = m.find()) {
            String str = ++i + " find " + m.group() + ", start " + m.start() + ", end " + m.end() + ", val "
                    + input.subSequence(m.start(), m.end());

            for (int g = 0; g <= m.groupCount(); g++) {
                if (g == 0)
                    str += " [";
                str += "" + g + ":" + m.group(g);
                if (g == m.groupCount()) {
                    str += "]";
                } else {
                    str += ", ";
                }
            }
            out(str);
        }

        s = m.replaceFirst(replacement);
        m.reset();
        out("replaceFirst: " + s);

        s = m.replaceAll(replacement);
        m.reset();
        out("replaceAll: " + s);
    }

    private static void predicate(String regex, String input) {
        predicate(regex, input, 0);
    }

    private static void predicate(String regex, String input, int flags) {
        out("");
        out("---------------    predicate    ---------------");
        out("Regular Expression: " + regex);
        out("Target string: " + input);
        // (?:x) - исключить из запоминания, содержимое не попадет в группу
        boolean b;
        Pattern p = getPattern(regex, flags);

        Predicate<String> predicate = p.asPredicate();
        b = predicate.test(input);
        out("test: " + b);
    }

    private static void replaceAll(String regex, CharSequence input, String replacement) {
        replaceAll(regex, input, replacement, 0);
    }

    private static void replaceAll(String regex, CharSequence input, String replacement, int flags) {

        Pattern p = getPattern(regex, flags);
        Matcher m = p.matcher(input);
        String s;

        out("");
        out("---------------    replaceAll    ---------------");
        out("Regular Expression: " + regex);
        out("Target string: " + input.toString());
        s = m.replaceAll(replacement);
        out("replaceAll: " + s);
    }

    private static void appendReplacement(String regex, CharSequence input, String replacement, boolean appendTail) {
        appendReplacement(regex, input, replacement, appendTail, 0);
    }

    private static void appendReplacement(String regex, CharSequence input, String replacement, boolean appendTail, int flags) {

        out("");
        out("---------------    appendReplacement    ---------------");
        out("Regular Expression: " + regex);
        out("Target string: " + input.toString());
        StringBuffer strBuf = new StringBuffer();
        Pattern p = getPattern(regex, flags);
        Matcher m = p.matcher(input);
        while (m.find()) {
            m.appendReplacement(strBuf, replacement);
        }
        if (appendTail)
            m.appendTail(strBuf);
        out(strBuf);
    }

    private static void split(String regex, CharSequence input, int limit) {
        split(regex, input, limit, 0);
    }

    private static void split(String regex, CharSequence input, int limit, int flags) {

        out("");
        out("---------------    split    ---------------");
        out("Regular Expression: " + regex);
        out("Target string: " + input.toString());
        out("limit: " + limit);
        String[] a;
        Pattern pattern = getPattern(regex);

        a = pattern.split(input, limit);
        out(a);
    }

    private static void out(StringBuffer strBuf) {
        out(strBuf.toString());
    }

    private static void out(String[] a) {
        out("size: " + a.length + " {" + String.join(",", a) + "}");
    }

    private static void out(int i) {
        out(String.valueOf(i));
    }

    private static void out(boolean bool) {
        out(String.valueOf(bool));
    }

    private static void out(String str) {
        System.out.println(str);
    }

    private static Pattern getPattern(String regex) {
        return getPattern(regex, 0);
    }

    private static Pattern getPattern(String regex, int flags) {
        return Pattern.compile(regex, flags);
    }
}