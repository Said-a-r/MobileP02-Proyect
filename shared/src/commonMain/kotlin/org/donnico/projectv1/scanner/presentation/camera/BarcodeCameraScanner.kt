package org.donnico.projectv1.scanner.presentation.camera

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun BarcodeCameraScanner(
    isScanning: Boolean,
    onBarcodeDetected: (String) -> Unit,
    modifier: Modifier = Modifier
)
