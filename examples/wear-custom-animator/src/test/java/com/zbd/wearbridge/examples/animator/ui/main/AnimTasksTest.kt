package com.zbd.wearbridge.examples.animator.ui.main

import org.junit.Assert.assertEquals
import org.junit.Test

class AnimTasksTest {

    @Test
    fun `clamp keeps in-range values`() {
        assertEquals(0.0, AnimTasks.clamp(0.0), 0.0)
        assertEquals(1.0, AnimTasks.clamp(1.0), 0.0)
        assertEquals(10.0, AnimTasks.clamp(10.0), 0.0)
    }

    @Test
    fun `clamp coerces out-of-range values`() {
        assertEquals(0.0, AnimTasks.clamp(-1.0), 0.0)
        assertEquals(10.0, AnimTasks.clamp(99.0), 0.0)
    }

    @Test
    fun `presets are within bounds`() {
        AnimTasks.PRESETS.forEach { preset ->
            assertEquals(preset, AnimTasks.clamp(preset), 0.0)
        }
    }
}
