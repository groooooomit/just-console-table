package just.console.table;

/*

  @ 少年作死团
  +---+------------------+---+---+-----------------+
  | N | Name             |   | N | Name            |
  +---+------------------+   +---+-----------------+
  | 0 | 江户川柯南　　　  |   | 5 | 小岛元太　　　　 |
  +---+------------------+   +---+-----------------+
  | 1 |                  |   | 6 | 灰原哀　　　　　 |
  +---+------------------+   +---+-----------------+
  | 2 | null             |   | 7 | 江户川柯南　　　 |
  +---+------------------+   +---+-----------------+
  | 3 | 吉田步.美　　　　 |   | 8 | 吉田步美　　　　 |
  +---+------------------+   +---+-----------------+
  | 4 | 圆.谷光彦　　　　 |   | 9 | 圆谷光彦　　　　 |
  +---+------------------+---+---+-----------------+

 * Tabular 的 console 小游戏
 * 注意与 kotlin 的兼容性
 * Tabular.of(...)
 * Tabular.put(1, "name", "张三")
 * Tabular.put(2, "name", "李四")
 * Tabular
 *      .create("xxx")
 *      [.header(...)] header 可通过 header 进行变更操作，而不用
 *      .row(...)
 *      .alignLeft()
 *      .alignRight()
 *      .alignCenter()
 *      .row(Cell.of(xxx), Cell.of(xxx), Cell.of(xxx), Cell.of(xxx), Cell.of(xxx))
 *      .row()
 *      .row() // 空行全填充 null
 *      .cell(xxx,Gravity.CENTER)
 *      .cell(xxx).alignCenter()
 *      .composeCell(1, 2, "xxx") 组合单元格
 *      .row(...)
 *      ...
 *      .row(...);
 *      .replace(null, "") // null 值处理，例如如果在行尾，那么不显示；如果一整行都为 null，那么不显示这整行，行号连续；如果为 null，显示 "null"，或显示其他
 *      .replace("", "[ ${value} ]")
 *      .platform(IDEA_LOG) // 多平台、多字体输出
 *      .showRowNo("No."); // 显示行号
 *
 * Tabular.of(List)
 * Tabular.of(name, List<E>)
 *      .header("xxx")
 *      .flatMap(Function<E, Row>) -> Row.of(xxx,xxx,xxx,xxx) -> Row.of(List<Cell>)
 *      .maxRow(5, margin);// 根据 of 的类型判断，itertaor 的类型可以有此配置，margin 用来进行分割小块
 *      .horizontal() 水平打印，即行和列互换位置
 * Tabular.of(Set)
 * Tabular.of(name, Set)
 * Tabular.of(Map)
 * Tabular.of(name, Map)
 * Tabular.of(exception)
 * Tabular.of(name, exception)
 */
public class Tabular {

    /**
     * 打印一行多列内容
     */
    public static <V> void of(V... values) {

    }

}
