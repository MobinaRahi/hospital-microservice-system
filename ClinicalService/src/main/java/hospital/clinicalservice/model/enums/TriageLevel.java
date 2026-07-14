package hospital.clinicalservice.model.enums;

/**
 * سطح فوریت تریاژ (هرچه کمتر، فوری‌تر)
 */
public enum TriageLevel {
    LEVEL_1(1, "بحرانی", "#ef4444"),
    LEVEL_2(2, "فوری", "#f97316"),
    LEVEL_3(3, "نیمه‌فوری", "#eab308"),
    LEVEL_4(4, "غیرفوری", "#22c55e"),
    LEVEL_5(5, "مشاوره‌ای", "#3b82f6");

    private final int code;
    private final String label;
    private final String color;

    TriageLevel(int code, String label, String color) {
        this.code = code;
        this.label = label;
        this.color = color;
    }

    public int getCode() { return code; }
    public String getLabel() { return label; }
    public String getColor() { return color; }
}
