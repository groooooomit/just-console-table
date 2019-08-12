package just.console.table;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class Table {

    private static final String DEFAULT_TABLE_MARGIN = "  ";
    private static final int DEFAULT_CELL_MARGIN = 1;
    private static final String NEXT_LINE = "\r\n";

    /**
     * 表名
     */
    @NonNull
    private final String name;

    /**
     * 行集合
     */
    @NonNull
    private final List<Row> rows = new LinkedList<>();

    private Table(String name) {
        this.name = null != name ? name : "Nameless";
    }

    public String string() {

        /* 计算列宽. （每一列最大单元格的宽度相加）*/
        final List<Integer> columnWidths = getColumnWidth(this.rows);

        /* 打印表头. */
        final StringBuilder stringBuilder = new StringBuilder()
                .append(" ")
                .append(NEXT_LINE)
                .append(NEXT_LINE)
                .append(DEFAULT_TABLE_MARGIN)
                .append("@ ")
                .append(name)
                .append(NEXT_LINE);

        /* 打印顶边界. */
        stringBuilder.append(DEFAULT_TABLE_MARGIN);
        appendTopBorder(stringBuilder, columnWidths, DEFAULT_CELL_MARGIN);

        /* 遍历打印每一行. */
        for (Row row : rows) {

            /* 一层一层打印. */
            final int height = row.getHeight();
            for (int i = 0; i < height; i++) {
                stringBuilder
                        .append(DEFAULT_TABLE_MARGIN)
                        .append(row.string(i, columnWidths))
                        .append(NEXT_LINE);
            }

            /* 打印底边界. */
            stringBuilder.append(DEFAULT_TABLE_MARGIN);
            appendBottomBorder(stringBuilder, columnWidths, DEFAULT_CELL_MARGIN);
        }
        stringBuilder.append(" ");
        return stringBuilder.toString();
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * 计算列宽
     */
    @NonNull
    private static List<Integer> getColumnWidth(@Nullable List<Row> rows) {
        if (null == rows || rows.isEmpty()) {
            return Collections.emptyList();
        } else {
            /* 计算列数. */
            int columnCount = Collections.max(rows, (o1, o2) -> Integer.compare(o1.getCellCount(), o2.getCellCount())).getCellCount();
            final List<Integer> columnWidths = new ArrayList<>(columnCount);
            for (int i = 0; i < columnCount; i++) {
                final int index = i;
                final int maxColumnWidth = Collections.max(rows, (o1, o2) -> Integer.compare(o1.getCellWidth(index), o2.getCellWidth(index))).getCellWidth(index);
                columnWidths.add(maxColumnWidth);
            }
            return columnWidths;
        }
    }

    /**
     * 打印边线
     */
    private static void appendTopBorder(StringBuilder stringBuilder, List<Integer> columnWidths, int padding) {
        stringBuilder.append("+");
        for (int columnWidth : columnWidths) {
            for (int i = 0; i < columnWidth + 2 * padding; i++) {
                stringBuilder.append("-");
            }
            stringBuilder.append("+");
        }
        stringBuilder.append(NEXT_LINE);
    }

    /**
     * 打印边线
     */
    private static void appendBottomBorder(StringBuilder stringBuilder, List<Integer> columnWidths, int padding) {
        stringBuilder.append("+");
        for (int columnWidth : columnWidths) {
            for (int i = 0; i < columnWidth + 2 * padding; i++) {
                stringBuilder.append("-");
            }
            stringBuilder.append("+");
        }
        stringBuilder.append(NEXT_LINE);
    }

    public DefaultRow row(Cell... cells) {
        return row(Arrays.asList(cells));
    }

    public DefaultRow row(String... cells) {
        List<Cell> cellList = new ArrayList<>(cells.length);
        for (String cell : cells) {
            cellList.add(DefaultCell.of(cell));
        }
        return row(cellList);
    }

    public DefaultRow row(List<Cell> cells) {
        final DefaultRow row = new DefaultRow(this, cells);
        this.rows.add(row);
        return row;
    }

    public DefaultRow row() {
        final DefaultRow row = new DefaultRow(this, new LinkedList<>());
        this.rows.add(row);
        return row;
    }

    ///////////////////////////////////////////////////////////////////////////

    public static Table create(String desc) {
        return new Table(desc);
    }

    /**
     * 打印 list，垂直排列
     *
     * @param name 表名称
     * @param list 要打印的 list
     * @param <E>  元素泛型
     * @return 返回描述 list 元素的表
     */
    public static <E> Table list(String name, @Nullable List<E> list) {
        final Table table = Table.create(name);
        if (null == list) {
            table.row(DefaultCell.of(null));
        } else {
            final int size = list.size();
            for (int i = 0; i < size; i++) {
                table.row(DefaultCell.of(i), DefaultCell.of(list.get(i)));
            }
        }
        return table;
    }


    /**
     * 打印 list，垂直排列
     *
     * @param name 表名称
     * @param list 要打印的 list
     * @param <E>  元素泛型
     * @return 返回描述 list 元素的表
     */
    public static <E> Table list(String name, @Nullable List<E> list, @NonNull Function<E, List<Cell>> mapper) {
        final Table table = Table.create(name);
        if (null == list) {
            table.row(DefaultCell.of(null));
        } else {
            final int size = list.size();
            for (int i = 0; i < size; i++) {
                final List<Cell> cells = new ArrayList<>();
                cells.add(DefaultCell.of(i));
                final List<Cell> parseCells = mapper.apply(list.get(i));
                if (null != parseCells) {
                    cells.addAll(parseCells);
                }
                table.row(cells);
            }
        }
        return table;
    }

    /**
     * 打印 list 垂直排列，限制最大行数
     *
     * @param name   表名称
     * @param maxRow 最大行数
     * @param list   要打印的 list
     * @param <E>    元素泛型
     * @return 返回描述 list 元素的表
     */
    public static <E> Table list(String name, int maxRow, @Nullable List<E> list) {
        if (maxRow <= 0) {
            throw new RuntimeException(" maxRow must > 0.");
        }
        final Table table = Table.create(name);
        if (null == list) {
            return table.row(DefaultCell.of(null)).end();
        } else {
            /* 元素总数. */
            final int totalCount = list.size();

            /* 总列数. */
            final int columnCount = totalCount / maxRow + (totalCount % maxRow > 0 ? 1 : 0);
            for (int i = 0; i < maxRow; i++) {
                final DefaultRow row = table.row();
                for (int j = 0; j < columnCount; j++) {
                    final int index = j * maxRow + i;
                    if (index < totalCount) {
                        row
                                .cell(DefaultCell.of(index))
                                .cell(DefaultCell.of(list.get(index)));
                    }
                }
            }
            return table;
        }
    }


    /**
     * 打印 list，水平排列
     *
     * @param name 表名称
     * @param list 要打印的 list
     * @param <E>  元素泛型
     * @return 返回描述 list 元素的表
     */
    public static <E> Table listH(String name, @Nullable List<E> list) {
        final Table table = Table.create(name);
        if (null == list) {
            table.row(DefaultCell.of(null));
        } else {
            final int size = list.size();
            final List<Cell> indexCells = new ArrayList<>(size);
            final List<Cell> valueCells = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                indexCells.add(DefaultCell.of(i));
                valueCells.add(DefaultCell.of(list.get(i)));
            }
            table.row(indexCells)
                    .row(valueCells);
        }
        return table;
    }

    /**
     * 打印 list，水平排列，限制最大列数
     *
     * @param name      表名称
     * @param maxColumn 最大列数，当达到最大列数时，进行换行打印
     * @param list      要打印的 list
     * @param <E>       元素泛型
     * @return 返回描述 list 元素的表
     */
    public static <E> Table listH(String name, int maxColumn, @Nullable List<E> list) {
        if (maxColumn <= 0) {
            throw new RuntimeException(" maxColumn must > 0.");
        }
        final Table table = Table.create(name);
        if (null == list) {
            table.row(DefaultCell.of(null));
        } else {
            /* 元素总数. */
            final int totalCount = list.size();

            /* 已处理数量. */
            int processCount = 0;

            /* 起始欧标*/
            while (processCount < totalCount) {
                final int fromIndex = processCount;
                final int toIndex = Math.min(processCount + maxColumn, totalCount);
                final List<E> sub = list.subList(fromIndex, toIndex);
                final int subSize = sub.size();
                final List<Cell> cells = new ArrayList<>(subSize);
                final List<Cell> indexes = new ArrayList<>(subSize);
                for (int i = 0; i < subSize; i++) {
                    indexes.add(DefaultCell.of(fromIndex + i));
                    cells.add(DefaultCell.of(sub.get(i)));
                }
                table.row(indexes).row(cells);
                processCount += subSize;
            }
        }
        return table;
    }

    /**
     * 打印 map
     */
    public static <K, V> Table map(String name, Map<K, V> map) {
        final Table table = Table.create(name);
        if (null == map) {
            table.row(DefaultCell.of(null));
        } else {
            for (Map.Entry<K, V> entry : map.entrySet()) {
                table.row(DefaultCell.of(entry.getKey()), DefaultCell.of(entry.getValue()));
            }
        }
        return table;
    }

    /**
     * 打印异常信息
     */
    public static Table error(String name, @NonNull Throwable throwable) {
        final StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter, true));
        return Table.create(name)
                .row(stringWriter.toString())
                .end();
    }

    private static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS E 'GMT'Z", Locale.ENGLISH);

    public static Table errorReach(String name, @NonNull Throwable throwable) {
        final StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter, true));
        return Table.create(name)
                .row("Thread", Thread.currentThread().getName())
                .row(Arrays.asList(DefaultCell.of("Time"), DefaultCell.of(DEFAULT_DATE_FORMAT.format(new Date()))))
                .row(Arrays.asList(DefaultCell.of("Type"), DefaultCell.of(throwable.getClass().getName())))
                .row(Arrays.asList(DefaultCell.of("Message"), DefaultCell.of(throwable.getMessage())))
                .row(Arrays.asList(DefaultCell.of("Stack Trace"), DefaultCell.of(stringWriter.toString())))
                .end();
    }

}
