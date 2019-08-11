package just.console.table;

import androidx.annotation.NonNull;

/**
 * 单元格
 */
public interface Cell {

    /**
     * 获取单元格宽度
     *
     * @return 单元格宽度是单元格内容中宽度最大 Line 的宽度
     */
    int getWidth();

    /**
     * 获取最大层数
     *
     * @return 单元格的层数即单元格文本的行数
     */
    int getHeight();

    /**
     * 转换为待打印字符串
     *
     * @param floor         第几层
     * @param allowMaxWidth 允许的最大宽度
     */
    @NonNull
    String print(int floor, int allowMaxWidth);

    /**
     * 对齐策略
     */
    enum Gravity {
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

}
