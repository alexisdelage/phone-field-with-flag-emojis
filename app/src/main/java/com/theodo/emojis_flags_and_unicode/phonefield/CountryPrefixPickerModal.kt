package com.theodo.emojis_flags_and_unicode.phonefield

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.CollectionItemInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.collectionItemInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.google.i18n.phonenumbers.PhoneNumberUtil

private val bottomSheetTopOffset = 100.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryPrefixPickerModal(
    onDismiss: () -> Unit,
    onCountrySelected: (Country) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Content(
            dismissBottomSheetModal = onDismiss,
            onCountrySelected = onCountrySelected,
        )
    }
}

@Composable
private fun Content(
    dismissBottomSheetModal: () -> Unit,
    onCountrySelected: (Country) -> Unit,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val countries = remember {
        PhoneNumberUtil.getInstance().getAllCountries().sortedBy { it.displayName }
    }

    Column(
        modifier = Modifier
            .heightIn(max = screenHeight - bottomSheetTopOffset)
            .padding(vertical = 16.dp),
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f, fill = false),
        ) {
            items(countries) { country ->
                Row(
                    modifier = Modifier
                        .clickable(role = Role.Button) {
                            onCountrySelected(country)
                            dismissBottomSheetModal()
                        }.semantics {
                            collectionItemInfo = CollectionItemInfo(
                                rowIndex = countries.indexOf(country),
                                rowSpan = 1,
                                columnIndex = 0,
                                columnSpan = 1,
                            )
                        }.fillMaxWidth()
                        .minimumInteractiveComponentSize()
                        .padding(
                            vertical = 8.dp,
                            horizontal = 16.dp,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier.semantics {
                            contentDescription = "${country.displayName} (${country.phonePrefix})"
                        },
                        text = "${country.flagEmoji} ${country.displayName} (${country.phonePrefix})",
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = dismissBottomSheetModal,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Text("Annuler")
        }
    }
}
