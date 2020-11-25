package sat.wooftown.solver


import sat.wooftown.io.Parser
import sat.wooftown.solver.cdcl.CDCLSolver
import sat.wooftown.solver.dpll.NaiveSolver
import sat.wooftown.util.Model
import java.io.File
import java.util.*

fun main() {
    println("Print \"delete\" for delete reports, or nothing to generate report")
    when(readLine()){
        "delete" -> ReportGenerator().deleteReports()
        else ->  ReportGenerator().main()
    }
}


class ReportGenerator {

    fun main() {

        val data = Date().toString().split(" ").subList(1, 4).joinToString("_").replace(":", "-")
        val outputStream = File("report_${data}.txt").bufferedWriter()
        val filesForTest = File("src\\test\\resources").walkTopDown().drop(1)

        for (file in filesForTest) {
            outputStream.write("Solving $file")
            outputStream.newLine()
            for (solver in SolverType.values()) {
                outputStream.write("$solver :")
                val result = solve(file, solver)
                outputStream.write("${result.first / 1000} s")
                outputStream.newLine()
                if (result.second != null) {
                    outputStream.write("SAT")
                } else {
                    outputStream.write("UNSAT")
                }
                outputStream.newLine()
                System.gc()
            }
            outputStream.newLine()
        }

        outputStream.close()
    }

    private fun solve(file: File, solver: SolverType): Pair<Long, Model?> {
        val parser = Parser(file).parse()
        val start = System.currentTimeMillis()
        val solution = when (solver) {
            SolverType.DPLL -> NaiveSolver(parser).solve()
            SolverType.CDCL -> CDCLSolver(parser).solve()
        }
        return System.currentTimeMillis() - start to solution
    }


    fun deleteReports() {
        val f = File(System.getProperty("user.dir"))
        val match = f.listFiles() ?: return
        for (file in match.filterNotNull()) {
            val name = file.name
            if (name.startsWith("report") && name.endsWith(".txt")) {
                file.delete()
            }
        }
    }


}