package just.console.table;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DefaultCell implements Cell {

    /**
     * 用来占空位的空行
     */
    private static final Line EMPTY_LINE = new DefaultLine("", Gravity.LEFT);

    /**
     * 单元格内容的行集合
     */
    private final List<Line> lines;

    /**
     * 水平偏向
     */
    private final Gravity gravity;

    /**
     * 单元格宽度
     */
    private final int width;

    private DefaultCell(@Nullable String content, @Nullable Gravity gravity) {
        this.gravity = null != gravity ? gravity : Gravity.LEFT;
        this.lines = parseToLines(content, gravity);
        this.width = findMaxShowLength(this.lines);
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.lines.size();
    }

    @NonNull
    @Override
    public String print(int floor, int allowMaxWidth) {
        /* 获取当前层的行内容，如果没有就空处理. */
        final Line line = getLineOrEmpty(this.lines, floor);

        /* 计算是否需要补充空格*/
        final int moreCount = allowMaxWidth - line.getShowLength();

        final String linePrint = line.string();

        /* 如果需要补充空格，按 gravity 进行空格填充. */
        if (moreCount == 0) {
            return linePrint;
        } else {
            switch (this.gravity) {
                case LEFT:
                    return printLeft(linePrint, moreCount);
                case CENTER:
                    return printCenter(linePrint, moreCount);
                case RIGHT:
                    return printRight(linePrint, moreCount);
                default:/* 默认居左对齐*/
                    return printLeft(linePrint, moreCount);
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * 提取文本内容的每一行到集合中
     *
     * @param content 原始文本内容
     * @param gravity 水平对齐
     * @return 返回文本行集合
     */
    @NonNull
    private static List<Line> parseToLines(@Nullable String content, Gravity gravity) {
        final String[] strings = String.valueOf(content).split("\n");
        final List<Line> lines = new ArrayList<>(strings.length);
        for (String str : strings) {
            lines.add(new DefaultLine(str, gravity));
        }
        return lines;
    }

    /**
     * 找出最宽行
     *
     * @param lines 行集合
     * @return 返回最宽文本行的显示长度
     */
    private static int findMaxShowLength(@NonNull List<Line> lines) {
        if (lines.isEmpty()) {
            return 0;
        } else {
            return Collections.max(lines, (o1, o2) -> Integer.compare(o1.getShowLength(), o2.getShowLength())).getShowLength();
        }
    }

    /**
     * 获取对应层的文本行
     *
     * @param lines 行集合
     * @param floor 层数
     * @return 如果对应层没有行，返回 EMPTY_LINE
     */
    @NonNull
    private static Line getLineOrEmpty(@Nullable List<Line> lines, int floor) {
        if (null == lines) {
            return EMPTY_LINE;
        } else {
            if (floor >= lines.size()) {
                return EMPTY_LINE;
            } else {
                return lines.get(floor);
            }
        }
    }

    /**
     * 在内容右边补充用于对齐的半角空格
     *
     * @param content   文本内容
     * @param moreCount 要补充的半角空格的数量
     * @return 返回添加空格的文本内容
     */
    @NonNull
    private static String printLeft(@NonNull String content, int moreCount) {
        /* 拼接文本内容. */
        final StringBuilder stringBuilder = new StringBuilder(content);

        /* 拼接空格. */
        for (int i = 0; i < moreCount; i++) {
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    /**
     * 在内容左边补充用于对齐的半角空格
     *
     * @param content   文本内容
     * @param moreCount 要补充的半角空格的数量
     * @return 返回添加空格的文本内容
     */
    @NonNull
    private static String printRight(@NonNull String content, int moreCount) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < moreCount; i++) {
            stringBuilder.append(" ");
        }
        return stringBuilder.append(content).toString();
    }

    /**
     * 在内容左右两边补充用于对齐的半角空格
     *
     * @param content   文本内容
     * @param moreCount 要补充的半角空格的数量
     * @return 返回添加空格的文本内容
     */
    @NonNull
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

    ///////////////////////////////////////////////////////////////////////////

    public static <T> DefaultCell of(@Nullable T content) {
        return of(content, Cell.Gravity.LEFT);
    }

    public static <T> DefaultCell of(@Nullable T content, Cell.Gravity gravity) {
        return new DefaultCell(String.valueOf(content), gravity);
    }
}
