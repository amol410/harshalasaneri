package com.healthapp.screens

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewerScreen(fileUri: String, navController: NavController) {

    val context = LocalContext.current
    val uri = Uri.parse(fileUri)

    var bitmapImage by remember { mutableStateOf<Bitmap?>(null) }
    var message by remember { mutableStateOf("Loading...") }

    LaunchedEffect(uri) {
        try {
            val resolver = context.contentResolver

            // Open file input
            val inputStream: InputStream? = resolver.openInputStream(uri)

            if (inputStream == null) {
                message = "Unable to open file."
                return@LaunchedEffect
            }

            // Save to temp file
            val tempFile = File(context.cacheDir, "temp_preview_file")
            val outputStream = FileOutputStream(tempFile)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()

            // Try image first
            val imageBitmap = BitmapFactory.decodeFile(tempFile.absolutePath)
            if (imageBitmap != null) {
                bitmapImage = imageBitmap
                return@LaunchedEffect
            }

            // Try PDF viewer
            val fd = android.os.ParcelFileDescriptor.open(
                tempFile,
                android.os.ParcelFileDescriptor.MODE_READ_ONLY
            ) ?: run {
                message = "Cannot open PDF."
                return@LaunchedEffect
            }

            val renderer = PdfRenderer(fd)
            val page = renderer.openPage(0)

            val pdfBitmap = Bitmap.createBitmap(
                page.width,
                page.height,
                Bitmap.Config.ARGB_8888
            )

            page.render(pdfBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            bitmapImage = pdfBitmap

            page.close()
            renderer.close()
            fd.close()

            message = ""

        } catch (e: Exception) {
            e.printStackTrace()
            message = "Unable to preview file."
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Viewer") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setDataAndType(uri, "*/*")
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        context.startActivity(intent)
                    }) {
                        Icon(Icons.Filled.OpenInNew, "Open Externally")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            if (bitmapImage != null) {
                Image(
                    bitmap = bitmapImage!!.asImageBitmap(),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                )
            } else {
                Text(message)
            }
        }
    }
}
