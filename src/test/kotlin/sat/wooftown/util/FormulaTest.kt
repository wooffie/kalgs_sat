package sat.wooftown.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class FormulaTest {

    @Test
    fun testAllFunc(){
        val clause1 = Clause(Literal(1), Literal(5), Literal(24))
        val clause2 = Clause(Literal(4), Literal(42), Literal(52))
        val formula1 = Formula()
        val formula2 = Formula(clause1,clause2)
        assertNotEquals(formula2,formula1)
        formula1 * clause1 * clause2
        assertEquals(formula2,formula1)
        formula1 * clause1
        formula1 * clause2
        assertEquals(formula2,formula1)
    }
}