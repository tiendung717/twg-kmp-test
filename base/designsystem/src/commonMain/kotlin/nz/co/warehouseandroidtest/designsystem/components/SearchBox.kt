package nz.co.warehouseandroidtest.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import nz.co.warehouseandroidtest.designsystem.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SearchBox(
    value: TextFieldValue,
    modifier: Modifier = Modifier,
    onValueChange: (TextFieldValue) -> Unit = {},
    enabled: Boolean = true,
    readOnly: Boolean = false,
    placeholder: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder.orEmpty()
            )
        },
        enabled = enabled,
        readOnly = readOnly,
        textStyle = MaterialTheme.typography.bodyLarge,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
        ),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        interactionSource = interactionSource,
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "search_icon",
            )
        },
        trailingIcon = if (value.text.isNotEmpty()) {
            {
                Icon(
                    modifier = Modifier.clickable(
                        onClick = { onValueChange(TextFieldValue()) },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                    imageVector = Icons.Outlined.Clear,
                    contentDescription = "clear"
                )
            }
        } else null
    )
}

@Composable
private fun SearchBoxPreviewContainer(
    useDarkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    AppTheme(useDarkTheme = useDarkTheme) {
        Surface {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth().padding(16.dp),
            ) {
                content()
            }
        }
    }
}

@Preview
@Composable
private fun SearchBoxEmptyPreview() {
    SearchBoxPreviewContainer {
        SearchBox(
            value = TextFieldValue(""),
            placeholder = "Search products",
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun SearchBoxFilledPreview() {
    SearchBoxPreviewContainer {
        SearchBox(
            value = TextFieldValue("wool jacket"),
            placeholder = "Search products",
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun SearchBoxLongValuePreview() {
    SearchBoxPreviewContainer {
        SearchBox(
            value = TextFieldValue("womens merino wool blend winter jacket size medium charcoal"),
            placeholder = "Search products",
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun SearchBoxDisabledPreview() {
    SearchBoxPreviewContainer {
        SearchBox(
            value = TextFieldValue("wool jacket"),
            placeholder = "Search products",
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun SearchBoxReadOnlyPreview() {
    SearchBoxPreviewContainer {
        SearchBox(
            value = TextFieldValue("wool jacket"),
            placeholder = "Search products",
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun SearchBoxStatesPreview() {
    SearchBoxPreviewContainer {
        SearchBox(value = TextFieldValue(""), placeholder = "Search products", modifier = Modifier.fillMaxWidth())
        SearchBox(value = TextFieldValue("wool jacket"), modifier = Modifier.fillMaxWidth())
        SearchBox(value = TextFieldValue("wool jacket"), enabled = false, modifier = Modifier.fillMaxWidth())
    }
}

@Preview
@Composable
private fun SearchBoxDarkPreview() {
    SearchBoxPreviewContainer(useDarkTheme = true) {
        SearchBox(value = TextFieldValue(""), placeholder = "Search products", modifier = Modifier.fillMaxWidth())
        SearchBox(value = TextFieldValue("wool jacket"), modifier = Modifier.fillMaxWidth())
    }
}

@Preview
@Composable
private fun SearchBoxInteractivePreview() {
    var query by remember { mutableStateOf(TextFieldValue("wool jacket")) }
    SearchBoxPreviewContainer {
        SearchBox(
            value = query,
            onValueChange = { query = it },
            placeholder = "Search products",
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
