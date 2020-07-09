package sample;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author DeMon
 * Created on 2020/5/19.
 * E-mail 757454343@qq.com
 * Desc:
 */
public class Utils {

    /**
     * 利用正则表达式判断字符串是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

}
