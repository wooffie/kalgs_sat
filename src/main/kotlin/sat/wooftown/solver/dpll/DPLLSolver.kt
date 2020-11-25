package sat.wooftown.solver.dpll

import sat.wooftown.solver.Solver
import sat.wooftown.util.Formula
import sat.wooftown.util.Literal
import sat.wooftown.util.Model

/**
 * Solver which implements Davis–Putnam–Logemann–Loveland algorithm
 * https://en.wikipedia.org/wiki/DPLL_algorithm
 * @param formulaToNumber - input
 */

class DPLLSolver(formulaToNumber: Pair<Formula, Int>) : Solver {


    override val picker: NaivePicker = NaivePicker(formulaToNumber.first, formulaToNumber.second)


    override fun solve(): Model? {
        var next = picker.getNextLiteral()

        while (next != null) {
            picker[next] = next.isPositive

            when {
                hasConflicts(next) -> picker.backtrack(next)
                picker.haveSolution() -> return picker.getSolution()
                else -> picker.prepareNextLiterals()
            }

            next = picker.getNextLiteral()
        }

        return null
    }

    /**
     * Analyze picked literal and find conflict
     * @param picked - picked literal
     * @return has conflicts or not
     */
    private fun hasConflicts(picked: Literal): Boolean {
        for (clause in  picker.getCommonClause(picked)) {
            var flag = false
            for (literal in clause.literals) {
                if (picker.notChecked(literal) || literal.isPositive == picker[literal]) {
                    flag = true
                    break
                }
            }
            if (!flag) {
                return true
            }
        }
        return false
    }

}