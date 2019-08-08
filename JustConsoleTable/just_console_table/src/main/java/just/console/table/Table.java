package just.console.table;

import java.util.*;

public class Table {

    private static final String DEFAULT_TABLE_MARGIN = "  ";
    private final String desc;

    /**
     * 行集合
     */
    private final List<Row> rows = new LinkedList<>();

    private Table(String desc) {
        this.desc = null != desc ? desc : "Anonymous";
    }

    /**
     * 添加行
     */
    public Table row(Cell... cells) {
        this.rows.add(new Row().addCell(cells));
        return this;
    }

    /**
     * 添加行
     */
    public Table row(Object... cells) {
        this.rows.add(new Row().addCell(cells));
        return this;
    }

    // 先计算、再打印
    public String print() {

        /* 计算最大列数. */
        final int columnCount = Collections.max(rows, (o1, o2) -> Integer.compare(o1.getCellCount(), o2.getCellCount())).getCellCount();

        /* 计算列宽. （每一列最大单元格的宽度相加）*/
        final List<Integer> columnWidths = new ArrayList<>(columnCount);

        for (int i = 0; i < columnCount; i++) {
            final int index = i;
            final int maxColumnWidth = Collections.max(rows, (o1, o2) -> Integer.compare(o1.getCellWidth(index), o2.getCellWidth(index))).getCellWidth(index);
            columnWidths.add(maxColumnWidth);
        }

        final StringBuilder stringBuilder = new StringBuilder("\n \n")
                .append(DEFAULT_TABLE_MARGIN)
                .append("@ ")
                .append(desc)
                .append("\n");

        // 计算总宽度
        stringBuilder.append(DEFAULT_TABLE_MARGIN);
        printBorder(stringBuilder, columnWidths);

        for (Row row : rows) {
            final int cellHeight = row.findMaxCellHeight();
            for (int i = 0; i < cellHeight; i++) {
                final String rowPrint = row.print(i, columnWidths);
                stringBuilder.append(DEFAULT_TABLE_MARGIN);
                stringBuilder.append(rowPrint);
                stringBuilder.append("\n");
            }

            stringBuilder.append(DEFAULT_TABLE_MARGIN);
            printBorder(stringBuilder, columnWidths);
        }
        // 计算出最大单元格宽度、最大行的宽度
        // measure
        return stringBuilder.toString();
    }

    private static void printBorder(StringBuilder stringBuilder, List<Integer> columnWidths) {
        stringBuilder.append("+");
        for (int columnWidth : columnWidths) {
            for (int i = 0; i < columnWidth + 2; i++) {// +2 是为了加上 margin 的空格
                stringBuilder.append("-");
            }
            stringBuilder.append("+");
        }
        stringBuilder.append("\n");
    }


    public static Table create(String desc) {
        return new Table(desc);
    }

    public static <E> Table of(String name, List<E> list) {
        final Table table = Table.create(name);
        if (null == list) {
            table.row(Cell.of(null));
        } else {
            final int size = list.size();
            for (int i = 0; i < size; i++) {
                table.row(i, list.get(i));
            }
        }
        return table;
    }

    public static <K, V> Table of(String name, Map<K, V> map) {
        final Table table = Table.create(name);
        if (null == map) {
            table.row(Cell.of(null));
        } else {
            for (Map.Entry<K, V> entry : map.entrySet()) {
                table.row(entry.getKey(), entry.getValue());
            }
        }
        return table;
    }

}
