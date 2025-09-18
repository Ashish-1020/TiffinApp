package com.example.tiffinapp.cart.util


import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.tiffinapp.core.data.CartItemDto
import java.io.IOException

@RequiresApi(Build.VERSION_CODES.Q)
fun generateStyledInvoicePdf(
    context: Context,
    invoiceNumber: String,
    date: String,
    dueDate: String,
    customerName: String,
    customerCompany: String,
    customerAddress: String,
    customerPhone: String,
    items: List<CartItemDto>
) {
    val document = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
    val page = document.startPage(pageInfo)
    val canvas = page.canvas

    val paint = Paint().apply {
        color = Color.BLACK
        textSize = 14f
        isAntiAlias = true
    }

    var y = 40
    paint.isFakeBoldText = true
    canvas.drawText("Tiffin App - Food Service Invoice", 40f, y.toFloat(), paint)
    paint.isFakeBoldText = false
    y += 30

    canvas.drawText("Invoice Number: $invoiceNumber", 40f, y.toFloat(), paint)
    y += 20
    canvas.drawText("Date: $date", 40f, y.toFloat(), paint)
    y += 20
    canvas.drawText("Due Date: $dueDate", 40f, y.toFloat(), paint)
    y += 30

    paint.isFakeBoldText = true
    canvas.drawText("Bill To:", 40f, y.toFloat(), paint)
    paint.isFakeBoldText = false
    y += 20
    canvas.drawText("• Name: $customerName", 40f, y.toFloat(), paint)
    y += 20
    canvas.drawText("• Address: $customerAddress", 40f, y.toFloat(), paint)
    y += 20
    canvas.drawText("• Phone: $customerPhone", 40f, y.toFloat(), paint)
    y += 30

    paint.isFakeBoldText = true
    canvas.drawText("Description", 40f, y.toFloat(), paint)
    canvas.drawText("Qty", 250f, y.toFloat(), paint)
    canvas.drawText("Unit", 320f, y.toFloat(), paint)
    canvas.drawText("Total", 420f, y.toFloat(), paint)
    paint.isFakeBoldText = false
    y += 20

    var totalAmount = 0.0
    items.forEach { item ->
        val discountedPrice = item.price - (item.price * item.offer / 100)
        val total = discountedPrice * item.quantity
        totalAmount += total

        canvas.drawText(item.name, 40f, y.toFloat(), paint)
        canvas.drawText(item.quantity.toString(), 250f, y.toFloat(), paint)
        canvas.drawText("₹%.2f".format(discountedPrice), 320f, y.toFloat(), paint)
        canvas.drawText("₹%.2f".format(total), 420f, y.toFloat(), paint)
        y += 20
    }

    y += 20
    paint.isFakeBoldText = true
    canvas.drawText("Total Amount: ₹%.2f".format(totalAmount), 320f, y.toFloat(), paint)
    paint.isFakeBoldText = false

    document.finishPage(page)

    val fileName = "TiffinInvoice_${System.currentTimeMillis()}.pdf"
    val contentValues = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, fileName)
        put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
        put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
    }

    val resolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

    if (uri != null) {
        try {
            resolver.openOutputStream(uri).use { output ->
                document.writeTo(output)
                Toast.makeText(context, "Invoice saved to Downloads", Toast.LENGTH_LONG).show()
            }
        } catch (e: IOException) {
            Toast.makeText(context, "Failed to save PDF: ${e.message}", Toast.LENGTH_LONG).show()
        }
    } else {
        Toast.makeText(context, "Unable to access Downloads folder", Toast.LENGTH_LONG).show()
    }

    document.close()
}
