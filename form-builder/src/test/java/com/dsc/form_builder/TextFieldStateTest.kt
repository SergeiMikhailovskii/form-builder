package com.dsc.form_builder

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource


internal class TextFieldStateTest {

    @Nested
    inner class DescribingStateChanges {
        private val classToTest: TextFieldState = TextFieldState(name = "")

        @Test
        fun `errors should be hidden`() {
            // Simulate an existing validation error
            classToTest.hasError = true
            classToTest.errorMessage = "error message"

            val newValue = "new value"      // Given a TextFieldState and a new value
            classToTest.change(newValue)    // When change is called

            assert(!classToTest.hasError)
            assert(classToTest.errorMessage.isEmpty())
        }

        @Test
        fun `state should be updated`() {
            val newValue = "new value"      // Given a TextFieldState and a new value
            classToTest.change(newValue)    // When change is called

            assert(classToTest.value == "new value")
        }

    }

    @Nested
    inner class DescribingValidation {

        private val classToTest: TextFieldState = TextFieldState(name = "")

        @Test
        fun `Validators_Required works correctly`() {

            // Verify when state hasn't changed
            val firstActual = classToTest.validateRequired("")
            assert(!firstActual)

            // Verify when state has changed
            classToTest.change("non-empty")
            val secondActual = classToTest.validateRequired("")
            assert(secondActual)
        }

        @ParameterizedTest
        @ArgumentsSource(EmailArgumentsProvider::class)
        fun `Validators_Email works correctly`(email: String, expected: Boolean) {
            classToTest.change(email)

            val actual = classToTest.validateEmail("expected validation: $expected")
            assert(actual == expected)
        }

        @ParameterizedTest
        @ArgumentsSource(MinCharsArgumentsProvider::class)
        fun `Validators_MinChars works correctly`(value: String, limit: Int, expected: Boolean) {
            classToTest.change(value)

            val actual = classToTest.validateMinChars(limit, "expected validation: $expected")
            assert(actual == expected)
        }

        @ParameterizedTest
        @ArgumentsSource(MaxCharsArgumentsProvider::class)
        fun `Validators_MaxChars works correctly`(value: String, limit: Int, expected: Boolean) {
            classToTest.change(value)

            val actual = classToTest.validateMaxChars(limit, "expected validation: $expected")
            assert(actual == expected)
        }

        @ParameterizedTest
        @ArgumentsSource(MinValueArgumentsProvider::class)
        fun `Validators_MinValue works correctly`(value: String, limit: Int, expected: Boolean) {
            classToTest.change(value)

            val actual = classToTest.validateMinValue(limit, "expected validation: $expected")
            assert(actual == expected)
        }

        @ParameterizedTest
        @ArgumentsSource(MaxValueArgumentsProvider::class)
        fun `Validators_MaxValue works correctly`(value: String, limit: Int, expected: Boolean) {
            classToTest.change(value)

            val actual = classToTest.validateMaxValue(limit, "expected validation: $expected")
            assert(actual == expected)
        }
    }
}