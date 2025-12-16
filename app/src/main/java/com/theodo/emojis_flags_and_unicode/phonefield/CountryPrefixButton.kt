package com.theodo.emojis_flags_and_unicode.phonefield

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
internal fun CountryPrefixButton(
    country: Country,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .compensateForTopPaddingOnInnerTextField()
            .height(IntrinsicSize.Min),
    ) {
        Row(
            modifier = modifier
                .clip(countryPrefixButtonShape)
                .clickable(onClick = onClick, role = Role.Button)
                .minimumInteractiveComponentSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = country.phonePrefix,
                modifier = Modifier
                    .compensateForStartPaddingOnLabel()
                    .padding(vertical = 4.dp),
            )

            Icon(
                modifier = Modifier.padding(horizontal = 4.dp),
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }

        VerticalDivider(
            modifier = Modifier.padding(
                horizontal = 4.dp,
                vertical = 12.dp,
            ),
        )
    }
}

private val countryPrefixButtonShape: Shape
    @Composable
    get() = MaterialTheme.shapes.medium.copy(
        bottomStart = ZeroCornerSize,
        bottomEnd = ZeroCornerSize,
    )

@Composable
private fun Modifier.compensateForTopPaddingOnInnerTextField(): Modifier {
    return this.padding(top = 4.dp)
}

@Composable
private fun Modifier.compensateForStartPaddingOnLabel(): Modifier {
    return this.padding(start = 4.dp)
}
