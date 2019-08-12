package just.console.table;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

// todo 支持不同风格的边界线、todo、不同字体的 Console platform

/**
 * 单元格中的文本行
 */
final class DefaultLine implements Line {

    /**
     * 全角空格
     */
    private static final String FULL_WIDTH_BLANK = "　";

    /**
     * 制表符的最大长度为 4
     */
    private static final int TAB_CHAR_LENGTH = 4;

    /**
     * 对齐后的字符串
     */
    private final String fixedValue;

    /**
     * 显示长度
     */
    private final int showLength;

    /**
     * 字体类型
     */
//    private final Font font;

    public DefaultLine(@Nullable String str, @NonNull Cell.Gravity gravity) {
        /* 全角对齐. */
        this.fixedValue = replaceTabTo4Blank(fixed(str, Font.getDefaultFont().fullCount(), gravity));
        this.showLength = calcShowLength(this.fixedValue) + calculateTabShowLength(this.fixedValue);
    }

    @Override
    public int getShowLength() {
        return this.showLength;
    }

    @NonNull
    @Override
    public String string() {
        return this.fixedValue;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * 全角字符数量对齐
     * <p>
     * 对齐是为了能够用有限的半角字符来对齐含有全角字符的字符串
     */
    @SuppressWarnings("SameParameterValue")
    private static String fixed(@Nullable String str, int base, @NonNull Cell.Gravity gravity) {
        /* 非空处理. */
        str = String.valueOf(str);

        /* 总字符数. */
        final int charCount = str.length();

        /* 总字节数. */
        final int byteCount = str.getBytes().length;

        /* 全角字符数量. */
        final int fullCharCount = (byteCount - charCount) / 2;

        /* 判断是否有全角字符。*/
        if (fullCharCount == 0) {

            /* 没有全角字符，直接返回原始字符串. */
            return str;

        } else {

            /* 有全角字符,计算需要补充的全角字符数量. */
            final int deltaFullCharCount = getNearestNumber(fullCharCount, base) - fullCharCount;

            /* 补充全角空格 */
            switch (gravity) {
                case LEFT:
                    return appendLeft(str, deltaFullCharCount);
                case RIGHT:
                    return appendRight(str, deltaFullCharCount);
                case CENTER:
                    return appendCenter(str, deltaFullCharCount);
                default:
                    return appendLeft(str, deltaFullCharCount);
            }
        }
    }

    /**
     * 将全角空格添加到内容右边
     *
     * @param str                原始内容
     * @param deltaFullCharCount 需要添加的全角空格数量
     * @return 返回拼接后的字符串
     */
    @NonNull
    private static String appendLeft(@NonNull String str, int deltaFullCharCount) {
        /* 拼接内容. */
        final StringBuilder stringBuilder = new StringBuilder(str);

        /* 拼接全角空格. */
        for (int i = 0; i < deltaFullCharCount; i++) {
            stringBuilder.append(FULL_WIDTH_BLANK);
        }
        return stringBuilder.toString();
    }

    /**
     * 将全角空格添加到内容左边
     *
     * @param str                原始内容
     * @param deltaFullCharCount 需要添加的全角空格数量
     * @return 返回拼接后的字符串
     */
    @NonNull
    private static String appendRight(@NonNull String str, int deltaFullCharCount) {
        /* 拼接全角空格. */
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < deltaFullCharCount; i++) {
            stringBuilder.append(FULL_WIDTH_BLANK);
        }

        /* 拼接内容并返回. */
        return stringBuilder.append(str).toString();
    }

    /**
     * 将全角空格添加到内容左右两边
     *
     * @param str                原始内容
     * @param deltaFullCharCount 需要添加的全角空格数量
     * @return 返回拼接后的字符串
     */
    @NonNull
    private static String appendCenter(@NonNull String str, int deltaFullCharCount) {

        /* 计算中间位置. */
        final int tail = deltaFullCharCount % 2;
        int halfPos;
        if (tail == 0) {
            halfPos = deltaFullCharCount / 2;
        } else {
            halfPos = deltaFullCharCount / 2 + 1;
        }

        /* 拼接左边全角空格. */
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < halfPos; i++) {
            stringBuilder.append(FULL_WIDTH_BLANK);
        }

        /* 拼接内容. */
        stringBuilder.append(str);

        /* 拼接右边全角空格. */
        final int anotherHalf = deltaFullCharCount - halfPos;
        for (int i = 0; i < anotherHalf; i++) {
            stringBuilder.append(FULL_WIDTH_BLANK);
        }
        return stringBuilder.toString();
    }


    /**
     * 计算最接近的倍数
     *
     * @param n    参照值
     * @param base 基准值
     * @return 如果 n = 1; base = 8; 那么返回 8
     * <p>
     * 如果 n = 5; base = 8; 那么返回 8
     * <p>
     * 如果 n = 9; base = 8; 那么返回 16
     * <p>
     * 如果 n = 18; base = 8; 那么返回 24
     * <p>
     * 即返回最小的但比 n 大的 base 的整数倍的数
     */
    private static int getNearestNumber(int n, int base) {
        final int tail = n % base;
        if (tail == 0) {// 整除，返回大的结果
            return Math.max(n, base);
        } else {
            // 不整除
            return n + base - tail;
        }
    }

    /**
     * 计算包含制表符的字符串的显示长度
     */
    private static int calculateTabShowLength(@NonNull String str) {
        int tLength = 0;
        int index = 0;
        int next = 0;
        while (index > -1) {
            index = str.indexOf('\t', next);
            // 计算长度
            if (index > -1) {
                final String substring = str.substring(next, index);
                final int realIndex = calcShowLength(substring);
                tLength += (TAB_CHAR_LENGTH - 1 - realIndex % TAB_CHAR_LENGTH);
            }
            next = index + 1;
        }
        return tLength;
    }

    /**
     * 将制表符替换为等价数量的半角空格，因为制表符的单位字体宽度和一个半角字符的字体宽度有细微的出入
     *
     * @param str 待转换的字符串
     * @return 返回替换了制表符的字符串
     */
    @NonNull
    private static String replaceTabTo4Blank(@NonNull String str) {
        final StringBuilder stringBuilder = new StringBuilder(str);
        int index = 0;// 制表符所在位置
        int next = 0;
        while (index > -1) {// 当 index 为 -1 时，表示已经遍历完所有的制表符
            index = stringBuilder.indexOf("\t", next);
            if (index > -1) {

                /* 计算制表符所在的显示位置. */
                final int realIndex = calcShowLength(stringBuilder.substring(next, index));

                /* 计算制表符所代表的半角字符串数量. */
                final int posCount = TAB_CHAR_LENGTH - 1 - realIndex % TAB_CHAR_LENGTH;

                /* 将制表符替换为对应数量的半角空格. */
                stringBuilder.replace(index, index + 1, generateBlanks(posCount));
            }

            /* 从当前处理位置往下继续查找制表符. */
            next = index + 1;
        }
        return stringBuilder.toString();
    }

    /**
     * 构造指定数量半角空格组成的字符串
     *
     * @param count 半角空格数量
     * @return 返回半角空格字符串
     */
    private static String generateBlanks(int count) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }


    /**
     * 计算字符串显示长度
     *
     * @param str 要计算的字符串
     * @return 返回字符串的显示长度
     */
    private static int calcShowLength(@NonNull String str) {
        /* 字符数量 */
        final int charLength = str.length();
        /* 字节数量 */
        final int byteLength = str.getBytes().length;
        if (charLength == byteLength) {
            return charLength;
        }
        /* 全角字符数量 */
        final int fullCharCount = (byteLength - charLength) / 2;
        /* 半角字符数量 */
        final int halfCharCount = charLength - fullCharCount;
        /* 显示长度. */
        return Math.round(fullCharCount * Font.getDefaultFont().scala()) + halfCharCount;
    }

}
