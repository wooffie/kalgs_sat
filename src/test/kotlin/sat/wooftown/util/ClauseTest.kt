package sat.wooftown.util

import org.junit.Assert.assertEquals
import org.junit.Test

class ClauseTest {

    @Test
    fun testAllFunc(){
        val clause1 = Clause()
        val list = listOf(Literal(1), Literal(2),Literal(32))
        list.forEach {
            clause1 + it
        }
        val clause2 = Clause(Literal(1), Literal(2),Literal(32))
        assertEquals(clause1.literals,clause2.literals)
    }

}