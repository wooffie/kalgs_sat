package sat.wooftown.util


/**
 * Container which stores variable and sign
 * Value show us "shifted" index
 * If value is  even - variable is positive
 * If value is odd - variable is negative
 * @param value - значение переменной
 */
class Literal(
    val value: Int,
) {

    /**
     * Get inverted literal
     */
    val inversion: Literal
        get() = Literal(value xor 1) // Not init cause of StackOverflow

    /**
     * Get index of variable
     */
    val index = value / 2

    /**
     * Get variable
     */
    val variable = Variable(index)

    /**
     * Returns sign
     */
    val isPositive = value and 1 == 0

    override fun equals(other: Any?): Boolean = other is Literal && value == other.value

    override fun hashCode() = value

    override fun toString() = if (isPositive)
        "+${index + 1} (index: $value)"
    else
        "-${index + 1} (index: $value)"


}
