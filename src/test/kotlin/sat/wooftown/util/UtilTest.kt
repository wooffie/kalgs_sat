package sat.wooftown.util

import org.junit.Assert
import org.junit.Test

class UtilTest {

    @Test
    fun variableTest(){
        val variable1 = Variable(1)
        val variable2 = Variable(2)

        Assert.assertEquals(Literal(2), variable1.toLiteral(true))
        Assert.assertEquals(Literal(3), variable1.toLiteral(false))
        Assert.assertEquals(variable1.toLiteral(false), -variable1)
        Assert.assertEquals(variable1.toLiteral(true), +variable1)
        Assert.assertNotEquals(variable1, variable2)
        Assert.assertEquals(variable1, Variable(1))
        /*  println("String for Variable(1):")
          println("${+variable1} and ${-variable1}")*/
    }

    @Test
    fun  literalTest(){
        val firstLiteral = Literal(3) // -1
        val secondLiteral = Literal(2) // 1

        Assert.assertEquals(firstLiteral, secondLiteral.inversion)
        Assert.assertEquals(firstLiteral.index, secondLiteral.index)
        Assert.assertEquals(Variable(1), firstLiteral.variable)
        Assert.assertEquals(Variable(1), secondLiteral.variable)
        Assert.assertTrue(secondLiteral.isPositive)
        Assert.assertFalse(firstLiteral.isPositive)
        Assert.assertNotEquals(firstLiteral, secondLiteral)
        println("String:")
        println("$firstLiteral, $secondLiteral")

    }

    @Test
    fun formulaTest(){
        val clause1 = Clause(Literal(1), Literal(5), Literal(24))
        val clause2 = Clause(Literal(4), Literal(42), Literal(52))
        val formula1 = Formula()
        val formula2 = Formula(clause1,clause2)
        Assert.assertNotEquals(formula2, formula1)
        formula1 * clause1 * clause2
        Assert.assertEquals(formula2, formula1)
        formula1 * clause1
        formula1 * clause2
        Assert.assertEquals(formula2, formula1)
    }

    @Test
    fun clauseTest(){
        val clause1 = Clause()
        val list = listOf(Literal(1), Literal(2),Literal(32))
        list.forEach {
            clause1 + it
        }
        val clause2 = Clause(Literal(1), Literal(2),Literal(32))
        Assert.assertEquals(clause1.literals, clause2.literals)
    }


}