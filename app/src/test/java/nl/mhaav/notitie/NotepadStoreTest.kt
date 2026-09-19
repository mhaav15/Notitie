package nl.mhaav.notitie

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class NotepadStoreTest {

    @get:Rule
    val folder = TemporaryFolder()

    private lateinit var store: NotepadStore

    @Before
    fun setUp() {
        store = NotepadStore(folder.newFolder("notepads"))
    }

    @Test
    fun emptyUntilWritten() {
        assertEquals("", store.getText(12))
    }

    @Test
    fun savesMultilinePerWidget() {
        store.setText(1, "eerste\nregel")
        store.setText(2, "ander blok")
        assertEquals("eerste\nregel", store.getText(1))
        assertEquals("ander blok", store.getText(2))
    }

    @Test
    fun renameAndDelete() {
        store.setText(3, "verhuis")
        store.rename(3, 9)
        assertEquals("", store.getText(3))
        assertEquals("verhuis", store.getText(9))
        store.delete(9)
        assertEquals("", store.getText(9))
    }
}
