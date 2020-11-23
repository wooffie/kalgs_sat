package sat.wooftown.solver

import sat.wooftown.util.Model

interface Solver {

    val picker : Picker

    fun solve(): Model?
}