package com.example.billapp

import android.Manifest
import android.widget.TextView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CompoundBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.google.zxing.BarcodeFormat
import android.widget.FrameLayout

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QRCodeScannerScreen(onScanResult: (String) -> Unit, onBack: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    var hasCameraPermission by remember { mutableStateOf(false) }

    LaunchedEffect(cameraPermissionState) {
        cameraPermissionState.launchPermissionRequest()
    }

    hasCameraPermission = cameraPermissionState.status.isGranted

    if (hasCameraPermission) {
        var barcodeView: CompoundBarcodeView? = null

        DisposableEffect(lifecycleOwner) {
            val observer = object : DefaultLifecycleObserver {
                override fun onResume(owner: LifecycleOwner) {
                    barcodeView?.resume()
                }

                override fun onPause(owner: LifecycleOwner) {
                    barcodeView?.pause()
                }
            }

            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }

        val density = LocalDensity.current

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AndroidView(
                factory = { context ->
                    CompoundBarcodeView(context).apply {
                        barcodeView = this
                        // 設置解碼器工廠以僅解碼 QR Code
                        barcodeView!!.decoderFactory = DefaultDecoderFactory(listOf(BarcodeFormat.QR_CODE))
                        decodeContinuous(object : BarcodeCallback {
                            override fun barcodeResult(result: BarcodeResult?) {
                                result?.text?.let {
                                    onScanResult(it)
                                }
                            }

                            override fun possibleResultPoints(resultPoints: MutableList<com.google.zxing.ResultPoint>?) {}
                        })
                        // 添加自定義文字
                        val textView = TextView(context).apply {
                            text = "請將條碼放置在取景框內進行掃描"
                            setTextColor(android.graphics.Color.WHITE)
                            textSize = 16f
                            with(density) {
                                setPadding(0, 0, 0, 16.dp.toPx().toInt())
                            }
                            layoutParams = FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.WRAP_CONTENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT,
                                android.view.Gravity.BOTTOM or android.view.Gravity.CENTER_HORIZONTAL
                            )
                        }
                        addView(textView)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onBack) {
                Text("返回")
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("需要相機權限來掃描 QR Code")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                Text("請求權限")
            }
        }
    }
}