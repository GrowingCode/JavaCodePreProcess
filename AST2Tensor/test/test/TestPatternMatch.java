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
      // group(0)��group()���᷵������ƥ����ַ�������ȫƥ�䣩��group(i)��᷵�������iƥ����ַ�
      // �������ֻ��һ������
      System.out.println(patt + " matches \"" + m.group(0) + "\" in \"" + line + "\"");
      System.out.println("start:" + m.start() + " end:" + m.end());
/*
�����
  
Q[^u]\d+\. matches "QT300." in "Order QT300. Now! QT400."
start:6 end:12
Q[^u]\d+\. matches "QT400." in "Order QT300. Now! QT400."
start:18 end:24
*/
    }
  }
}