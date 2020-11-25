package sat.wooftown.solver

import sat.wooftown.util.Model

interface Solver {

    /**
     * Picker for solver
     */
    val picker : Picker

    /**
     * Get solution
     */
    fun solve(): Model?
}