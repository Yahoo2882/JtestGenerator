package jtg.generator;

import java.lang.reflect.Method;

public class StringGenerator extends TypeKnownGenerator {

    private static StringBuilder stringBuilder = new StringBuilder();

    private static IntGenerator lengthGenerator = new IntGenerator();

    private static CharGenerator charGenerator = new CharGenerator();

    private final int minLength = 6;
    private final int maxLength = 20;

    /**
     * 长度 6-20 的 Unicode 字符串
     *
     * @return 示例
     * 쀇윒?ဍ▥謁䕠?뭧脾韙ၸꃮ噑ﴹᐃ衚曲
     * 茄挣Ĕ崉毡給䄈答缹≷腧䣊ύ
     * 䕏횕極㴲䴘ꑲ谁檾榨᩸Ό駇꣠䙄੹ᢜ➁
     */
    @Override
    public String generate() {
        String string = null;
        try {
            string = generateRandomOrGivenLengthString(
                    -1,
                    charGenerator.getClass().getMethod("generate"));
        } catch (NoSuchMethodException exception) {
            System.err.println(exception.toString());
        }
        return string;
    }

    /**
     * 生成只包含英文字母和数字的字符串
     *
     * @param length
     * @return 示例
     * 2UKWvmss54XdxwS
     * J8gkq2Qj81DbBY
     * y8yafUd3sU1QcxI
     */
    public String generateReadableString(int length) {
        String string = null;
        try {
            string = generateRandomOrGivenLengthString(
                    length,
                    charGenerator.getClass().getMethod("generateAlphaAndNumber"));
        } catch (NoSuchMethodException exception) {
            System.err.println(exception.toString());
        }
        return string;
    }

    /**
     * 工具函数，用于协助 generate 系列方法
     *
     * @param length == -1 随机生成 || >= 0 按照给定长度生成
     * @param wayOfGenerateChar 产生字符的方法
     * @return
     */
    private String generateRandomOrGivenLengthString(int length, Method wayOfGenerateChar) {
        int currLength = length;
        if (-1 == currLength) {
            currLength = lengthGenerateFromRange(minLength, maxLength);
        } else {
            currLength = length;
        }
        stringBuilder.delete(0, stringBuilder.length());
        try {
            for (int i = 0; i < currLength; ++i) {
                stringBuilder.append(wayOfGenerateChar.invoke(charGenerator));
            }
        } catch (ReflectiveOperationException exception) {
            System.err.println(exception.toString());
        }
        return stringBuilder.toString();
    }

    private int lengthGenerateFromRange(int lowerBound, int upperBound) {
        return (int)randomIntegerFromRange(lowerBound, upperBound);
    }
}
