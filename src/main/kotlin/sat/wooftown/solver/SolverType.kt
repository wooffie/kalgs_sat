package sat.wooftown.solver

import sat.wooftown.io.Parser
import sat.wooftown.solver.cdcl.CDCLSolver
import sat.wooftown.solver.dpll.DPLLSolver
import sat.wooftown.util.Model
import java.io.File

/**
 * Types of solvers in project, can be added more
 */
enum class SolverType {
    DPLL, CDCL;

    /**
     * Solve .cnf with *this* type of solver
     */
    fun solve(file: File) : Model? {
        val parsed = Parser(file).parse()
        return when (this) {
            DPLL -> DPLLSolver(parsed).solve()
            CDCL -> CDCLSolver(parsed).solve()
        }
    }

}