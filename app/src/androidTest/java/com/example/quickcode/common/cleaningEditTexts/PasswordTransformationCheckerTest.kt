package com.example.quickcode.common.cleaningEditTexts

import android.text.method.PasswordTransformationMethod
import org.junit.Assert.assertEquals
import org.junit.Test

class PasswordTransformationCheckerTest{

    @Test
    fun twoTransformationMethodsAreTheSame() {
        // given
        val transformationMethod = PasswordTransformationMethod.getInstance()
        val passwordTransformationChecker =
            PasswordTransformationChecker(
                transformationMethod
            )

        // when
        val changed = passwordTransformationChecker.hasTransformationMethodChanged(PasswordTransformationMethod())

        // then
        assertEquals(false, changed)
    }

    @Test
    fun twoTransformationMethodsAreNotTheSame() {
        // given
        val transformationMethod = PasswordTransformationMethod.getInstance()
        val passwordTransformationChecker =
            PasswordTransformationChecker(
                transformationMethod
            )

        // when
        val changed = passwordTransformationChecker.hasTransformationMethodChanged(null)

        // then
        assertEquals(true, changed)
    }

}