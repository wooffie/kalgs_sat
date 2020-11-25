package sat.wooftown.util


/**
 * Many of literals for line
 * @param literals - литералы на вход
 */
class Clause(vararg literals: Literal) {

    val literals = mutableSetOf<Literal>()

    init {
        this.literals.addAll(literals)
    }

    /**
     * Add literal for clause
     */
    operator fun plus(other: Literal): Clause {
        literals.add(other)
        return this
    }

}

