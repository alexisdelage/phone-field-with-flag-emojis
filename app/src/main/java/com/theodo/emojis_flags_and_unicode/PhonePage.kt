package com.theodo.emojis_flags_and_unicode

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theodo.emojis_flags_and_unicode.phonefield.Country
import com.theodo.emojis_flags_and_unicode.phonefield.PhoneField

@Composable
fun PhonePage(modifier: Modifier = Modifier) {
    var phoneNumber by remember { mutableStateOf("") }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Phone Page",
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 24.dp),
            )

            PhoneField(
                label = { Text("Phone Number") },
                phoneNumber = phoneNumber,
                onPhoneNumberChange = { phoneNumber = it },
                defaultCountry = Country.France,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
