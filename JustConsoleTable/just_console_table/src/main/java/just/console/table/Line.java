package just.console.table;

import androidx.annotation.NonNull;

/**
 * 单元格里每一层内容
 */
interface Line {

    /**
     * 获取显示长度
     *
     * @return 以一个半角字符的打印宽度为单位，全角字符的显示长度为半角字符的 1.875 倍
     */
    int getShowLength();

    /**
     * 转换为待打印的字符串
     *
     * @return 待打印的字符串
     */
    @NonNull
    String string();
}
