package serverModule.util;

import common.data.StudyGroup;

import java.util.List;

public class ResponseOutputer {
    private static StringBuilder stringBuilder = new StringBuilder();
    private static List<StudyGroup> list;

    public static void append(Object o) {
        stringBuilder.append(o);
    }

    public static void appendTable(Object o1, Object o2) {
        stringBuilder.append(String.format("%-37s%-1s%n", o1, o2));
    }

    public static String getString() {
        return stringBuilder.toString();
    }

    public static void appendError(Object o) {
        stringBuilder.append("error: " + o + "\n");
    }

    public static void setList(List<StudyGroup> list) {
        ResponseOutputer.list = list;
    }

    public static List<StudyGroup> getList() {
        return list;
    }

    public static String getAndClear() {
        String toReturn = stringBuilder.toString();
        stringBuilder.delete(0, stringBuilder.length());
        return toReturn;
    }

    public static void clear() {
        stringBuilder.delete(0, stringBuilder.length());
    }
}
