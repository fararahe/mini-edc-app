package id.faradyna.miniedcapp.data.mapper

import id.faradyna.miniedcapp.data.local.room.entity.TransactionEntity
import id.faradyna.miniedcapp.domain.model.SaleTransaction
import id.faradyna.miniedcapp.domain.model.TransactionStatus

/**
 * Mapper untuk konversi antara TransactionEntity (data layer)
 * dan SaleTransaction (domain layer).
 */
fun TransactionEntity.toDomain(): SaleTransaction {
    return SaleTransaction(
        id = id,
        amount = amount,
        type = type,
        timestamp = timestamp,
        invoiceNumber = invoiceNumber,
        traceNumber = traceNumber,
        cardPan = cardPan,
        status = TransactionStatus.from(status),
        rrn = rrn,
        approvalCode = approvalCode,
        message = message
    )
}

fun SaleTransaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id,
        amount = amount,
        type = type,
        timestamp = timestamp,
        invoiceNumber = invoiceNumber,
        traceNumber = traceNumber,
        cardPan = cardPan,
        status = status.name,
        rrn = rrn,
        approvalCode = approvalCode,
        message = message
    )
}
