package sat.wooftown.solver.cdcl

import sat.wooftown.solver.Solver
import sat.wooftown.util.Formula
import sat.wooftown.util.Model

/**
 * Solver which implements conflict-driven clause learning algorithm
 * https://en.wikipedia.org/wiki/Conflict-driven_clause_learning
 */
class CDCLSolver(formulaToNumber: Pair<Formula, Int>) : Solver {


    override val picker = CDCLPicker(formulaToNumber.first, formulaToNumber.second)

    override fun solve(): Model? {
        with(picker) {
            while (canIterate()) {
                getNextLiteral()

                if (duplicated()) {
                    if (!backtrack()) return null

                } else {
                    val conflict = hasConflict()

                    if (conflict.first){
                        val learned = learn(conflict.second)
                        if (!backtrack(learned)) return null

                    }
                }
            }
            return getSolution()

        }
    }


}