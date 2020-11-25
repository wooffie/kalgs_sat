package sat.wooftown.util


/**
 * Many of literals which show us result of solving
 * If in set positive literal -> variable must be TRUE, otherwise FALSE
 * @param literals - result
 */
class Model(
    val literals: Set<Literal>,
) {

    override fun equals(other: Any?) = other is Model && literals == other.literals

    override fun hashCode() = literals.hashCode()

    override fun toString(): String {
        val string = StringBuilder()
        string.append("solution: ")
        for (literal in literals.toList().sortedBy { it.value }) {
            if (literal.isPositive) {
                string.append("+")
            } else {
                string.append("-")
            }
            string.append(literal.variable)
            string.append("  ")
        }
        return string.toString()
    }
}