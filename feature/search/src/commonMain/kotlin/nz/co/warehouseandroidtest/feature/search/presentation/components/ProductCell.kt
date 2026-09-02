package nz.co.warehouseandroidtest.feature.search.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import nz.co.warehouseandroidtest.designsystem.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ProductCell(
    title: String,
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    description: String? = null,
    price: String? = null,
    rating: Double? = null,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick),
    ) {
        Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().aspectRatio(1f),
            )

            rating?.let {
                RatingBadge(
                    rating = it,
                    modifier = Modifier.align(Alignment.TopStart).padding(8.dp),
                )
            }
        }

        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
            price?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 6.dp),
            )

            description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 6.dp),
                )
            }
        }
    }
}

@Composable
private fun RatingBadge(rating: Double, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = RatingTint,
                modifier = Modifier.size(14.dp),
            )
            Text(
                text = formatRating(rating),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

private fun formatRating(rating: Double): String {
    val tenths = (rating * 10).toLong()
    return "${tenths / 10}.${tenths % 10}"
}

private val RatingTint = Color(0xFFFFC107)

@Composable
private fun ProductCellPreviewContainer(
    useDarkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    AppTheme(useDarkTheme = useDarkTheme) {
        Surface(color = MaterialTheme.colorScheme.outlineVariant) {
            Box(modifier = Modifier.padding(1.dp)) { content() }
        }
    }
}

@Preview
@Composable
private fun ProductCellPreview() {
    ProductCellPreviewContainer {
        ProductCell(
            title = "BRDRLESS Faye Windbreaker Jacket",
            price = "$45.00",
            modifier = Modifier.width(180.dp),
        )
    }
}

@Preview
@Composable
private fun ProductCellWithDescriptionPreview() {
    ProductCellPreviewContainer {
        ProductCell(
            title = "BRDRLESS Bindy Long Sleeve Balloon Bomber Jacket",
            price = "$45.00",
            description = "Relaxed balloon-sleeve bomber with a cropped cut and ribbed trims.",
            modifier = Modifier.width(180.dp),
        )
    }
}

@Preview
@Composable
private fun ProductCellWithRatingPreview() {
    ProductCellPreviewContainer {
        ProductCell(
            title = "H&H Women's Nylon Funnel Neck Bomber Jacket",
            price = "$45.00",
            rating = 4.5,
            modifier = Modifier.width(180.dp),
        )
    }
}

@Preview
@Composable
private fun ProductCellGridPreview() {
    ProductCellPreviewContainer {
        Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
            ProductCell(
                title = "BRDRLESS Bindy Long Sleeve Balloon Bomber Jacket",
                price = "$45.00",
                modifier = Modifier.weight(1f),
            )
            ProductCell(
                title = "Rivet High Visibilty Bomber Jacket",
                price = "$60.00",
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Preview
@Composable
private fun ProductCellLongDescriptionPreview() {
    ProductCellPreviewContainer {
        ProductCell(
            title = "Jasper J Loop Desk Gloss White",
            price = "$339.00",
            description = "Minimalist yet elegant, ideal for home offices or small business set-ups.",
            modifier = Modifier.width(180.dp),
        )
    }
}

@Preview
@Composable
private fun ProductCellDarkPreview() {
    ProductCellPreviewContainer(useDarkTheme = true) {
        ProductCell(
            title = "BRDRLESS Faye Windbreaker Jacket",
            price = "$45.00",
            modifier = Modifier.width(180.dp),
        )
    }
}
