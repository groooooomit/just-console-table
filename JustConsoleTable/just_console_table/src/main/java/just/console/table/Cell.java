package just.console.table;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * 支持中文
 * <p>
 * 支持制表符
 */
public class Cell {

    /**
     * 对齐策略
     */
    public enum Gravity {
        /**
         * 居左对齐
         */
        LEFT,
        /**
         * 居中对齐
         */
        CENTER,
        /**
         * 居右对齐
         */
        RIGHT,
    }

    /**
     * 单字节字符和多字节字符在 IDEA console 中的字体长度比例
     */
    private static final float CHAR_FACTORY = 1.875f;

    /**
     * 制表符的最大长度为 4
     */
    private static final int TAB_CHAR_LENGTH = 4;

    /**
     * 单元格内容的行集合
     */
    private final List<String> lines;

    /**
     * 左右行中字数最多行的字符数量
     */
    private final int maxLineWidth;

    /**
     * 内容对齐方式
     */
    private final Gravity gravity;

    public Cell(@Nullable String content) {
        this(content, Gravity.LEFT);
    }

    public Cell(@Nullable String content, @NonNull Gravity gravity) {
        this.lines = Arrays.asList(String
                .valueOf(content) // null 处理
                .split("\n"));
        this.maxLineWidth = findMaxLineWidth(this.lines);
        this.gravity = gravity;
    }

    /**
     * 当前内容中行的最大宽度
     */
    public int getMaxLineWidth() {
        return maxLineWidth;
    }

    public int getLineCount() {
        return this.lines.size();
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * 打印
     *
     * @param height   第几行
     * @param maxWidth 本行单元格允许的最大宽度
     */
    public String print(int height, int maxWidth) {
        /* 获取当前 height 的行内容，如果没有就空处理. */
        final String lineToPrint = getLineOrEmpty(height);

        /* 计算是否需要补充空格*/
        final int moreCount = maxWidth - calcShowLengthWithTab(lineToPrint);

        /* 如果需要补充空格，按 gravity 进行空格填充. */
        if (moreCount > 0) {
            switch (this.gravity) {
                case LEFT:
                    return printLeft(lineToPrint, moreCount);
                case CENTER:
                    return printCenter(lineToPrint, moreCount);
                case RIGHT:
                    return printRight(lineToPrint, moreCount);
                default:/* 默认居左对齐*/
                    return printLeft(lineToPrint, moreCount);
            }
        } else {
            return lineToPrint;
        }
    }

    private String getLineOrEmpty(int height) {
        if (height >= lines.size()) {
            return "";
        } else {
            return lines.get(height);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // static
    ///////////////////////////////////////////////////////////////////////////

    private static String printLeft(@NonNull String content, int moreCount) {
        final StringBuilder stringBuilder = new StringBuilder(content);
        for (int i = 0; i < moreCount; i++) {
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    private static String printRight(@NonNull String content, int moreCount) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < moreCount; i++) {
            stringBuilder.append(" ");
        }
        return stringBuilder.append(content).toString();
    }

    private static String printCenter(@NonNull String content, int moreCount) {
        final int tail = moreCount % 2;
        int halfPos;
        if (tail == 0) {
            halfPos = moreCount / 2;
        } else {
            halfPos = moreCount / 2 + 1;
        }
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < halfPos; i++) {
            stringBuilder.append(" ");
        }
        stringBuilder.append(content);
        final int anotherHalf = moreCount - halfPos;
        for (int i = 0; i < anotherHalf; i++) {
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    /**
     * 计算出最大行宽
     */
    private static int findMaxLineWidth(List<String> lines) {
        if (null == lines || lines.isEmpty()) {
            return 0;
        }
        int maxLineWidth = 0;
        for (String line : lines) {
            maxLineWidth = Math.max(calcShowLengthWithTab(line), maxLineWidth);
        }
        return maxLineWidth;
    }

    /**
     * 计算包含制表符的字符串的显示长度
     */
    private static int calcShowLengthWithTab(@NonNull String str) {
        final int originLength = calcShowLength(str);
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
        return tLength + originLength;
    }


    /**
     * 计算字符串显示长度
     */
    private static int calcShowLength(@NonNull String str) {
        final int charLength = str.length();
        final int byteLength = str.getBytes().length;
        if (charLength == byteLength) {
            return charLength;
        } else {

            // todo 特殊的不可见字符需要单独统计

            // 计算双字节数量
            final int multiCharCount = (byteLength - charLength) / 2;

            // 计算普通字符数量
            final int normalCount = charLength - multiCharCount;
            return Math.round(multiCharCount * CHAR_FACTORY) + normalCount;
        }
    }

    public static Cell of(@Nullable Object content) {
        return of(content, Gravity.LEFT);
    }

    public static Cell of(@Nullable Object content, Gravity gravity) {
        return new Cell(String.valueOf(content), gravity);
    }
}
