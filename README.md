# just-console-table
A Java console table log library.

## 在 AndroidStudio 项目中使用
```gradle
implementation 'com.bfu:just-console-table:1.0.0'
```

## 打印 List
```java
String table = Table.from("老百姓", Arrays.asList("张三", "李四", "王五", "赵六", "钱七"));
System.out.println(table);
```
 ```console
  @ 老百姓
  +---+------+
  | 0 | 张三 |
  +---+------+
  | 1 | 李四 |
  +---+------+
  | 2 | 王五 |
  +---+------+
  | 3 | 赵六 |
  +---+------+
  | 4 | 钱七 |
  +---+------+
```
