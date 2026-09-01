package nz.co.warehouseandroidtest.shared.presentation

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nz.co.warehouseandroidtest.designsystem.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import nz.co.warehouseandroidtest.shared.resources.Res
import nz.co.warehouseandroidtest.shared.resources.no_internet_message
import nz.co.warehouseandroidtest.shared.resources.no_internet_retry
import nz.co.warehouseandroidtest.shared.resources.no_internet_title

@Composable
fun NoInternetDialog(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {},
        title = { Text(stringResource(Res.string.no_internet_title)) },
        text = { Text(stringResource(Res.string.no_internet_message)) },
        confirmButton = {
            Button(onClick = onRetry) {
                Text(stringResource(Res.string.no_internet_retry))
            }
        },
        shape = RoundedCornerShape(8.dp)
    )
}

@Preview
@Composable
private fun NoInternetDialogPreview() {
    AppTheme { NoInternetDialog(onRetry = {}) }
}
