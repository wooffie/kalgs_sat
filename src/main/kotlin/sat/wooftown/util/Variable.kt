package sat.wooftown.util


/**
 * Variable which takes from .cnf file
 * Index - name of variable (in DIMACS be like 1,2,3....)
 * @param index - name
 */

class Variable(
    val index: Int,
) {

    /**
     * Return Literal for Variable
     * @see sat.wooftown.util.Literal
     * @param isPositive
     * @return literal - Literal
     */
    fun toLiteral(isPositive: Boolean) = Literal(index * 2 + if (isPositive) 0 else 1)

    /**
     * Operator for creating positive Literal
     * @return positive literal
     */
    operator fun unaryPlus() = Literal(index * 2)

    /**
     * Operator for creating negative Literal
     * @return negative literal
     */
    operator fun unaryMinus() = Literal(index * 2 + 1)


    override fun equals(other: Any?): Boolean = other is Variable && index == other.index

    override fun hashCode(): Int {
        return index
    }

    override fun toString(): String {
        return (index + 1).toString()
    }
}

