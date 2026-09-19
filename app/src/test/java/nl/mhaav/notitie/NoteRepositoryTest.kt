package nl.mhaav.notitie

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class NoteRepositoryTest {

    @get:Rule
    val folder = TemporaryFolder()

    private lateinit var repo: NoteRepository

    @Before
    fun setUp() {
        repo = NoteRepository(folder.newFile("notes.json"))
    }

    @Test
    fun addIgnoresBlankText() {
        assertNull(repo.add("   "))
        assertTrue(repo.getAll().isEmpty())
    }

    @Test
    fun addToggleAndDelete() {
        val first = repo.add("Boodschappen")
        val second = repo.add("Bellen")
        assertNotNull(first)
        assertNotNull(second)

        val notes = repo.getAll()
        assertEquals(2, notes.size)
        assertEquals("Bellen", notes[0].text)
        assertEquals("Boodschappen", notes[1].text)
        assertFalse(notes[0].done)

        val toggled = repo.toggle(notes[0].id)
        assertNotNull(toggled)
        assertTrue(toggled!!.done)
        assertTrue(repo.getAll()[0].done)

        assertTrue(repo.delete(notes[0].id))
        assertEquals(1, repo.getAll().size)
        assertEquals("Boodschappen", repo.getAll()[0].text)
    }

    @Test
    fun persistsAcrossInstances() {
        val file = folder.newFile("shared.json")
        NoteRepository(file).add("Blijft bewaard")
        val reloaded = NoteRepository(file).getAll()
        assertEquals(1, reloaded.size)
        assertEquals("Blijft bewaard", reloaded[0].text)
    }
}
