package test;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by Frank
 */
public class TestPatternMatch {
  public static void main(String[] args) {
    String patt = "Q[^u]\\d+\\.";
    Pattern r = Pattern.compile(patt);
    String line = "Order QT300. Now! QT400.";
    Matcher m = r.matcher(line);
    while (m.find()) {
      // group(0)或group()将会返回整个匹配的字符串（完全匹配）；group(i)则会返回与分组i匹配的字符
      // 这个例子只有一个分组
      System.out.println(patt + " matches \"" + m.group(0) + "\" in \"" + line + "\"");
      System.out.println("start:" + m.start() + " end:" + m.end());
/*
输出：
  
Q[^u]\d+\. matches "QT300." in "Order QT300. Now! QT400."
start:6 end:12
Q[^u]\d+\. matches "QT400." in "Order QT300. Now! QT400."
start:18 end:24
*/
    }
  }
}