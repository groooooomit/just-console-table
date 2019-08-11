package just.console.table;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * 行
 */
public interface Row {

    /**
     * 获取单元格数量
     *
     * @return 返回单元格数量
     */
    int getCellCount();

    /**
     * 获取行高
     *
     * @return 返回单元格中层数最大的单元格的层数
     */
    int getHeight();

    /**
     * 获取指定列的单元格的宽度
     *
     * @param column 指定列
     * @return 返回单元格显示宽度
     */
    int getCellWidth(int column);

    /**
     * 打印
     *
     * @param floor        层数
     * @param columnWidths 列宽映射
     */
    @NonNull
    String string(int floor, List<Integer> columnWidths);
}
