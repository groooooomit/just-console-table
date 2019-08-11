package just.console.table;

import androidx.annotation.NonNull;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testTable() {
        String table = Table.listH("老百姓", Arrays.asList("张三", "李四", "王五", "赵六", "钱七")).string();
        Map<String, String> map = new LinkedHashMap<>();
        map.put("张a三", "哈哈");
        map.put("李四dd", "嘿嘿");
        map.put("王222五", "呵呵");
        String str = Table.map("测试", map).string();
        System.out.println(table);
        System.out.println(str);
    }

    @Test
    public void testTable2() {
        Table table = Table.create("测试");
        table.row("123", "正", "正正", "正正", "正正正正正正", "正", "aaau我", "正", "正正正", "正", "正正", "正正正正正正", "66", "正正", "正正正正正正", "正", "正", "正正正");
        table.row("正正正正正正", "222", "正", "正正", "正正", "正", "正", "333", "正", "正正", "正正正正正正", "44adfa哈", "正正正", "正正正正正正", "正", "正正", "正", "正正正");
        table.row("222", "正", "正正", "正正", "正", "333", "正正正正正正", "44adfa哈", "正正正", "正正正正正正", "正正", "正", "正正正");
        String print = table.string();
        System.out.println(print);
    }

    @Test
    public void testTable4() {
        Table table = Table.create("测试");
        table.row("正正正正正正正正正正正正正正正正正正正正正正正正正正正正正正正正正正正正正正正正");
        String print = table.string();
        System.out.println(print);
    }

    @Test
    public void testTable3() {
        System.out.println("+----+----+----+----+----+----+----+----+");
        System.out.println("| 正 | 正 | 正 | 正 | 正 | 正 | 正 | 正 |");
        System.out.println("---+---+---+---+---+---+---+---+-");
        System.out.println("12341234123412341234123412341234|");
        System.out.println("  正\t  正\t  正\t  正\t  正\t  正\t  正\t  正\t|");
        System.out.println("正\t正\t正\t正\t正\t正\t正\t正\t|");
        System.out.println("哈\t聊\t三\t梦\t天\t赢\t无\t车\t|");
        System.out.println("| 正\t | 正 | 正 | 正\t| 正 | 正 | 正 | 正 |");
        System.out.println("+----+----+----+----+----+----+----+----+");
        // 如果正正好是第三的位置，即 \t 正好第四，那么就能消除对不正的尴尬
        // 误差小于一个 - ，那么使用 \t 修正
        // 满 8个格子，就补充一个空格
    }

    @Test
    public void findFactor() {
        for (int i = 0; i < 1875; i++) {
            System.out.print("-");
        }
        System.out.println();
        for (int i = 0; i < 1000; i++) {
            System.out.print("正");
        }
    }

    /*
     * +----+------------+--------------+---------------+----------------+------------------+--------------------+------------+-------------+---------------+---------------+-----------+-------+---------------+
     * | id | creator_id | creator_name | create_time   | last_editor_id | last_editor_name | last_modified_time | optimistic | customer_id | end_time      | face_group_id | name      | place | start_time    |
     * +----+------------+--------------+---------------+----------------+------------------+--------------------+------------+-------------+---------------+---------------+-----------+-------+---------------+
     * |  1 |        207 | checkin_a_2  | 1563855118533 |            207 | checkin_a_2      |      1563855118533 |          0 |          31 | 1563941451563 |           343 | 签到测试1 | 这里  | 1563855291563 |
     * +----+------------+--------------+---------------+----------------+------------------+--------------------+------------+-------------+---------------+---------------+-----------+-------+---------------+
     */
    @Test
    public void testLength() {
        for (int i = 0; i < 10; i++) {
            System.out.println("char length: " + i + ", show length: " + getLength(i) + ", float: " + getLength2(i));
        }
    }

    public static int getLength(int charWIdth) {
        return Math.round(charWIdth * 1.875f);
    }

    public static float getLength2(int charWIdth) {
        return charWIdth * 1.875f;
    }


    @Test
    public void testCharCount() {
        calcShowLength("aAbB哈哈c证l");
    }

    @Test
    public void testT() {
        System.out.println("--------------------");
        System.out.println("123\t45678901234567890");
        System.out.println("哈哈\t哈哈哈哈哈哈");// 超过 4，那么就不起作用了
        // 因为每8个凑1，所以 8 个 正 和 15 个 a 是等长的，即 15 / 8 = 1.875
        // 由于精度关系，实际用到是2，所以会绘制 16 个 -
        // 一种方式规定单元格最小宽度为 8，另一种是 使用 制表符修正，最大格子长度都是 t 的整数
        // 如果 length 长度为 1.875，那么
        // 如果有中文，都添加一个 \t ，
    }

    private static final float CHAR_FACTORY = 1.875f;

    /**
     * 计算字符串显示长度
     */
    private static int calcShowLength(@NonNull String str) {
        final int charLength = str.length();
        System.out.println("总字符数： " + charLength);
        final int byteLength = str.getBytes().length;
        System.out.println("总字节数： " + byteLength);
        if (charLength == byteLength) {
            return charLength;
        } else {

            // todo 特殊的不可见字符需要单独统计
            // todo 如果有中文存在，那么需要将其最小单元格设置为 8 的倍数，15 : 8
            // todo 每打印满 8 个，那么就增加一个 -

            // 计算双字节数量
            final int multiCharCount = (byteLength - charLength) / 2;

            System.out.println("多字节字符数量： " + multiCharCount);

            // 计算普通字符数量
            final int normalCount = charLength - multiCharCount;

            System.out.println("单字节字符数量： " + normalCount);

            // 不能直接 四舍五入，依据精度，给原先的字符串补充空格
            // 1875 a = 1000 b -> a = 1 -> b = 1.875
            // 1.875 个 - 的长度等于一个 正 的长度
            // 一个 - 的长度占了 正的 0.533333
            //  1.06666 : 1
            // 1.3 -> 1 少了 0.3
            // 1.8 -> 2 多了 0.2
            // --
            // 正
            //
            // 取一个最接近 - 的整数
            return Math.round(multiCharCount * CHAR_FACTORY) + normalCount;
        }
    }

    public static void main(String[] args) {
        //    ┬—
        // ├┼
        System.out.println(
                "+----+----+----+----+----+----+----+--－—－---+\n"
                        + "| 正 | 正 | 正 | 正 | 正 | 正 | 正 | 正 |\n"
                        + "| — | — | — | — | — | — | — | — |\n"
                        + "| － | － | － | － | － | － | － | － |\n"
                        + "+----+----+----+----+----+----+----+----+\n"
                        + "| 正 | 正 | 正 | 正 | 正 | 正 | 正 | 正 |\n"
                        + "+----+----+----+----+----+----+----+----+\n"
                        + "| 正 | 正 | 正 | 正 | 正 | 正 | 正 | 正 |\n"
                        + "+----+----+----+----+----+----+----+----+");
    }
}