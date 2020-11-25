package sat.wooftown.solver

import sat.wooftown.util.Clause
import sat.wooftown.util.Model

/**
 * Picker which pick next literal
 */
interface Picker {

    /**
     * Get solution of formula
     */
    fun getSolution() : Model

    /**
     * Check for conflict
     */
    fun hasConflict() : Pair<Boolean, Clause?>
}