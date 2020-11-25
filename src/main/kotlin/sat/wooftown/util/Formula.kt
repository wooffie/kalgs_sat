package sat.wooftown.util

/**
 * Lots of clauses
 * @param clauses
 */
class Formula(
    vararg clauses: Clause,
) {

    /**
     * Set of clauses
     */
    val clauses = mutableSetOf<Clause>()

    init {
        this.clauses.addAll(clauses)
    }

    /**
     * Add one more clause to Set
     */
    operator fun times(other: Clause): Formula {
        clauses.add(other)
        return this
    }

    override fun equals(other: Any?): Boolean = other is Formula && clauses == other.clauses

    override fun hashCode(): Int {
        return clauses.hashCode()
    }

}