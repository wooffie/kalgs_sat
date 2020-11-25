package sat.wooftown.solver.dpll

import sat.wooftown.solver.CommonClauses
import sat.wooftown.solver.Picker
import sat.wooftown.util.*
import java.util.*

/**
 * Picker for DPLL algorithm
 * @param formula - input
 * @param size - number of variables
 */
class NaivePicker(
    formula: Formula,
    private val size: Int,
) : Picker {

    /*
    https://sun9-33.userapi.com/XT6Tk5CLdiD-hHcvmIexNwofQpcCsjOaTynbIw/tlrCkXCnD8Q.jpg
    */
    /**
     * *Tree* which display which literal we take next
     */
    private val checkQueue = LinkedList<Literal>()

    /**
     * Common clauses
     * @see com.wooftown.solver.dpll.NaivePicker.hasConflict
     */
    private val commonClauses = CommonClauses(formula,size)


    /**
     * Returns common clauses
     */
    fun getCommonClause(literal: Literal): MutableSet<Clause> {
        return commonClauses[literal]
    }

    /**
     * Array for Solution
     */
    private val solutions = BooleanArray(size)

    /**
     * Write down solved literal
     * @param literal - which literal
     * @param isPositive - sign
     */
    operator fun set(literal: Literal, isPositive: Boolean) {
        solutions[literal.variable.index] = isPositive
        currentIndex = literal.index + 1
    }

    /**
     * Get value of solution
     * @param literal - literal for his value
     */
    operator fun get(literal: Literal) = solutions[literal.index]

    override fun getSolution(): Model {
        val result = mutableSetOf<Literal>()
        for (literal in solutions.indices) {
            result.add(Variable(literal).toLiteral(solutions[literal]))
        }
        return Model(result)
    }

    override fun hasConflict(): Pair<Boolean, Clause?> {
        throw NotImplementedError("Naive picker cannot choose problem clause")
    }


    init {
        checkQueue.add(Literal(0))
        checkQueue.add(Literal(1))
    }

    /**
     * Pointer of picker
     */
    private var currentIndex = 0

    /**
     * Back to literal, if we have conflict
     */
    fun backtrack(literal: Literal) {
        currentIndex = literal.index
    }

    /**
     * Check index
     */
    fun notChecked(literal: Literal): Boolean {
        return literal.index >= currentIndex
    }

    /**
     * True if we have right solution
     */
    fun haveSolution() = size <= currentIndex

    /**
     * Pick next literal
     */
    fun getNextLiteral(): Literal? =
        if (checkQueue.isEmpty()) {
            null
        } else {
            checkQueue.removeLast()
        }

    /**
     * Add literals in queue
     */
    fun prepareNextLiterals() {
        checkQueue.add(Literal(currentIndex * 2))
        checkQueue.add(Literal(currentIndex * 2 + 1))
    }

}