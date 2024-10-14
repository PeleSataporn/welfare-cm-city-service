package com.cm.welfarecmcity;

import java.text.DecimalFormat;

public class CalloanOld {

  //    public static void main(String[] args) {
  //
  //        BigDecimal loanAmount = new BigDecimal("450000");
  //        BigDecimal interestRate = new BigDecimal("0.05");
  //        int numInstallments = 22;
  //
  //        BigDecimal installment = calculateInstallment(loanAmount, interestRate,
  // numInstallments);
  //        System.out.println("Monthly Installment: $" + installment);
  //    }
  //
  //    public static BigDecimal calculateInstallment(BigDecimal loanAmount, BigDecimal
  // interestRate, int numInstallments) {
  //        BigDecimal monthlyInterestRate = interestRate.divide(BigDecimal.valueOf(12), 5,
  // RoundingMode.HALF_UP);
  //        BigDecimal numerator = monthlyInterestRate.multiply(loanAmount);
  //        BigDecimal denominator =
  // BigDecimal.ONE.subtract(BigDecimal.ONE.divide(monthlyInterestRate.add(BigDecimal.ONE).pow(numInstallments).negate(), 5, RoundingMode.HALF_UP));
  //        BigDecimal installment = numerator.divide(denominator, 2, RoundingMode.HALF_UP);
  //        return installment;
  //    }

  public static void main(String[] args) {

    System.out.println("\nAmortization Table:");
    System.out.println("Month\t\tMonthPaymonth\t\tInterest\t\tPrincipal\t\tRemaining Balance");

    double loanAmount = 450000; // Replace with the actual loan amount
    double interestRate = 0.05; // Replace with the actual interest rate
    int loanTermInMonths = 22; // Replace with the actual loan term in months

    double monthlyInterestRate = interestRate / 12;
    double monthlyPayment = loanAmount / loanTermInMonths;

    DecimalFormat decimalFormat = new DecimalFormat("#");

    for (int month = 1; month <= loanTermInMonths; month++) {
      double interestPayment = Math.round((loanAmount * interestRate) / 12);
      double principalPayment = Math.round(monthlyPayment - interestPayment);
      loanAmount = Math.round(loanAmount - monthlyPayment);

      System.out.println(
          month
              + "\t\t"
              + decimalFormat.format(monthlyPayment)
              + "\t\t"
              + decimalFormat.format(interestPayment)
              + "\t\t"
              + decimalFormat.format(principalPayment)
              + "\t\t"
              + decimalFormat.format(loanAmount));

      // System.out.println(decimalFormat.format(interestPayment));
    }
  }
}
