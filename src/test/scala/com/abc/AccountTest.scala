package com.abc


import java.text.SimpleDateFormat

import org.scalatest.{FlatSpec, Matchers}


class AccountTest extends FlatSpec with Matchers {

  "An Account" should "increase its holding when there is a deposit" in {
    val account = new Account(Account.CHECKING)
    assert(account.sumTransactions() == 0)
    account.deposit(100.0)
    assert(account.sumTransactions() == 100.0)
  }

  it should "decrease its holding when there is a withdrawal" in {
    val account = new Account(Account.CHECKING)
    account.deposit(100.0)
    account.withdraw(10.0)
    assert(account.sumTransactions() == 90)
  }

  it should "be able to display the last withdrawal date of beginning of time if there is no withdrawals" in {
    val account = new Account(Account.CHECKING)
    account.deposit(100.0)
    val dateformat = new SimpleDateFormat("yyyy-MM-dd")
    val withdrawalDate = dateformat.parse("0000-01-01")
    assert(account.dateOfLastWithdrawal == withdrawalDate)
  }

  it should "be able to display the last withdrawal date of today if there was a withdrawal today" in {
    val account = new Account(Account.CHECKING)
    account.deposit(100.0)
    account.withdraw(10.0)
    assert(account.dateOfLastWithdrawal == DateProvider.getInstance.now)
  }

  "A Checking Account" should "should have a flat 1% interest rate" in {
    val checkingAccount: Account = new Account(Account.CHECKING)
    checkingAccount.deposit(100.0)
    checkingAccount.interestEarned should be(0.1)
  }

  "A Savings Account" should "be able to check the interest paid on a savings account" in {
    val savingsAccount: Account = new Account(Account.SAVINGS)
    savingsAccount.deposit(1500.0)
    savingsAccount.interestEarned should be(2.0)
  }

  "A Maxi-Savings Account" should "be able to check the interest paid on a maxi savings account without a withdrawal" in {
    val maxiAccount: Account = new Account(Account.MAXI_SAVINGS)
    maxiAccount.deposit(3000.0)
    maxiAccount.interestEarned should be(150.0)
  }

  it should "should be .1% after first withdrawal" in {
    val maxiAccount: Account = new Account(Account.MAXI_SAVINGS)
    maxiAccount.deposit(3000.0)
    maxiAccount.withdraw(2000.0)
    maxiAccount.interestEarned should be (1.0)
  }
}
