package org.donnico.projectv1.scanner.presentation.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
actual fun BarcodeCameraScanner(
    isScanning: Boolean,
    onBarcodeDetected: (String) -> Unit,
    modifier: Modifier
) {
    Box(modifier = modifier.background(Color.Black))
}
