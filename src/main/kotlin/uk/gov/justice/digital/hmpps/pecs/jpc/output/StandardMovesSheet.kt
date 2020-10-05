package uk.gov.justice.digital.hmpps.pecs.jpc.output

import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Workbook
import uk.gov.justice.digital.hmpps.pecs.jpc.calculator.MovePrice

class StandardMovesSheet(workbook: Workbook, header: Header) : PriceSheet(workbook.getSheet("Standard")!!, header) {

    override fun writeRow(row: Row, price: MovePrice) = writeStandardRow(row, price)

}