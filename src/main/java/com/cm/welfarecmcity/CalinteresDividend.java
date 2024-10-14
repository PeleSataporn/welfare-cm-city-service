package com.cm.welfarecmcity;

import java.util.HashMap;

public class CalinteresDividend {

  //    public static void main(String[] args) {
  //
  //        String[] month = new String[] {"มิถุนายน", "กรกฎาคม"};
  //        String[] listMonth = new String[] {"มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน",
  // "พฤษภาคม", "มิถุนายน", "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม" };
  //        int sum1 = 0;
  //        int sum2 = 0;
  //        int sum3 = 0;
  //        int sum4 = 0;
  //        int sum5 = 0;
  //        int sum6 = 0;
  //        int sum7 = 0;
  //        int sum8 = 0;
  //        int sum9 = 0;
  //        int sum10 = 0;
  //        int sum11 = 0;
  //        int sum12 = 0;
  //
  //        int total = 0;
  //
  //        for(String result: month){
  //            if(result.equalsIgnoreCase(listMonth[0])){
  //                sum1 = 10;
  //            }else if(result.equalsIgnoreCase(listMonth[1])){
  //                sum2 = 10;
  //            }else if(result.equalsIgnoreCase(listMonth[2])){
  //                sum3 = 10;
  //            }else if(result.equalsIgnoreCase(listMonth[3])){
  //                sum4 = 10;
  //            }else if(result.equalsIgnoreCase(listMonth[4])){
  //                sum5 = 10;
  //            }else if(result.equalsIgnoreCase(listMonth[5])){
  //                sum6 = 10;
  //            }else if(result.equalsIgnoreCase(listMonth[6])){
  //                sum7 = 10;
  //            }else if(result.equalsIgnoreCase(listMonth[7])){
  //                sum8 = 10;
  //            }else if(result.equalsIgnoreCase(listMonth[8])){
  //                sum9 = 10;
  //            }else if(result.equalsIgnoreCase(listMonth[9])){
  //                sum10 = 10;
  //            }else if(result.equalsIgnoreCase(listMonth[10])){
  //                sum11 = 10;
  //            }else if(result.equalsIgnoreCase(listMonth[11])){
  //                sum12 = 10;
  //            }
  //            System.out.println(" month = " + result);
  //        }
  //        total = sum1 + sum2 + sum3 + sum4 + sum5 + sum6 + sum7 + sum8 + sum9 + sum10 + sum11 +
  // sum12;
  //        System.out.println(total);
  //    }

  public static void main(String[] args) {
    String[] month = new String[] {"มิถุนายน", "กรกฎาคม"};
    String[] listMonth =
        new String[] {
          "มกราคม",
          "กุมภาพันธ์",
          "มีนาคม",
          "เมษายน",
          "พฤษภาคม",
          "มิถุนายน",
          "กรกฎาคม",
          "สิงหาคม",
          "กันยายน",
          "ตุลาคม",
          "พฤศจิกายน",
          "ธันวาคม"
        };

    HashMap<String, Integer> monthSums = new HashMap<>();
    for (String monthName : listMonth) {
      monthSums.put(monthName, 0);
    }

    for (String result : month) {
      for (String monthName : listMonth) {
        if (result.equalsIgnoreCase(monthName)) {
          monthSums.put(monthName, 1200);
          break;
        }
      }
      System.out.println("month = " + result);
    }
    System.out.println("monthSums = " + monthSums.values());
    int total = monthSums.values().stream().mapToInt(Integer::intValue).sum();
    int totalInterest = Math.round((int) (total * 0.37));
    System.out.println(total);
    System.out.println(totalInterest);
  }
}
