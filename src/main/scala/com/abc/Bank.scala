package com.abc

import java.util.concurrent.{TimeUnit, Executors}

import scala.collection.mutable.ListBuffer

class Bank {

  val scheduledPool = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(payOutInterestDaily, 0, 1, TimeUnit.DAYS)

  val customers = new ListBuffer[Customer]

  def addCustomer(customer: Customer) {
    customers += customer
  }

  def customerSummary: String = {
    var summary: String = "Customer Summary"
    for (customer <- customers)
      summary = summary + "\n - " + customer.name + " (" + format(customer.numberOfAccounts, "account") + ")"
    summary
  }

  private def format(number: Int, word: String): String = {
    number + " " + (if (number == 1) word else word + "s")
  }

  def totalInterestPaid: Double = {
    var total: Double = 0
    for (c <- customers) total += c.totalInterestEarned
    total
  }

  def payOutInterestDaily = new Runnable {
    override def run(): Unit = {
//     println("interest Accrued")
      customers.foreach {
        customer =>
          customer.accounts.foreach {
            account =>
              val interest = account.dailyInterestEarned
              account.deposit(Transaction.INTEREST, interest)
          }
      }
    }
  }

}


