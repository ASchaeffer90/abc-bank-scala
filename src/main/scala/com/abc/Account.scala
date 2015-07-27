package com.abc

import java.text.SimpleDateFormat
import java.util.Calendar

import scala.collection.mutable.ListBuffer

object Account {
  final val CHECKING: Int = 0
  final val SAVINGS: Int = 1
  final val MAXI_SAVINGS: Int = 2
}

case class Account(identifier: Long, accountType: Int, transactions: ListBuffer[Transaction] = ListBuffer()) {

  //"DateProvider.getInstance.now.getTime" will make sure the account id is unique
  def this(accountType: Int) = this(DateProvider.getInstance.now.getTime, accountType)

  def deposit(reason: Int, amount: Double) {
    if (amount <= 0)
      throw new IllegalArgumentException("amount must be greater than zero")
    else
      transactions += Transaction(reason, amount)
  }

  def withdraw(amount: Double) {
    if (amount <= 0)
      throw new IllegalArgumentException("amount must be greater than zero")
    else
      transactions += Transaction(Transaction.WITHDRAWAL, -amount)
  }

  def dateOfLastWithdrawal = {
    val transaction = transactions.filter(_.reason == Transaction.WITHDRAWAL).sortBy(_.transactionDate).reverse
    try{
      transaction.head.transactionDate
    } catch {
      case e: RuntimeException =>
        val dateFormat = new SimpleDateFormat("yyyy-MM-dd")
        dateFormat.parse("0000-01-01")

    }
  }

  val interestPer = 365 // rate per year divided by days in year including weekends

  def dailyInterestEarned: Double = {
    val amount: Double = sumTransactions()
    accountType match {
      case Account.SAVINGS =>
        if (amount <= 1000) amount * (0.001/ interestPer)
        else ((amount - 1000) * (0.002/ interestPer)) + (1000 * (0.001/ interestPer))
      case Account.MAXI_SAVINGS =>
        val tenDaysAgoOnCalendar = Calendar.getInstance()
        tenDaysAgoOnCalendar.add(Calendar.DATE, -10)
        dateOfLastWithdrawal match {
          case x if x.before(tenDaysAgoOnCalendar.getTime) => amount * (0.05 / interestPer)
          case x if x.after(tenDaysAgoOnCalendar.getTime) => amount * (0.001 / interestPer)
        }
      case _ =>
        amount * (0.001/ interestPer)
    }
  }

  def sumTransactions(checkAllTransactions: Boolean = true): Double = transactions.map(_.amount).sum

  def sumOfInterest: Double = transactions.filter(_.reason == Transaction.INTEREST).map(_.amount).sum

}