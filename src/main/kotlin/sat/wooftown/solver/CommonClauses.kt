package sat.wooftown.solver

import sat.wooftown.util.Clause
import sat.wooftown.util.Formula
import sat.wooftown.util.Literal

class CommonClauses(formula : Formula,size : Int) {

    private val data = List<MutableSet<Clause>>(size ) { mutableSetOf()}

    init {
        formula.clauses.forEach { clause ->
            clause.literals.forEach {
                data[it.index].add(clause)
            }
        }
    }

    operator fun get(literal: Literal) = data[literal.index]

    fun updateClauses(clause: Clause){
        clause.literals.forEach {
            data[it.index].add(clause)
        }
    }
}