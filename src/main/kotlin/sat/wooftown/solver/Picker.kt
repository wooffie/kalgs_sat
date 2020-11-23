package sat.wooftown.solver

import sat.wooftown.util.Clause
import sat.wooftown.util.Model

interface Picker {

    fun getSolution() : Model

    fun hasConflict() : Pair<Boolean, Clause?>
}