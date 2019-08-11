package just.console.table;

import androidx.annotation.NonNull;

// todo 合并单元格
public class ComposeCell implements Cell {


    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @NonNull
    @Override
    public String print(int floor, int allowMaxWidth) {
        return null;
    }
}
