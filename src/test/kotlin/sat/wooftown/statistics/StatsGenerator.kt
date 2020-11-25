package sat.wooftown.statistics

import sat.wooftown.io.Parser
import sat.wooftown.solver.SolverType
import java.io.BufferedWriter
import java.io.File
import java.util.*

fun main() {
    val path = File(System.getProperty("user.dir")).toString() + "\\stats\\cnfs"
    StatsGenerator(path).generateReports()

}

/**
 * Stats which you can get on your PC
 */
class StatsGenerator(private val path: String) {

    fun generateReports() {

        fun generate(sat : String){
            val listOfTypes = mutableListOf<Pair<BufferedWriter,SolverType>>()
            val dpllReport = File("$path\\DPLL_report_$sat.txt").bufferedWriter()
            val cdclReport = File("$path\\CDCL_report_$sat.txt").bufferedWriter()
            listOfTypes.add(dpllReport to SolverType.DPLL)
            listOfTypes.add(cdclReport to SolverType.CDCL)
            listOfTypes.map { it.first }.forEach { bufferedWriter ->
                bufferedWriter.write(Date().toString())
                bufferedWriter.newLine()
            }
            val listOfFiles = File("$path\\$sat").walkTopDown().drop(1)
            for (file in listOfFiles){
                println(file)
                for ((writer,solver) in listOfTypes){
                    val parser = Parser(file)
                    val stats = parser.getStats()
                    writer.write("${stats.first} ${stats.second}")
                    writer.write(" ${solver.solveWithTime(parser.parse())}")
                    writer.newLine()
                    System.gc()
                }
            }
            listOfTypes.map { it.first }.forEach { bufferedWriter ->
                bufferedWriter.write(Date().toString())
                bufferedWriter.close()
            }
        }
     //   generate("sat")
        generate("unsat") // TODO() DELETE
    }


}
