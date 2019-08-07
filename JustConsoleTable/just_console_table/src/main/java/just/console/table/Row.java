package just.console.table;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Row {

    /**
     * 默认左右缩进一个空格
     */
    public static final String DEFAULT_PADDING = " ";

    /**
     * 分割线样式
     */
    public static final String DEFAULT_DIVIDER = "|";

    /**
     * 用于补充空缺的单元格
     */
    private static final Cell DEFAULT = new Cell(null);

    /**
     * 当前行的单元格集合
     */
    private final List<Cell> cells = new LinkedList<>();

    /**
     * 缩进的空格数
     */
    private String padding = DEFAULT_PADDING;

    /**
     * 分割线样式
     */
    private String divider = DEFAULT_DIVIDER;

    public void setPadding(String padding) {
        this.padding = padding;
    }

    public void setDivider(String divider) {
        this.divider = divider;
    }

    /**
     * 添加单元格
     */
    public Row addCell(Cell... cells) {
        this.cells.addAll(Arrays.asList(cells));
        return this;
    }

    public Row addCell(Object... cells) {
        for (Object cell : cells) {
            this.cells.add(Cell.of(cell));
        }
        return this;
    }

    /**
     * 当前行的单元格数量
     */
    public int getCellCount() {
        return this.cells.size();
    }

    /**
     * 当前行的单元格中行数最大的
     */
    public int findMaxCellHeight() {
        return Collections.max(this.cells, (o1, o2) -> Integer.compare(o1.getLineCount(), o2.getLineCount())).getLineCount();
    }

    /**
     * 获取指定位置的单元格的最大行数
     */
    public int getCellWidth(int index) {
        final Cell cell = getCellAt(index);
        if (null != cell) {
            return cell.getMaxLineWidth();
        } else {
            return 0;
        }
    }

    /**
     * 打印行
     *
     * @param heightIndex 行的第几层
     * @param columnWidth 每个单元格宽度要求
     */
    public String print(int heightIndex, List<Integer> columnWidth) {
        /* 计算列宽*/
        final int columnCount = columnWidth.size();
        // 计算本行的高度（本行最大cell 的高度），如果有多行，那么要进行多行绘制

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(divider);
        for (int index = 0; index < columnCount; index++) {
            final int width = columnWidth.get(index);
            final Cell cell = getCellAt(index);

            /* 添加左缩进，注意 padding 必须是单字节字符. */
            stringBuilder
                    .append(padding) // margin、padding
                    .append(cell.print(heightIndex, width))
                    .append(padding)
                    .append(divider);
        }
        return stringBuilder.toString();
    }

    private Cell getCellAt(int index) {
        if (index >= cells.size()) {
            return DEFAULT;
        } else {
            return cells.get(index);
        }
    }


}
