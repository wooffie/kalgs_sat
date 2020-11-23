package sat.wooftown.solver


import sat.wooftown.io.Parser
import sat.wooftown.solver.cdcl.CDCLSolver
import sat.wooftown.solver.dpll.NaiveSolver
import sat.wooftown.util.Model
import java.io.File
import java.util.*

fun main(){
    ReportGenerator().main()
}

fun deleteReports(){
    ReportGenerator().deleteReports()
    TODO()
}

class ReportGenerator {

    fun main(){

        val data = Date().toString().split(" ").drop(1).dropLast(2).joinToString("_").replace(":","-")
        val outputStream = File("report_${data}.txt").bufferedWriter()

        val filesForTest = File("src\\test\\resources").walkTopDown().drop(1)

        for (file in filesForTest){
            outputStream.write("Solving $file")
            outputStream.newLine()
            for (solver in SolverType.values()){
                outputStream.write("$solver :")
                val result = solve(file,solver)
                outputStream.write("${result.first / 1000 } s")
                outputStream.newLine()
                if (result.second != null){
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

    enum class SolverType{
        DPLL , CDCL
    }

    private fun solve(file: File,solver: SolverType) : Pair<Long, Model?> {
        val parser = Parser(file).parse()
        val start = System.currentTimeMillis()
        val solution = when(solver){
            SolverType.DPLL -> NaiveSolver(parser).solve()
            SolverType.CDCL -> CDCLSolver(parser).solve()
        }
        return System.currentTimeMillis() - start to solution
    }




    fun deleteReports(){
        TODO()
    }


}