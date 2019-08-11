package just.console.table;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class DefaultRow implements Row {

    /**
     * 默认单元格左右缩进一个空格
     */
    private static final String DEFAULT_PADDING = " ";

    /**
     * 分割线样式
     */
    private static final String DEFAULT_DIVIDER = "|";

    /**
     * 用于补充空缺的单元格
     */
    private static final Cell NULL_CELL = DefaultCell.of(null);

    /**
     * 所在 table 引用
     */
    private final Table table;

    /**
     * 当前行的单元格集合
     */
    private final List<Cell> cells = new LinkedList<>();

    public DefaultRow(Table table, List<Cell> cells) {
        this.table = table;
        this.cells.addAll(cells);
    }

    /**
     * 当前行的单元格数量
     */
    @Override
    public int getCellCount() {
        return this.cells.size();
    }

    @Override
    public int getHeight() {
        return findMaxHeight(this.cells);
    }

    @Override
    public int getCellWidth(int index) {
        return getCellAt(index).getWidth();
    }

    /**
     * 打印行
     *
     * @param floor       行的第几层
     * @param columnWidth 每个单元格宽度要求
     */
    @NonNull
    public String string(int floor, List<Integer> columnWidth) {
        /* 计算列宽*/
        final int columnCount = columnWidth.size();

        /* 计算本行的高度（本行最大cell 的高度），如果有多行，那么要进行多行绘制. */
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DEFAULT_DIVIDER);
        for (int index = 0; index < columnCount; index++) {
            final int width = columnWidth.get(index);
            final Cell cell = getCellAt(index);
//            if (!NULL_CELL.equals(cell)) {
                stringBuilder
                        .append(DEFAULT_PADDING) // padding
                        .append(cell.print(floor, width))
                        .append(DEFAULT_PADDING)
                        .append(DEFAULT_DIVIDER);
//            }
        }
        return stringBuilder.toString();
    }

    /**
     * 获取指定列的单元格，如果不存在，返回一个空的单元格
     *
     * @param index 指定列
     * @return 单元格
     */
    @NonNull
    private Cell getCellAt(int index) {
        if (index >= cells.size()) {
            return NULL_CELL;
        } else {
            return cells.get(index);
        }
    }

    ///////////////////////////////////////////////////////////////////////////

    public DefaultRow row(List<Cell> cells) {
        return this.table.row(cells);
    }

    public DefaultRow row() {
        return this.table.row();
    }


    public DefaultRow cell(Cell cell) {
        this.cells.add(cell);
        return this;
    }

    public <T> DefaultRow cell(T value) {
        this.cells.add(DefaultCell.of(value));
        return this;
    }

    public Table end() {
        return this.table;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * 当前行的单元格中行数最大的
     *
     * @param cells 单元格集合
     * @return 返回集合中层数最大的单元格的层数
     */
    private static int findMaxHeight(@NonNull List<Cell> cells) {
        if (cells.isEmpty()) {
            return 0;
        } else {
            return Collections.max(cells, (o1, o2) -> Integer.compare(o1.getHeight(), o2.getHeight())).getHeight();
        }
    }


}
