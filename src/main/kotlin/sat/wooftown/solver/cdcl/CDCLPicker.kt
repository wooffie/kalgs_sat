package sat.wooftown.solver.cdcl

import sat.wooftown.solver.CommonClauses
import sat.wooftown.solver.Picker
import sat.wooftown.util.*

/**
 * Picker for CDCLSolver
 */
class CDCLPicker(
    formula: Formula,
    private val size: Int,
) : Picker {

    /**
     * Common clauses for formula
     */
    private val commonClauses = CommonClauses(formula, size)

    /**
     * Parent clauses - clauses from which followed the exception by the rule of pure variable
     */
    private val parentClauses: MutableList<Clause?> = MutableList(size * 2) { null }

    /**
     * Solution list
     */
    private val solutions: MutableList<Literal?> = MutableList(size * 2) { null }

    /**
     * Get mapping of solution
     * @return map of solution
     */
    private fun valuesToMapVariable(): Map<Variable, Boolean> {
        val assignments = mutableMapOf<Variable, Boolean>()
        for (it in 0 until pickedCount) {
            assignments[solutions[it]!!.variable] = solutions[it]!!.isPositive
        }
        return assignments
    }

    /**
     * Get solution in Model type
     * @return model
     */
    override fun getSolution(): Model {
        return Model(solutions.filterNotNull().toSet())
    }

    /**
     * Decision level , in more details see in net "implication graph"
     */
    private val decisionLevels = MutableList(size * 2) { -1 }

    /**
     * Current decision level
     */
    private var currentLevel = -1

    /**
     * Next variable for picking
     */
    private var nextIndex = 0

    /**
     * How many variable has been assigned
     */
    private var pickedCount = 0

    /**
     * Iterating for while cycle
     */
    fun canIterate() = pickedCount < size


    /**
     * Find duplicates in solution
     * @return has duplicates or not
     */
    fun duplicated(): Boolean {
        val literal = solutions[pickedCount]!!
        for (it in pickedCount - 1 downTo 0) {
            val other = solutions[it]!!
            if (other.index == literal.index) {
                return true
            }
        }
        pickedCount++
        return false
    }


    /**
     * Check next literal for conflict
     */
    override fun hasConflict(): Pair<Boolean, Clause?> {
        // литерал который сейчас определили
        val literal = solutions[pickedCount - 1]!!
        for (clause in commonClauses[literal]) {
            // если у нас нету неопределённых литералов в наборе и он неверен, то вернём неверный clause
            if (!analyze(clause)) {
                return true to clause
            }
        }
        return false to null
    }

    /**
     * Analyze clause with picked literal
     */
    private fun analyze(clause: Clause): Boolean {
        val assignments = valuesToMapVariable()
        var uncheckedCount = 0
        var lastLiteral: Literal? = null
        for (literal in clause.literals) {
            if (assignments[literal.variable] == literal.isPositive) {
                return true
            }
            if (assignments[literal.variable] == null) {
                uncheckedCount++
                lastLiteral = literal
            }
        }
        if (uncheckedCount == 0) {
            return false
        }
        if (uncheckedCount == 1 && lastLiteral != null) {
            addToValues(lastLiteral, clause)
        }

        return true
    }


    /**
     * Get next literal
     */
    fun getNextLiteral() {
        if (pickedCount >= nextIndex) {
            val assignments = valuesToMapVariable()
            for (it in 0 until size) {
                val variable = Variable(it)
                if (assignments[variable] == null) {
                    addToValues(+variable)
                    return
                }
            }
            return
            //throw NoSuchElementException("No next elements") // ????
        }

    }


    /**
     * Add value for solution
     * @param literal - literal to add
     * @param parent - parrentClause
     */
    private fun addToValues(literal: Literal, parent: Clause? = null) {

        solutions.subList(0, nextIndex).forEach {
            if (it == literal) {
                return    // if exists
            }
        }
        solutions[nextIndex] = literal
        parentClauses[nextIndex] = parent
        if (parent == null) {
            currentLevel++
        }
        decisionLevels[nextIndex] = currentLevel
        nextIndex++
    }


    /**
     * Backtrack if picked literal has conflicts
     * Backtrack goes for level of decision
     */
    fun backtrack(conflictedClause: Clause? = null): Boolean {


        var backtrackingIndex = 0
        while (decisionLevels[backtrackingIndex] != decisionLevels[pickedCount - 1]) {
            backtrackingIndex++
        }
        if (decisionLevels[backtrackingIndex] == -1) {
            return false
        }

        if (conflictedClause != null) {
            var backtrackTo = backtrackingIndex - 1

            while (backtrackTo >= 0) {
                for (literal in conflictedClause.literals) {
                    if (solutions[backtrackTo] == literal) {
                        backtrackingIndex = pickedCount - 1
                        while (decisionLevels[backtrackingIndex] != decisionLevels[backtrackTo]) {
                            backtrackingIndex--
                        }
                        backtrackingIndex++
                        break
                    }
                }
                backtrackTo--
            }
        }

        solutions[backtrackingIndex] = solutions[backtrackingIndex]!!.inversion
        nextIndex = backtrackingIndex + 1
        pickedCount = backtrackingIndex

        decisionLevels[backtrackingIndex] = if (backtrackingIndex == 0) {
            -1
        } else {
            decisionLevels[backtrackingIndex - 1]
        }
        currentLevel = decisionLevels[backtrackingIndex]

        return true
    }


    /**
     * If we can conflict caused of pure elimination, we need to learn those clauses
     */
    fun learn(conflictedClause: Clause?): Clause? {
        val literalParent = parentClauses[pickedCount - 1]
        val conflictedVariable = solutions[pickedCount - 1]!!.variable
        if (conflictedClause != null && literalParent != null) {
            val result = Clause()
            (conflictedClause.literals + literalParent.literals)
                .filter { it.variable != conflictedVariable }
                .forEach {
                    result + it
                }
            commonClauses.updateClauses(result)
            return result
        }
        return null
    }

}