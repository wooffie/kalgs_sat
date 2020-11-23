package sat.wooftown.util



import java.lang.StringBuilder

/*
Ответ на задачу выносимости
 */

/**
 * Набор показывающий какие переменные должны быть true/false
 * @param literals - *ответ*
 */
class Model(
    val literals: Set<Literal>,
) {

    override fun equals(other: Any?) = other is Model && literals == other.literals

    // не думаю что искать все решения буду, но на всякий
    override fun hashCode() = literals.hashCode()

    // debug only
    override fun toString(): String {
        val string = StringBuilder()
        string.append("solution: ")
        for (literal in literals.toList().sortedBy { it.value }){
            if (literal.isPositive){
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