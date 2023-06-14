package com.cm.welfarecmcity;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class Calculate {

    public static void main(String[] args) {
        double principal = 350000; // The initial loan amount
        double interestRate = 5.0; // The annual interest rate (e.g., 5%)
        int numOfPayments = 24; // The number of monthly payments
        LocalDate paymentStartDate = LocalDate.of(2023, 1, 31); // The start date of payments

        double installment = calculateLoanInstallment(principal, interestRate, numOfPayments);
        System.out.println("Monthly installment: " + installment);

        createAmortizationTable(principal, interestRate, numOfPayments, installment, paymentStartDate);
    }

    public static double calculateLoanInstallment(double principal, double interestRate, int numOfPayments) {
        double monthlyInterestRate = (interestRate / 100) / 12;
        double installment = (principal * monthlyInterestRate) / (1 - Math.pow(1 + monthlyInterestRate, -numOfPayments));
        return installment;
    }

    public static void createAmortizationTable(double principal, double interestRate, int numOfPayments, double installment, LocalDate paymentStartDate) {
        DecimalFormat decimalFormat = new DecimalFormat("#");

        System.out.println("\nAmortization Table:");
        System.out.println("Payment\t\tDate\t\tInterest\tPrincipal\t\tInstallment\t\tBalance\t\tNumber of Day\t\tTotal Deduction");

        double remainingBalance = principal;
        double toralDeduction = principal;
        LocalDate paymentDate = paymentStartDate;
        LocalDate paymentDateDeduction = paymentStartDate;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateFormatterDay = DateTimeFormatter.ofPattern("dd");

        for (int i = 1; i <= numOfPayments; i++) {
            LocalDate paymentDateShow = paymentStartDate.plusMonths(i-1);

            // Balance
            YearMonth currentPaymentMonth = YearMonth.from(paymentDate);
            int daysInMonth = currentPaymentMonth.lengthOfMonth();
            double interest = Math.round((remainingBalance * (interestRate / 100) / 365) * daysInMonth);
            double principalPaid = Math.round(installment - interest);

            if(i == numOfPayments){
                 double principalPaidLast = remainingBalance;
                 double installmentSumLastMonth = principalPaidLast - interest;
                 //remainingBalance -= principalPaid;
                 // paymentDate.format(dateFormatter)
                 System.out.println(
                         i + "\t\t"
                         + paymentDateShow.format(dateFormatter) + "\t\t"
                         + decimalFormat.format(interest) + "\t\t"
                         + decimalFormat.format(principalPaidLast) + "\t\t"
                         + decimalFormat.format(principalPaidLast) + "\t\t\t\t"
                         + decimalFormat.format(0) + "\t\t"
                         + paymentDateShow.format(dateFormatterDay) + "\t\t\t\t"
                         + decimalFormat.format(principalPaidLast) + "\t\t"
                 );
            }else {
                remainingBalance -= principalPaid;
                if (i == 1) {
                    System.out.println(
                            i + "\t\t"
                                    + paymentDateShow.format(dateFormatter) + "\t\t"
                                    + decimalFormat.format(interest) + "\t\t"
                                    + decimalFormat.format(principalPaid) + "\t\t"
                                    + decimalFormat.format(installment) + "\t\t\t\t"
                                    + decimalFormat.format(remainingBalance) + "\t\t"
                                    + paymentDateShow.format(dateFormatterDay) + "\t\t\t\t"
                                    + decimalFormat.format(principal) + "\t\t"
                    );
                } else {
                    // toralDeduction
                    YearMonth currentPaymentMonthDeduction = YearMonth.from(paymentDateDeduction);
                    int daysInMonthDeduction = currentPaymentMonthDeduction.lengthOfMonth();
                    double interestDeduction = Math.round((toralDeduction * (interestRate / 100) / 365) * daysInMonthDeduction);
                    double principalPaidDeduction = Math.round(installment - interestDeduction);
                    toralDeduction -= principalPaidDeduction;
                    System.out.println(
                            i + "\t\t"
                                    + paymentDateShow.format(dateFormatter) + "\t\t"
                                    + decimalFormat.format(interest) + "\t\t"
                                    + decimalFormat.format(principalPaid) + "\t\t"
                                    + decimalFormat.format(installment) + "\t\t\t\t"
                                    + decimalFormat.format(remainingBalance) + "\t\t"
                                    + paymentDateShow.format(dateFormatterDay) + "\t\t\t\t"
                                    + decimalFormat.format(toralDeduction) + "\t\t"
                    );
                    paymentDateDeduction = paymentDateDeduction.plusMonths(1);
                }
            }
            paymentDate = paymentDate.plusMonths(1);
        }
    }

//    public static void main(String[] args) {
//        double loanAmount = 350000; // The loan amount
//        double interestRate = 0.05; // The annual interest rate (5%)
//        int loanDuration = 24; // The duration of the loan in months
//        LocalDate startDate = LocalDate.of(2023, 1, 31); // The start date of the loan payments
//
//        double[][] installmentSchedule = calculateInstallmentSchedule(loanAmount, interestRate, loanDuration, startDate);
//        DecimalFormat df = new DecimalFormat("#.##");
//        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//        System.out.println("Loan Amount: $" + loanAmount);
//        System.out.println("Interest Rate: " + (interestRate * 100) + "%");
//        System.out.println("Loan Duration: " + loanDuration + " months");
//        System.out.println("Start Date: " + startDate.format(dateFormatter));
//        System.out.println("\nInstallment Schedule:");
//
//        for (int i = 0; i < installmentSchedule.length; i++) {
//            LocalDate paymentDate = startDate.plusMonths(i);
//            System.out.println("Month " + (i + 1) + " (" + paymentDate.format(dateFormatter) + "):");
//            System.out.println("  - Principal: $" + df.format(installmentSchedule[i][0]));
//            System.out.println("  - Interest: $" + df.format(installmentSchedule[i][1]));
//            System.out.println("  - Total Payment: $" + df.format(installmentSchedule[i][2]));
//        }
//    }
//
//    public static double[][] calculateInstallmentSchedule(double loanAmount, double interestRate, int loanDuration, LocalDate startDate) {
//        double monthlyInterestRate = interestRate / 12;
//        double[][] installmentSchedule = new double[loanDuration][3];
//
//        double denominator = 1 - Math.pow(1 + monthlyInterestRate, -loanDuration);
//        double installment = loanAmount * (monthlyInterestRate / denominator);
//
//        for (int i = 0; i < loanDuration; i++) {
//            double interest = loanAmount * monthlyInterestRate;
//            double principal = installment - interest;
//
//            installmentSchedule[i][0] = principal;
//            installmentSchedule[i][1] = interest;
//            installmentSchedule[i][2] = installment;
//
//            loanAmount -= principal;
//        }
//
//        return installmentSchedule;
//    }

}
