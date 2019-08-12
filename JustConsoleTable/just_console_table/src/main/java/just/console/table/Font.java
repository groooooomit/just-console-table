package just.console.table;

/**
 * 全角字符适配
 * <p>
 * 尽量不要使用全角字符
 */
public enum Font {

    /* 5:3
     * |aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa|
     * |正正正正正正正正正正正正正正正正正正|
     *
     * 15:8
     * |aaaaaaaaaaaaaaa|
     * |正正正正正正正正|
     *
     * 2:1
     * |aaaaaaaaaa|
     * |正正正正正|
     */

    ///////////////////////////////////////////////////////////////////////////
    // Android Studio 字体
    ///////////////////////////////////////////////////////////////////////////

    CONSOLAS(15, 8),
    COURIER_NEW(5, 3),
    DIALOG_INPUT(5, 3),
    DROID_SANS_MONO(5, 3),
    DROID_SANS_MONO_DOTTED(5, 3),
    DROID_SANS_MONO_SLASHED(5, 3),
    FANG_SONG(15, 8),
    FIRA_CODE(5, 3),
    FIRA_CODE_LIGHT(5, 3),
    FIRA_CODE_MEDIUM(5, 3),
    FIRA_CODE_RETINA(5, 3),
    INCONSOLATA(5, 3),
    KAI_TI(15, 8),
    LI_SU(15, 8),
    LUCIDA_CONDOLE(5, 3),
    MINGLIU_EXTB(15, 8),
    MINGLIU_HKSCS_EXTB(15, 8),
    MONOSPACED(15, 8),
    NSIM_SUN(15, 8),
    SIM_HEI(15, 8),
    SIM_SUN(15, 8),
    SIM_SUN_EXB(15, 8),
    SOURCE_CODE_PRO(5, 3),
    YAHEI_CONSOLAS_HYBIRD(15, 8),
    YOU_YUAN(15, 8),

    ///////////////////////////////////////////////////////////////////////////
    // windows 系统
    ///////////////////////////////////////////////////////////////////////////

    /**
     * windows 命令行，所有字体比例都是 2:1，包括 Windows 下 IDEA 或 AndroidStudio 的 Terminal
     */
    WINDOWS_CMD(2, 1),

    /**
     * IDEA 或 Android Studio Terminal 都是 2:1
     */
    IDEA_TERMINAL(2, 1),

    /**
     * windows 微软雅黑（windows 10 中文版记事本默认字体）
     */
    MICROSOFT_YAHEI(20, 9);

    private final int halfCount;
    private final int fullCount;

    Font(int halfCount, int fullCount) {
        this.halfCount = halfCount;
        this.fullCount = fullCount;
    }

    public final int halfCount() {
        return halfCount;
    }

    public final int fullCount() {
        return fullCount;
    }

    /**
     * 全角与半角显示长度比例
     * <p>
     * 例如一个全角字符的显示宽度等于 1.875 个半角字符的显示宽度，那么 8 个汉字打印出来的长度正好等于 8 * 1.875 = 15 个字母打印出来的长度
     */
    public final float scala() {
        return halfCount * 1f / fullCount;
    }

    private static Font defaultFont = CONSOLAS;

    public static Font getDefaultFont() {
        return defaultFont;
    }

    public static void setDefaultFont(Font defaultFont) {
        Font.defaultFont = defaultFont;
    }}
