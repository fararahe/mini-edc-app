package id.faradyna.miniedcapp.domain.model

/**
 * Model data untuk representasi struk (receipt) yang siap dicetak.
 *
 * Berisi informasi transaksi, merchant, dan terminal
 * yang sudah diformat untuk kebutuhan display/printing.
 */
data class Receipt(
    val merchantName: String,
    val terminalId: String,
    val traceNumber: String,
    val dateTime: String,
    val amount: String,
    val status: String,
    val rrn: String,
    val approvalCode: String
)
