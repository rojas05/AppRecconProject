package com.rojasdev.apprecconproject.pdf

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.itextpdf.awt.geom.Rectangle
import com.itextpdf.text.PageSize
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
import com.itextpdf.text.Image
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

class generatePdfSemanal(
    var context: Context,
    var resources: Resources, //resourses permite acceder a la imagen para el pdf
    var location: () -> Unit //location corresponde a la ubicacion del pdf
) {
    //dateWeek me guarda la fecha de inicio y de fin de la semana
    private lateinit var dateWeek: Pair<String,String>

    private var txtFont: Font = FontFactory.getFont("arial", 12f, Font.BOLD)

    @SuppressLint("SuspiciousIndentation")
    fun generate(uri: Uri) {

        dateWeek = getStarAndEndWeek() //llamada de la funcion que retorna la fecha de inicio y fin de la semana

        try {
            val document = Document()
            val outputStream = context.contentResolver.openOutputStream(uri)
            val writer = PdfWriter.getInstance(document, outputStream)
                document.open()

            val contentByte: PdfContentByte = writer.directContentUnder
            val byte: PdfContentByte = writer.directContentUnder
            val square: PdfContentByte = writer.directContentUnder

            // Background del encabezado de las esquinas
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

            // Agrega una imagen al PDF desde la carpeta "drawable"
            val drawableImageLogo = BitmapFactory.decodeResource(resources, R.drawable.reccon_pdf)
            val byteArrayOutputStream = ByteArrayOutputStream()
                drawableImageLogo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val image = Image.getInstance(byteArray)
                image.scaleToFit(130f, 130f)

            // Pdf Title
            val titleFont: Font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 21F, BaseColor.WHITE)
            val titlePdf = Paragraph("\n${context.getString(R.string.titlePdfWeek)}\n", titleFont)

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
            val infoPdf = Paragraph("${context.getString(R.string.txtPdfWeek)}\n", infoFont)

            val cellInfo = PdfPCell(infoPdf)
                cellInfo.verticalAlignment = PdfPCell.ALIGN_LEFT
                cellInfo.horizontalAlignment = PdfPCell.ALIGN_LEFT
                cellInfo.borderWidth = 0f

            tableInfo.addCell(cellInfo)
            document.add(tableInfo)

            //ejecutando las funciones en cadena para que al terminar el pdf se cierre
            createTableAliment("active", context, context.getString(R.string.actualPrice), document){
                createTableAliment("archived", context, context.getString(R.string.previousPrice), document){

                    val txtInfo = Paragraph(context.getString(R.string.infoPdfYearRecolection), infoFont)
                    txtInfo.alignment = Element.ALIGN_LEFT

                    document.add(txtInfo)

                    createTableWeek(context.getString(R.string.notAliment), document, "no"){
                        createTableWeek(context.getString(R.string.yesAliment), document, "yes"){
                            finish(document)
                            writer.close()
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

    @SuppressLint("SuspiciousIndentation")
    private fun createTableWeek(title: String,document: Document, aliment: String,ready: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch{
           val query = AppDataBase.getInstance(context).RecolectoresDao()
                .getWeekPdf(
                    dateWeek.first,
                    dateWeek.second,
                    aliment
                )
            launch(Dispatchers.Main) {
                if (query.isNotEmpty()){
                    //pdf table permite crear una tabla donde numColumns es el numero de columnas que organizara automaticamente
                    val table = PdfPTable(4)
                        table.horizontalAlignment = Element.ALIGN_LEFT
                        table.widthPercentage = 100f
                    val header = PdfPCell()
                        header.verticalAlignment = Element.ALIGN_CENTER
                        header.horizontalAlignment = Element.ALIGN_CENTER

                    //funcion para crear un titulo a la tabla
                    titleTable(title,100f, document)

                    //funcion para crear items de la tabla
                    itemsTable(table,header)

                    //de esta manera se pintyan cada uno de los resultados
                    for (item in query) {
                        header.phrase = Phrase(item.name_recolector)
                            table.addCell(header)
                        header.phrase = Phrase("${item.result} Kg")
                            table.addCell(header)

                        price.priceSplit(item.total.toInt()) {
                            header.phrase = Phrase(it)
                                table.addCell(header)
                        }

                        if (item.Estado == "active") {
                            header.phrase = Phrase(context.getString(R.string.stateActive))
                                table.addCell(header)
                        } else {
                            header.phrase = Phrase(context.getString(R.string.stateArchive))
                                table.addCell(header)
                        }
                    }

                    //pintando la tabla en documento
                    document.add(table)

                    val txtInfoFont: Font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18F, BaseColor.BLACK)
                    val txtInfo =  Paragraph("${context.getString(R.string.totalInfoYear)} \n\n", txtInfoFont)
                        txtInfo.alignment = Element.ALIGN_LEFT

                    document.add(txtInfo)

                    //generando una tabla nueva para el total
                    createTotal(document,aliment){
                        ready() //respondiendo de que se finalizo para generar la siguiente tabla
                    }

                }else{
                    // en caso de no tener datos de respuesta de la base de datos pintamos un texto en el pdf para que el documento no falle
                    noData(title,document){
                        ready() //respondiendo de que se finalizo para generar la siguiente tabla
                    }
                }
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun createTotal(document: Document, aliment: String, ready: () -> Unit) {
        val dateWeek = getStarAndEndWeek()
        CoroutineScope(Dispatchers.IO).launch{
            val query = AppDataBase.getInstance(context).SettingDao()
                .getTotalPdfWeek(
                    dateWeek.first,
                    dateWeek.second,
                    aliment
                )
            launch(Dispatchers.Main) {
                if (query.isNotEmpty()){
                    // de esta manera se pueden dar un poco de estilos a las tablas
                    val table = PdfPTable(4)
                        table.widthPercentage = 100f
                    val header = PdfPCell()
                        header.verticalAlignment = Element.ALIGN_CENTER
                        header.horizontalAlignment = Element.ALIGN_CENTER
                        header.borderWidth = 0f

                    val listCell = listOf(
                        context.getString(R.string.princeTotal),
                        context.getString(R.string.tvRecolcection),
                        context.getString(R.string.valorTotal),
                        context.getString(R.string.total)
                    )

                    txtFont.color = BaseColor.WHITE

                    header.phrase = Phrase("")
                        table.addCell(header)
                    header.borderWidth = 1f
                    header.backgroundColor = BaseColor(74, 120, 74)

                    for (item in listCell){
                        header.horizontalAlignment = Element.ALIGN_CENTER
                        header.phrase = Phrase(item, txtFont)
                            table.addCell(header)
                    }

                    header.backgroundColor = BaseColor.WHITE
                    price.priceSplit(query[0].Precio){
                        //al hacer ersto se agrega el contenido de esta manera
                        header.phrase = Phrase(it)
                            table.addCell(header)
                    }

                    header.phrase = Phrase("${query[0].cantidad} Kg")
                        table.addCell(header)

                    price.priceSplit(query[0].total.toInt()){
                        header.phrase = Phrase(it)
                            table.addCell(header)
                    }

                    document.add(table)

                    ready()
                }
            }
        }
    }

    private fun getStarAndEndWeek(): Pair<String, String> {
        val calendar = Calendar.getInstance()

        // Encontrar el día de la semana actual
        val diaSemanaActual = calendar.get(Calendar.DAY_OF_WEEK)

        // Calcular la cantidad de días para llegar al lunes anterior (considerando que domingo es 1 y lunes es 2)
        val days = when (diaSemanaActual) {
            Calendar.MONDAY -> {
                0
            }
            Calendar.TUESDAY -> {
                1
            }
            Calendar.WEDNESDAY -> {
                2
            }
            Calendar.THURSDAY -> {
                3
            }
            Calendar.FRIDAY -> {
                4
            }
            Calendar.SATURDAY -> {
                5
            }
            else -> {
                6
            }
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd ",Locale.getDefault())
        val currentDate = Calendar.getInstance()

        val endWeek = dateFormat.format(currentDate.time)

        currentDate.add(Calendar.DATE, - days)
        val startWeek = dateFormat.format(currentDate.time)

        val horaStar = "00:00:00"
        val horaEnd = "23:59:59"

        return Pair(startWeek+horaStar, endWeek.toString()+horaEnd)
    }

    private fun titleTable(title: String, width: Float,document : Document){
        val tableTitle = PdfPTable(1)
            tableTitle.horizontalAlignment = Element.ALIGN_LEFT
            tableTitle.widthPercentage = width
            tableTitle.spacingBefore = 20f

            txtFont.size = 16f
            txtFont.color = BaseColor.WHITE
        val titlePrice = Paragraph(title,txtFont)
        val header = PdfPCell()

            header.horizontalAlignment = Element.ALIGN_CENTER
            header.backgroundColor = BaseColor(74, 120, 74)
            header.phrase = Phrase(titlePrice)
            tableTitle.addCell(header)

        document.add(tableTitle)
    }

    @SuppressLint("SuspiciousIndentation")
    private fun createTableAliment(
        state: String,
        context: Context,
        title: String,
        document: Document,
        ready : () -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch{
            val query = AppDataBase.getInstance(context).SettingDao().getAlimentState(state)
            launch(Dispatchers.Main) {
                if(query.isEmpty()){
                    noData(title,document){
                        ready()
                    }
                }else{
                    val tablePrices = PdfPTable(2)
                        tablePrices.horizontalAlignment = Element.ALIGN_LEFT
                        tablePrices.widthPercentage = 50f
                    val header = PdfPCell()
                    val txtFont: Font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12f, BaseColor.BLACK)

                    titleTable(title,50f,document)

                    val cell = listOf(
                        context.getString(R.string.aliment),
                        context.getString(R.string.princeTotal)
                    )

                    for(item in cell) {
                        header.horizontalAlignment = Element.ALIGN_CENTER
                        header.phrase = Phrase(item, txtFont)
                        tablePrices.addCell(header)
                    }

                    for(item in query){
                        header.horizontalAlignment = Element.ALIGN_CENTER
                        if (item.feeding == "yes"){
                            header.backgroundColor = BaseColor(74, 120, 74,15)
                            header.phrase = Phrase("Si")
                                tablePrices.addCell(header)
                        }else{
                            header.backgroundColor = BaseColor.WHITE
                            header.phrase = Phrase("No")
                                tablePrices.addCell(header)
                        }

                        price.priceSplit(item.cost){
                            header.phrase = Phrase(it)
                                tablePrices.addCell(header)
                        }
                    }
                    document.add(tablePrices)
                    ready()
                }
            }
        }

    }

    private fun itemsTable(table: PdfPTable,header: PdfPCell) {
        val cell = listOf(
            context.getString(R.string.name),
            context.getString(R.string.recolection),
            context.getString(R.string.totalPrince),
            context.getString(R.string.state)
        )
        txtFont.color = BaseColor.BLACK

        for(it in cell) {
            header.horizontalAlignment = Element.ALIGN_CENTER
            header.phrase = Phrase(it, txtFont)
            table.addCell(header)
        }
    }


    private fun finish(document: Document) {
        val titleFont: Font = FontFactory.getFont(FontFactory.HELVETICA, 16f, BaseColor.BLACK)
        val titlePdf = Paragraph("\n${context.getString(R.string.pdfFinish)}", titleFont)
            titlePdf.alignment = Element.ALIGN_CENTER
        document.add(titlePdf)
        document.close()

        location()
    }


    private fun noData(message:String,document: Document,ready : () -> Unit) {
        val titleFont: Font = FontFactory.getFont(FontFactory.HELVETICA, 20f, BaseColor.GRAY)
        val titlePdf = Paragraph("No se registraron $message", titleFont)
            titlePdf.alignment = Element.ALIGN_CENTER
        document.add(titlePdf)
        ready()
    }

}