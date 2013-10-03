package gwt.shared;

import java.util.ArrayList;

public class Utils {

    public static int compareDate(int day1, int month1, int year1, int day2, int month2, int year2) {
        if (year1 > year2) {
            return 1;
        } else if (year1 < year2) {
            return -1;
        } else {
            if (month1 > month2) {
                return 1;
            } else if (month1 < month2) {
                return -1;
            } else {
                if (day1 > day2) {
                    return 1;
                } else if (day1 < day2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    }

    public static int getLastDay(int month, int year) {

        // Stupid hack!!
        // more than 2500 means Thai year so minus 543
        if (year > 2500) {
            year = year - 543;
        }

        switch (month) {
        case 1:
            return 31;
        case 2:
            if (year % 4 == 0) {
                return 29;
            } else {
                return 28;
            }
        case 3:
            return 31;
        case 4:
            return 30;
        case 5:
            return 31;
        case 6:
            return 30;
        case 7:
            return 31;
        case 8:
            return 31;
        case 9:
            return 30;
        case 10:
            return 31;
        case 11:
            return 30;
        case 12:
            return 31;
        default:
            throw new AssertionError(month);
        }
    }
    
    public static boolean isZero(double d, int decimals) {
        return ((int)(d * Math.pow(10, decimals))) == 0;
    }
    
    public static boolean hasSpace(String s) {
        return s.indexOf(" ") != -1;
    }
 
    /**
     * Find all months from a period
     * @param beginDate [beginDay, beginMonth, beginYear]
     * @param endDate [endDay, endMonth, endYear]
     * @return { [beginDay, beginMonth, beginYear], [0, month2, year2], ..., [endDay, endMonth, endYear] }
     */
    public static ArrayList<int[]> findAllMonths(int[] beginDate, int[] endDate) {

        final ArrayList<int[]> months = new ArrayList<int[]>();

        // Find all months
        if (beginDate[2] == endDate[2]) {
            for (int i = beginDate[1]; i <= endDate[1]; i++) {
                int day = 0;
                if (i == beginDate[1]) {
                    day = beginDate[0];
                } else if (i == endDate[1]) {
                    day = endDate[0];
                }
                int[] month = { day, i, beginDate[2] };
                months.add(month);
            }
        } else {
            for (int i = beginDate[1]; i <= 12; i++) {
                int day = 0;
                if (i == beginDate[1]) {
                    day = beginDate[0];
                }
                int[] month = { day, i, beginDate[2] };
                months.add(month);
            }
            for (int i = 1; i <= endDate[1]; i++) {
                int day = 0;
                if (i == endDate[1]) {
                    day = endDate[0];
                }
                int[] month = { day, i, endDate[2] };
                months.add(month);
            }
        }

        return months;
    }
}
