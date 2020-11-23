package sat.wooftown.util

import org.junit.Assert.*
import org.junit.Test

class LiteralTest {


    @Test
    fun testAllFunc(){
        val firstLiteral = Literal(3) // -1
        val secondLiteral = Literal(2) // 1

        assertEquals(firstLiteral,secondLiteral.inversion)
        assertEquals(firstLiteral.index,secondLiteral.index)
        assertEquals(Variable(1),firstLiteral.variable)
        assertEquals(Variable(1),secondLiteral.variable)
        assertTrue(secondLiteral.isPositive)
        assertFalse(firstLiteral.isPositive)
        assertNotEquals(firstLiteral,secondLiteral)
        println("String:")
        println("$firstLiteral, $secondLiteral")

    }
}