package com.zbd.wearbridge.examples.dpi.ui.main

import org.junit.Assert.assertEquals
import org.junit.Test

class DpiTasksTest {

    @Test
    fun `clamp keeps in-range values`() {
        assertEquals(200, DpiTasks.clamp(200))
        assertEquals(640, DpiTasks.clamp(640))
        assertEquals(120, DpiTasks.clamp(120))
    }

    @Test
    fun `clamp coerces out-of-range values`() {
        assertEquals(120, DpiTasks.clamp(0))
        assertEquals(640, DpiTasks.clamp(9999))
        assertEquals(120, DpiTasks.clamp(-50))
    }

    @Test
    fun `presets are within bounds`() {
        DpiTasks.PRESETS.forEach { preset ->
            assertEquals(preset, DpiTasks.clamp(preset))
        }
    }
}
