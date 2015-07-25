package com.abc

import java.text.SimpleDateFormat
import java.util.Calendar

import scala.collection.mutable.ListBuffer

object Account {
  final val CHECKING: Int = 0
  final val SAVINGS: Int = 1
  final val MAXI_SAVINGS: Int = 2
}

class Account(val identifier: Long, val accountType: Int, var transactions: ListBuffer[Transaction] = ListBuffer()) {

  //"DateProvider.getInstance.now.getTime" will make sure the account id is unique
  def this(accountType: Int) = this(DateProvider.getInstance.now.getTime, accountType)

  def deposit(amount: Double) {
    if (amount <= 0)
      throw new IllegalArgumentException("amount must be greater than zero")
    else
      transactions += Transaction(amount)
  }

  def withdraw(amount: Double) {
    if (amount <= 0)
      throw new IllegalArgumentException("amount must be greater than zero")
    else
      transactions += Transaction(-amount)
  }

  def dateOfLastWithdrawal = {
    val transaction = transactions.filter(_.amount < 0).sortBy(_.transactionDate).reverse
    try{
      transaction.head.transactionDate
    } catch {
      case e: RuntimeException =>
        val dateformat = new SimpleDateFormat("yyyy-MM-dd")
        dateformat.parse("0000-01-01")

    }
  }

  def interestEarned: Double = {
    val amount: Double = sumTransactions()
    accountType match {
      case Account.SAVINGS =>
        if (amount <= 1000) amount * 0.001
        else 1 + (amount - 1000) * 0.002
      case Account.MAXI_SAVINGS =>
        val tenDaysAgoOnCalandar = Calendar.getInstance()
        tenDaysAgoOnCalandar.add(Calendar.DATE, -10)
        dateOfLastWithdrawal match {
          case x if x.before(tenDaysAgoOnCalandar.getTime) => amount * 0.05
          case x if x.after(tenDaysAgoOnCalandar.getTime) => amount * 0.001
        }
      case _ =>
        amount * 0.001
    }
  }

  def sumTransactions(checkAllTransactions: Boolean = true): Double = transactions.map(_.amount).sum

}