package com.zbd.wearbridge.examples.permlist.ui.main

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PermTasksTest {

    @Test
    fun `planned task ids match server validator shape`() {
        assertTrue(PermTasks.isValidTaskId(PermTasks.LIST))
        assertTrue(PermTasks.isValidTaskId(PermTasks.SET))
        assertTrue(PermTasks.isValidTaskId(PermTasks.APPOPS_GET))
        assertTrue(PermTasks.isValidTaskId(PermTasks.APPOPS_SET))
    }

    @Test
    fun `invalid ids rejected`() {
        assertFalse(PermTasks.isValidTaskId(""))
        assertFalse(PermTasks.isValidTaskId("NOPE"))
        assertFalse(PermTasks.isValidTaskId("privacy perm list"))
        assertFalse(PermTasks.isValidTaskId("privacy.perm.list; rm -rf"))
    }
}
