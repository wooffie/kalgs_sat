package sat.wooftown.util

import org.junit.Assert.*
import org.junit.Test

class VariableTest {


    @Test
    fun testAllFunc(){
        val variable1 = Variable(1)
        val variable2 = Variable(2)

        assertEquals(Literal(2),variable1.toLiteral(true))
        assertEquals(Literal(3),variable1.toLiteral(false))
        assertEquals(variable1.toLiteral(false),-variable1)
        assertEquals(variable1.toLiteral(true),+variable1)
        assertNotEquals(variable1,variable2)
        assertEquals(variable1,Variable(1))
      /*  println("String for Variable(1):")
        println("${+variable1} and ${-variable1}")*/
    }

}