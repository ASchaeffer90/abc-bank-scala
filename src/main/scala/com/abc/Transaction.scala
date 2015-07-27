package com.abc

case class Transaction(reason: Int ,amount: Double) {
  val transactionDate = DateProvider.getInstance.now
}

object Transaction {
  final val DEPOSIT: Int = 0
  final val WITHDRAWAL: Int = 1
  final val INTEREST: Int = 2
}

