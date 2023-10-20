package com.rojasdev.apprecconproject.pdf

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.itextpdf.awt.geom.Rectangle
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfContentByte
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class generateYearPDF(
    var context: Context,
    var resources: Resources,
    var location: () -> Unit
) {

    private val calendar = Calendar.getInstance().time
    private val formatOriginal = SimpleDateFormat("yyyy", Locale("es", "ES"))
    private val dateYear = formatOriginal.format(calendar)

    private var txtFont: Font = FontFactory.getFont("arial", 12f, Font.BOLD)

    @SuppressLint("SuspiciousIndentation")
    fun generateYearPdf(uri: Uri){
        try {

            val document = Document()
            val outputStream = context.contentResolver.openOutputStream(uri)
            val writer = PdfWriter.getInstance(document, outputStream)
                document.open()

            val contentByte: PdfContentByte = writer.directContentUnder
            val byte: PdfContentByte = writer.directContentUnder
            val square: PdfContentByte = writer.directContentUnder

            // Background del encabezado
            byte.setColorFill(BaseColor(74, 120, 74))
            byte.roundRectangle(0f, PageSize.A4.height * 7 / 8, PageSize.A4.width, PageSize.A4.height / 8, 0f)
            byte.fill()

            // Background del encabezado
            contentByte.setColorFill(BaseColor(74, 120, 74))
            contentByte.roundRectangle(0f, PageSize.A4.height * 4 / 5, PageSize.A4.width, PageSize.A4.height / 5, 35f)
            contentByte.fill()

            val squarePosition = Rectangle(PageSize.A4.width - 175.0, PageSize.A4.height - 150.0, 110.0, 110.0)
            val borderRadius = 20.0

            // Cuadrado
            square.roundRectangle(squarePosition.x, squarePosition.y, squarePosition.width, squarePosition.height, borderRadius)
            square.setColorFill(BaseColor.WHITE)
            square.fillStroke()

            val table = PdfPTable(3)
                table.widthPercentage = 100f

            // Logo Image
            val drawableImageLogo = BitmapFactory.decodeResource(resources, R.drawable.reccon_pdf)
            val byteArrayOutputStream = ByteArrayOutputStream()
                drawableImageLogo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val image = Image.getInstance(byteArray)
                image.scaleToFit(130f, 130f)

            // Pdf Title
            val titleFont: Font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 21F, BaseColor.WHITE)
            val titlePdf = Paragraph("\n${context.getString(R.string.titlePdfYear)}\n", titleFont)

            val cellText = PdfPCell(titlePdf)
                cellText.verticalAlignment = PdfPCell.ALIGN_CENTER
                cellText.horizontalAlignment = PdfPCell.ALIGN_LEFT
                cellText.borderWidth = 0f
                cellText.paddingRight = 8f
                cellText.colspan = 2

            val cellImage = PdfPCell(image)
                cellImage.verticalAlignment = PdfPCell.ALIGN_TOP
                cellImage.horizontalAlignment = PdfPCell.ALIGN_CENTER
                cellImage.borderWidth = 0f
                cellImage.colspan = 1

            table.addCell(cellText)
            table.addCell(cellImage)
            document.add(table)

            document.add(Phrase("\n"))

            val tableInfo = PdfPTable(1)
                tableInfo.horizontalAlignment = Element.ALIGN_LEFT
                tableInfo.widthPercentage = 75f

            val infoFont: Font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18F, BaseColor.BLACK)
            val infoPdf = Paragraph("${context.getString(R.string.infoPdfYear)} $dateYear \n", infoFont)

            val cellInfo = PdfPCell(infoPdf)
                cellInfo.verticalAlignment = PdfPCell.ALIGN_LEFT
                cellInfo.horizontalAlignment = PdfPCell.ALIGN_LEFT
                cellInfo.borderWidth = 0f

            tableInfo.addCell(cellInfo)
            document.add(tableInfo)

            // Function
            createTableAliment("active", context, context.getString(R.string.actualPrice), document) {
                createTableAliment("archived", context, context.getString(R.string.previousPrice), document) {

                    val txtInfoFont: Font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18F, BaseColor.BLACK)
                    val txtInfo = Paragraph(context.getString(R.string.infoPdfYearRecolection), txtInfoFont)
                        txtInfo.alignment = Element.ALIGN_LEFT

                    document.add(txtInfo)

                    createTableYear(context.getString(R.string.notAliment), document, "no") {
                        createTableYear(context.getString(R.string.yesAliment), document, "yes") {
                            finish(document)
                        }
                    }
                }
            }

        } catch (e: DocumentException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun createTableAliment(state: String, context: Context, title: String, document: Document, ready : () -> Unit){
        CoroutineScope(Dispatchers.IO).launch{
            val query = AppDataBase.getInstance(context).SettingDao().getAlimentState(state)
            launch(Dispatchers.Main) {
                if (query.isEmpty()){
                    notData(title,document){
                        ready()
                    }
                } else {
                    val tablePrice = PdfPTable(2)
                        tablePrice.horizontalAlignment = Element.ALIGN_LEFT
                        tablePrice.widthPercentage = 50f
                    val header = PdfPCell()
                    val txtFont: Font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12f, BaseColor.BLACK)

                    tableTitle(title,50f, document)

                    val listCell = listOf(
                        context.getString(R.string.aliment),
                        context.getString(R.string.princeTotal)
                    )

                    for(item in listCell){
                        header.horizontalAlignment = Element.ALIGN_CENTER
                        header.phrase = Phrase(item, txtFont)
                        tablePrice.addCell(header)
                    }

                    for(item in query){
                        header.horizontalAlignment = Element.ALIGN_CENTER
                        if (item.feeding == "yes"){
                            header.phrase = Phrase("si")
                            tablePrice.addCell(header)
                        }else{
                            header.phrase = Phrase("no")
                            tablePrice.addCell(header)
                        }

                        price.priceSplit(item.cost){
                            header.phrase = Phrase(it)
                            tablePrice.addCell(header)
                        }
                    }

                    document.add(tablePrice)
                    ready()
                }
            }
        }
    }

    private fun createTableYear(title: String, document: Document, aliment: String, ready: () -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            val query = AppDataBase.getInstance(context).RecolectoresDao().getPdfInfo("${dateYear}%", aliment)
            launch(Dispatchers.Main) {
                if (query.isNotEmpty()){
                    val columns = PdfPTable(4)
                        columns.widthPercentage = 100f
                    val header = PdfPCell()
                        header.verticalAlignment = Element.ALIGN_CENTER
                        header.horizontalAlignment = Element.ALIGN_CENTER

                    tableTitle(title,100f, document) // title
                    itemsTable(columns, header) // Crear los items

                    for (item in query){
                        header.phrase = Phrase(item.name_recolector)
                        columns.addCell(header)
                        header.phrase = Phrase("${item.result} Kg")
                        columns.addCell(header)
                        price.priceSplit(item.total.toInt()){
                            header.phrase = Phrase(it)
                            columns.addCell(header)
                        }

                        if (item.Estado == "active"){
                            header.phrase = Phrase(context.getString(R.string.stateActive))
                            columns.addCell(header)
                        } else {
                            header.phrase = Phrase(context.getString(R.string.stateArchive))
                            columns.addCell(header)
                        }
                    }

                    document.add(columns)

                    val txtInfoFont: Font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18F, BaseColor.BLACK)
                    val txtInfo =  Paragraph("${context.getString(R.string.totalInfoYear)} \n\n", txtInfoFont)
                        txtInfo.alignment = Element.ALIGN_LEFT

                    document.add(txtInfo)

                    totalTable(document, aliment){
                        ready()
                    }

                } else{
                    notData(title, document){
                        ready()
                    }
                }
            }
        }
    }

    private fun totalTable(document: Document, aliment: String, ready: () -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            val query = AppDataBase.getInstance(context).RecolectoresDao().getTotalPdf("${dateYear}%", aliment)
            launch(Dispatchers.Main) {
                if (query.isNotEmpty()){
                    val columns = PdfPTable(4)
                        columns.widthPercentage = 100f
                    val header = PdfPCell()
                        header.verticalAlignment = Element.ALIGN_CENTER
                        header.horizontalAlignment = Element.ALIGN_CENTER
                        header.borderWidth = 0f

                    txtFont.color = BaseColor.WHITE

                    header.phrase = Phrase("")
                    columns.addCell(header)
                    header.borderWidth = 1f
                    header.backgroundColor = BaseColor(74, 120, 74)
                    header.phrase = Phrase("Recolectado", txtFont)
                    columns.addCell(header)
                    Phrase("Recolectado", txtFont)
                    header.phrase = Phrase("Total Gastado", txtFont)
                    columns.addCell(header)
                    header.phrase = Phrase("Precio", txtFont)
                    columns.addCell(header)

                    header.phrase = Phrase("Total: ", txtFont)
                    columns.addCell(header)
                    header.backgroundColor = BaseColor.WHITE
                    header.phrase = Phrase("${query[0].result} Kg")
                    columns.addCell(header)
                    price.priceSplit(query[0].total.toInt()){
                        header.phrase = Phrase(it)
                        columns.addCell(header)
                    }
                    price.priceSplit(query[0].Precio){
                        header.phrase = Phrase(it)
                        columns.addCell(header)
                    }

                    document.add(columns)
                    ready()
                }
            }
        }
    }

    private fun tableTitle(title: String, width: Float, document: Document){
        val tableTitle = PdfPTable(1)
            tableTitle.horizontalAlignment = Element.ALIGN_LEFT
            tableTitle.widthPercentage = width
            tableTitle.spacingBefore = 20f

            txtFont.size = 16f
            txtFont.color = BaseColor.WHITE
        val titlePrice = Paragraph(title, txtFont)
        val header = PdfPCell()

            header.horizontalAlignment = Element.ALIGN_CENTER
            header.backgroundColor = BaseColor(74, 120, 74)
            header.phrase = Phrase(titlePrice)
            tableTitle.addCell(header)

        document.add(tableTitle)
    }

    private fun itemsTable(columns: PdfPTable, header: PdfPCell){
        val listCell = listOf(
            context.getString(R.string.name),
            context.getString(R.string.recolection),
            context.getString(R.string.totalPrince),
            context.getString(R.string.state)
        )
        txtFont.color = BaseColor.BLACK

        for (it in listCell){
            header.horizontalAlignment = Element.ALIGN_CENTER
            header.phrase = Phrase(it, txtFont)
            columns.addCell(header)
        }
    }

    private fun finish(document: Document){
        val titleFont: Font = FontFactory.getFont(FontFactory.HELVETICA, 16f, BaseColor.BLACK)
        val title = Paragraph("\n${context.getString(R.string.pdfFinish)}", titleFont)
            title.alignment = Element.ALIGN_CENTER
        document.add(title)
        document.close()
        location()
    }

    private fun notData(message: String, document: Document, ready: () -> Unit) {
        val titleFont: Font = FontFactory.getFont(FontFactory.HELVETICA, 20f, BaseColor.GRAY)
        val titleNotData = Paragraph("No se registraron $message", titleFont)
            titleNotData.alignment = Element.ALIGN_CENTER
        document.add(titleNotData)
        ready()
    }

}