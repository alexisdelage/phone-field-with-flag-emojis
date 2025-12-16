package com.theodo.emojis_flags_and_unicode.phonefield

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun PhoneField(
    label: @Composable (() -> Unit),
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    defaultCountry: Country,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
    keyboardActions: KeyboardActions = KeyboardActions(),
    isError: Boolean = false,
    errorMessage: String? = null,
) {
    val focusManager = LocalFocusManager.current

    var selectedCountry by remember(defaultCountry) {
        mutableStateOf(defaultCountry)
    }
    var isCountryPickerOpen by remember { mutableStateOf(false) }

    val visualTransformation = remember(selectedCountry) {
        PhoneNumberVisualTransformation(selectedCountry)
    }

    Box(modifier = modifier) {
        Column {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = label,
                value = phoneNumber.withoutPrefix(selectedCountry),
                onValueChange = { newValue ->
                    val formattedValue = newValue.filterAndAddPrefix(selectedCountry)
                    onPhoneNumberChange(formattedValue)
                },
                leadingIcon = {
                    CountryPrefixButton(
                        country = selectedCountry,
                        onClick = {
                            focusManager.clearFocus()
                            isCountryPickerOpen = true
                        },
                    )
                },
                isError = isError,
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                singleLine = true,
                supportingText = if (isError && errorMessage != null) {
                    { Text(errorMessage) }
                } else {
                    null
                },
            )
        }

        if (isCountryPickerOpen) {
            CountryPrefixPickerModal(
                onDismiss = { isCountryPickerOpen = false },
                onCountrySelected = {
                    onPhoneNumberChange("")
                    selectedCountry = it
                },
            )
        }
    }
}
