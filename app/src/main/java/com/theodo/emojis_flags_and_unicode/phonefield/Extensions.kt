package com.theodo.emojis_flags_and_unicode.phonefield

import com.google.i18n.phonenumbers.PhoneNumberUtil

/**
 * Filter input for phone number field, and add the international prefix.
 * Use only on a local phone number (without prefix).
 */
internal fun String.filterAndAddPrefix(country: Country): String {
    val filteredPhoneNumber = this
        .filter { it.isDigit() }
        .removeLeadingZeroIfFrench(country)

    val phoneNumberWithPrefix = filteredPhoneNumber.withPrefix(country)

    return phoneNumberWithPrefix
}

/**
 * Remove leading zero for French phone numbers if applicable
 * This is specifically for French numbers where the leading zero is not needed
 */
internal fun String.removeLeadingZeroIfFrench(country: Country): String {
    // Remove the zero only after typing at least 2 digits
    if (country.phonePrefix == "+33" && this.startsWith("0") && this.length >= 2) {
        return this.removePrefix("0")
    }
    return this
}

/**
 * Add the country prefix to a local phone number
 * If empty, do not add the prefix (keep empty).
 * This is used to format the phone number correctly for display or storage
 */
internal fun String.withPrefix(country: Country): String {
    if (this.isBlank()) return this

    return "${country.phonePrefix}$this"
}

/**
 * Remove the country prefix from a phone number
 * This is used to extract the local part of the phone number for processing
 */
internal fun String.withoutPrefix(country: Country): String {
    return this
        .removePrefix(country.phonePrefix)
        .trimStart()
}

/**
 * Extract the country from a phone number string.
 * This process can be intensive, be sure to remember the result if you use it in a Composable.
 */
fun String.extractCountry(): Country? {
    val phoneUtil = PhoneNumberUtil.getInstance()
    val parsedNumber = try {
        phoneUtil.parse(this, null)
    } catch (e: Exception) {
        return null
    }
    val prefix = "+" + parsedNumber.countryCode

    val allCountries = phoneUtil.getAllCountries()

    return allCountries.firstOrNull { it.phonePrefix == prefix }
}

/**
 * Get a list of all countries supported by libphonenumber with their phone prefixes.
 */
internal fun PhoneNumberUtil.getAllCountries(): List<Country> {
    return supportedRegions.map { regionCode ->
        Country(regionCode)
    }
}

/**
 * Check if a phone number is valid.
 */
fun String.isValidPhoneNumber(): Boolean {
    val phoneUtil = PhoneNumberUtil.getInstance()
    return try {
        val parsedNumber = phoneUtil.parse(this, null)
        phoneUtil.isValidNumber(parsedNumber)
    } catch (e: Exception) {
        false
    }
}

/**
 * Format and prettify a phone number for display.
 */
fun String.formatPhoneNumber(): String {
    val phoneUtil = PhoneNumberUtil.getInstance()
    try {
        val parsedNumber = phoneUtil.parse(this, null)
        return phoneUtil.format(parsedNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
    } catch (e: Exception) {
        return this // Return the original string if parsing fails
    }
}
