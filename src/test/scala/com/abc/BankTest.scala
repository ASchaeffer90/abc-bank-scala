package com.abc

import org.scalatest.{Matchers, FlatSpec}

class BankTest extends FlatSpec with Matchers {

  "Bank Manager" should "be able to request a customer summary" in {
    val bank: Bank = new Bank
    val john: Customer = new Customer("John").openAccount(new Account(Account.CHECKING))
    bank.addCustomer(john)
    bank.customerSummary should be("Customer Summary\n - John (1 account)")
  }

  it should "be able to check the interest paid on a checking account" in {
    val bank: Bank = new Bank
    val checkingAccount: Account = new Account(Account.CHECKING)
    val bill: Customer = new Customer("Bill").openAccount(checkingAccount)
    bank.addCustomer(bill)
    checkingAccount.deposit(100.0)
    bank.totalInterestPaid should be(0.1)
  }

  it should "be able to check the interest paid on a savings account" in {
    val bank: Bank = new Bank
    val checkingAccount: Account = new Account(Account.SAVINGS)
    bank.addCustomer(new Customer("Bill").openAccount(checkingAccount))
    checkingAccount.deposit(1500.0)
    bank.totalInterestPaid should be(2.0)
  }

  it should "be able to check the interest paid on a maxi savings account" in {
    val bank: Bank = new Bank
    val checkingAccount: Account = new Account(Account.MAXI_SAVINGS)
    bank.addCustomer(new Customer("Bill").openAccount(checkingAccount))
    checkingAccount.deposit(3000.0)
    bank.totalInterestPaid should be(150)
  }

  it should "be able to check the total interest paid on multiple different opened accounts" in {
    val bank: Bank = new Bank
    val maxiSavingsAccount: Account = new Account(Account.MAXI_SAVINGS)
    val checkingAccount: Account = new Account(Account.CHECKING)
    val customerBill: Customer = new Customer("Bill")
    customerBill.openAccount(maxiSavingsAccount)
    customerBill.openAccount(checkingAccount)
    bank.addCustomer(customerBill)
    checkingAccount.deposit(100.0)
    maxiSavingsAccount.deposit(3000.0)
    bank.totalInterestPaid should be(150.1)
  }

}
