package com.rojasdev.apprecconproject.pdf

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import androidx.room.util.query
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
import com.itextpdf.text.Image
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
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
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
//resourses permite acceder a la imagen para el pdf
//location corecpponde a la ubicacion del pdf
class generatePdfSemanal(
    var context: Context,
    var resources: Resources,
    var location: (String) -> Unit
) {
    //dateWeek me guarda la fecha de inicio y de fin de la semana
    //file name el nombre del pdf
    //fol der el nombre de la carpeta para almacenar los pdf
    private lateinit var dateWeek: Pair<String,String>
    private lateinit var fileName: String
    private lateinit var folder : String


    fun generate() {
        //llamada de la funcion que retorna la fecha de inicio y fin de la semana
        dateWeek = getStarAndEndWeek()

        //nomombre de la carpeta para generar
        folder = "/Informes_Semanales_de_Recoleccion"

        //ubicando la carpeta en el directorio general
        val path = Environment.getExternalStorageDirectory().absolutePath + folder

        //generando la carpeta en caso de no existir
        val dir = File(path)
        if (!dir.exists()) {
            dir.mkdirs()
        }

        try {
            //nombre del pdf
            fileName = "informe_semana_${dateWeek.first.replace("0","")}.pdf"
            val file = File(dir, fileName )
            val fileOutputStream = FileOutputStream(file)

            //creando el documento
            val document = Document()
            PdfWriter.getInstance(document, fileOutputStream)
            document.open()


            // Agrega una imagen al PDF desde la carpeta "drawable"
            val drawableImage = BitmapFactory.decodeResource(resources, R.drawable.reccon_pdf)
            val byteArrayOutputStream = ByteArrayOutputStream()
            drawableImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val image = Image.getInstance(byteArray)
            image.scaleToFit(150f, 150f)
            image.alignment = Element.ALIGN_CENTER
            document.add(image)

            // Título
            val titleFont: Font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24f, BaseColor.BLACK)
            val titlePdf = Paragraph("Resumen Semanal de Recolección Cafetera", titleFont)
            titlePdf.alignment = Element.ALIGN_CENTER
            document.add(titlePdf)

            //ejecutando las funciones en cadena para que al terminar el pdf se cierre
            createTableAliment("active",context,"Precios vigentes",document){
                createTableAliment("archived",context,"Precios anteriores",document){
                    createTableWeek("Recolección sin alimentación",document,"no"){
                        createTableWeek("Recolección con alimentación",document,"yes"){
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
                    //tamano
                    table.widthPercentage = 100f
                    val headerNo = PdfPCell()

                    //funcion para crear un titulo a la tabla
                    titleTable(title,100f,document)

                    //funcion para crear items de la tabla
                    itemsTable(table,headerNo)

                    //de esta manera se pintyan cada uno de los resultados
                    for (item in query){
                        table.addCell(item.name_recolector)
                        table.addCell("${item.result}Kg")
                        price.priceSplit(item.total.toInt()){
                            table.addCell(it)
                        }
                        table.addCell(item.Estado)
                    }

                    //pintando la tabla en documento
                    document.add(table)

                    //generando una tabla nueva para el total
                    createTotal(document,aliment){
                        //respondiendo de que se finalizo para generar la siguiente tabla
                        ready()
                    }
                }else{
                    // en caso de no tener datos de respuesta de la base de datos pintamos un texto en el pdf para que el documento no falle
                    noData(title,document){
                        //respondiendo de que se finalizo para generar la siguiente tabla
                        ready()
                    }
                }
            }
        }
    }

    private fun createTotal(document: Document, aliment: String,ready: () -> Unit) {
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
                    // de estamanera se pueden dar un poco de estilos a las tablas
                    val table = PdfPTable(3)
                    table.widthPercentage = 100f
                    val header = PdfPCell()
                    header.horizontalAlignment = Element.ALIGN_CENTER
                    header.backgroundColor = BaseColor.LIGHT_GRAY


                    price.priceSplit(query[0].Precio){
                        //al hacer ersto se agrega el contenido de esta manera
                        header.phrase = Phrase("PRECIO:\n$it")
                        table.addCell(header)
                    }

                    header.phrase = Phrase("TOTAL RECOLECTADO:\n${query[0].cantidad}")
                    table.addCell(header)

                    price.priceSplit(query[0].total.toInt()){
                        header.phrase = Phrase("VALOR POR PAGAR:\n$it")
                        table.addCell(header)
                    }

                    document.add(table)

                    ready()
                }
            }
        }
    }

    fun getStarAndEndWeek(): Pair<String, String> {
        val calendar = Calendar.getInstance()

        // Obtener la fecha del día actual
        val fechaActual = calendar.time

        // Encontrar el día de la semana actual
        val diaSemanaActual = calendar.get(Calendar.DAY_OF_WEEK)

        // Calcular la cantidad de días para llegar al lunes anterior (considerando que domingo es 1 y lunes es 2)
        val days = if (diaSemanaActual == Calendar.MONDAY) {
            0
        } else if (diaSemanaActual == Calendar.TUESDAY){
            1
        } else if (diaSemanaActual == Calendar.WEDNESDAY){
            2
        } else if (diaSemanaActual == Calendar.THURSDAY){
            3
        } else if (diaSemanaActual == Calendar.FRIDAY){
            4
        } else if (diaSemanaActual == Calendar.SATURDAY){
            5
        } else {
            6
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
        tableTitle.widthPercentage = width
        tableTitle.spacingBefore = 20f

        val titleFont = FontFactory.getFont("arial", 14f, Font.BOLD, BaseColor.BLACK)
        val titlePrice = Paragraph(title,titleFont)
        val header = PdfPCell()
        header.horizontalAlignment = Element.ALIGN_CENTER
        header.backgroundColor = BaseColor.LIGHT_GRAY
        header.phrase = Phrase(titlePrice)
        tableTitle.addCell(header)

        document.add(tableTitle)
    }

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
                    tablePrices.widthPercentage = 50f
                    val header = PdfPCell()

                    titleTable(title,50f,document)

                    val cell = listOf(
                        "ALIMENTACIÓN",
                        "PRECIO"
                    )

                    for(item in cell) {
                        header.horizontalAlignment = Element.ALIGN_CENTER
                        header.phrase = Phrase(item)
                        tablePrices.addCell(header)
                    }

                    for(item in query){
                        header.horizontalAlignment = Element.ALIGN_CENTER
                        if (item.feeding.equals("yes")){
                            header.phrase = Phrase("si")
                            tablePrices.addCell(header)
                        }else{
                            header.phrase = Phrase("no")
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
            "NOMBRE",
            "RECOLECTADO",
            "TOTAL A PAGAR",
            "ESTADO",
        )

        for(it in cell) {
            header.horizontalAlignment = Element.ALIGN_CENTER
            header.phrase = Phrase(it)
            table.addCell(header)
        }
    }


    private fun finish(document: Document) {
        val titleFont: Font = FontFactory.getFont(FontFactory.HELVETICA, 15f, BaseColor.GRAY)
        val titlePdf = Paragraph("Con cada semana de recolección, avanzamos hacia la excelencia en nuestra producción cafetera. ¡Sigamos cosechando éxitos juntos!", titleFont)
        titlePdf.alignment = Element.ALIGN_CENTER
        document.add(titlePdf)
        document.close()
        location("$folder/$fileName")
    }


    private fun noData(message:String,document: Document,ready : () -> Unit) {
        val titleFont: Font = FontFactory.getFont(FontFactory.HELVETICA, 20f, BaseColor.GRAY)
        val titlePdf = Paragraph("No se registraron ${message}", titleFont)
        titlePdf.alignment = Element.ALIGN_CENTER
        document.add(titlePdf)
        ready()
    }

}