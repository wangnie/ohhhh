package com;

import org.springframework.aop.scope.ScopedProxyUtils;
import sun.java2d.pipe.SpanIterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Test {

    static String CHN_NUMBER[] = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    static String CHN_UNIT[] = {"", "十", "百", "千"};          //权位
    static String CHN_UNIT_SECTION[] = {"", "万", "亿", "万亿"}; //节权位

    public static void main(String[] args) {


        List<Man> mans = new ArrayList<>();
        mans.add(new Man("002","李四", Arrays.asList(new Card("招商银行","6225800002"),new Card("建设银行","6227035248"))));
        mans.add(new Man("003","王五",Arrays.asList(new Card("建设银行","6227056547"),new Card("中国银行","6013832547"),new Card("民生银行","4074058542"))));
        mans.add(new Man("004","赵六",Arrays.asList(new Card("工商银行","9558832458"),new Card("工商银行","9558832547"),new Card("建设银行","6227032578"))));
        mans.add(new Man("005","孙七",Arrays.asList(new Card("中国银行","6013825847"),new Card("农业银行","6228836547"),new Card("招商银行","6225014582"))));
        mans.add(new Man("006","张三",Arrays.asList(new Card("工商银行","9558832587"),new Card("交通银行","6222814578"),new Card("工商银行","9558865427"))));
        mans.add(new Man("007","张三",Arrays.asList(new Card("招商银行","1158832587"),new Card("交通银行","6344814578"),new Card("工商银行","2131133133"))));

        List<Card> collect = mans.stream().filter(man -> "张三".equals(man.getName())).flatMap(man -> man.getCards().stream()).collect(Collectors.toList());
        System.out.println(collect);
        System.out.println(collect.size());
        List<List<Card>> collect1 = mans.stream().map(man -> man.getCards()).collect(Collectors.toList());
        System.out.println(collect1);
        System.out.println(collect1.size());

    }


    /**
     * 阿拉伯数字转换为中文数字的核心算法实现。
     *
     * @param num num为需要转换为中文数字的阿拉伯数字，是无符号的整形数
     * @return
     */
    public static String NumberToChn(String num1) {
        Integer num;
        try {
            num = Integer.parseInt(num1);
        } catch (Exception e) {
            return "";
        }

        StringBuffer returnStr = new StringBuffer();
        Boolean needZero = false;
        int pos = 0;           //节权位的位置
        if (num == 0) {
            //如果num为0，进行特殊处理。
            returnStr.insert(0, CHN_NUMBER[0]);
        }
        while (num > 0) {
            int section = num % 10000;
            if (needZero) {
                returnStr.insert(0, CHN_NUMBER[0]);
            }
            String sectionToChn = SectionNumToChn(section);
            //判断是否需要节权位
            sectionToChn += (section != 0) ? CHN_UNIT_SECTION[pos] : CHN_UNIT_SECTION[0];
            returnStr.insert(0, sectionToChn);
            needZero = ((section < 1000 && section > 0) ? true : false); //判断section中的千位上是不是为零，若为零应该添加一个零。
            pos++;
            num = num / 10000;
        }
        return returnStr.toString();
    }


    /**
     * 将四位的section转换为中文数字
     *
     * @param section
     * @return
     */
    public static String SectionNumToChn(int section) {
        StringBuffer returnStr = new StringBuffer();
        int unitPos = 0;       //节权位的位置编号，0-3依次为个十百千;

        Boolean zero = true;
        while (section > 0) {

            int v = (section % 10);
            if (v == 0) {
                if ((section == 0) || !zero) {
                    zero = true; /*需要补0，zero的作用是确保对连续的多个0，只补一个中文零*/
                    //chnStr.insert(0, chnNumChar[v]);
                    returnStr.insert(0, CHN_NUMBER[v]);
                }
            } else {
                zero = false; //至少有一个数字不是0
                StringBuffer tempStr = new StringBuffer(CHN_NUMBER[v]);//数字v所对应的中文数字
                tempStr.append(CHN_UNIT[unitPos]);  //数字v所对应的中文权位
                returnStr.insert(0, tempStr);
            }
            unitPos++; //移位
            section = section / 10;
        }
        return returnStr.toString();
    }

    /**
     * 中文转换成阿拉伯数字，中文字符串除了包括0-9的中文汉字，还包括十，百，千，万等权位。
     * 此处是完成对这些权位的类型定义。
     * name是指这些权位的汉字字符串。
     * value是指权位多对应的数值的大小。诸如：十对应的值的大小为10，百对应为100等
     * secUnit若为true，代表该权位为节权位，即万，亿，万亿等
     */
    public static class Chn_Name_value {
        String name;
        int value;
        Boolean secUnit;

        public Chn_Name_value(String name, int value, Boolean secUnit) {
            this.name = name;
            this.value = value;
            this.secUnit = secUnit;
        }
    }

    static Chn_Name_value chnNameValue[] = {
            new Chn_Name_value("十", 10, false),
            new Chn_Name_value("百", 100, false),
            new Chn_Name_value("千", 1000, false),
            new Chn_Name_value("万", 10000, true),
            new Chn_Name_value("亿", 100000000, true)
    };

    /**
     * 返回中文数字汉字所对应的阿拉伯数字，若str不为中文数字，则返回-1
     *
     * @param str
     * @return
     */
    public static int ChnNumToValue(String str) {
        for (int i = 0; i < CHN_NUMBER.length; i++) {
            if (str.equals(CHN_NUMBER[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 返回中文汉字权位在chnNameValue数组中所对应的索引号，若不为中文汉字权位，则返回-1
     *
     * @param str
     * @return
     */
    public static int ChnUnitToValue(String str) {
        for (int i = 0; i < chnNameValue.length; i++) {
            if (str.equals(chnNameValue[i].name)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 返回中文数字字符串所对应的int类型的阿拉伯数字
     *
     * @param str
     * @return
     */
    public static Integer ChnStringToNumber(String str) {
        int returnNumber = 0;
        int section = 0;
        int pos = 0;
        int number = 0;
        while (pos < str.length()) {
            int num = ChnNumToValue(str.substring(pos, pos + 1));
            //若num>=0，代表该位置（pos），所对应的是数字不是权位。若小于0，则表示为权位
            if (num >= 0) {
                number = num;
                pos++;
                //pos是最好一位，直接将number加入到section中。
                if (pos >= str.length()) {
                    section += number;
                    returnNumber += section;
                    break;
                }
            } else {
                int chnNameValueIndex = ChnUnitToValue(str.substring(pos, pos + 1));
                //chnNameValue[chnNameValueIndex].secUnit==true，表示该位置所对应的权位是节权位，
                try {
                    if (chnNameValue[chnNameValueIndex].secUnit) {
                        section = (section + number) * chnNameValue[chnNameValueIndex].value;
                        returnNumber += section;
                        section = 0;
                    } else {
                        section += number * chnNameValue[chnNameValueIndex].value;
                    }
                    pos++;
                    number = 0;
                } catch (Exception e) {
                    return -1;
                }

                if (pos >= str.length()) {
                    returnNumber += section;
                    break;
                }
            }
        }
        return returnNumber;
    }





}
