package sat.wooftown.solver

import sat.wooftown.util.Clause
import sat.wooftown.util.Formula
import sat.wooftown.util.Literal

/**
 * Mapping formula for List with Clauses
 * Can return clauses which contains VARIABLE
 * @param formula
 * @param size
 */
class CommonClauses(formula : Formula,size : Int) {

    private val data = List<MutableSet<Clause>>(size ) { mutableSetOf()}

    init {
        formula.clauses.forEach { clause ->
            clause.literals.forEach {
                data[it.index].add(clause)
            }
        }
    }

    /**
     * Get set of clauses
     * @param literal
     */
    operator fun get(literal: Literal) = data[literal.index]

    /**
     * Add more clauses
     * @param clause
     */
    fun updateClauses(clause: Clause){
        clause.literals.forEach {
            data[it.index].add(clause)
        }
    }
}