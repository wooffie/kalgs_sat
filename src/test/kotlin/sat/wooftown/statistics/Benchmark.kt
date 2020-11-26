package sat.wooftown.statistics

import sat.wooftown.io.Parser
import sat.wooftown.solver.SolverType
import java.io.BufferedWriter
import java.io.File
import java.util.*

/**
 * Report for benchmark, only for generating report on my PC, cause count of files are too big
 */
fun main() {
    val path = File(System.getProperty("user.dir")).toString() + "\\stats\\benchmark"
    val reportPath = File(System.getProperty("user.dir")).toString() + "\\stats\\benchmarkStats"

    fun generate(sat: String) {
        val listOfTypes = mutableListOf<Pair<BufferedWriter, SolverType>>()
        val nameOfReport = sat.split("\\").last()

        val dpllReport = File("$reportPath\\DPLL\\DPLL_report_$nameOfReport.txt").bufferedWriter()
        val cdclReport = File("$reportPath\\CDCL\\CDCL_report_$nameOfReport.txt").bufferedWriter()
        listOfTypes.add(dpllReport to SolverType.DPLL)
        listOfTypes.add(cdclReport to SolverType.CDCL)
        listOfTypes.map { it.first }.forEach { bufferedWriter ->
            bufferedWriter.write(Date().toString())
            bufferedWriter.newLine()
        }
        val listOfFiles = File(sat).walkTopDown().drop(1)

        for (file in listOfFiles) {
            println(file)
            for ((writer, solver) in listOfTypes) {
                val parser = Parser(file)
                writer.write("${solver.solveWithTime(parser.parse())}")
                writer.newLine()
                System.gc()
            }
        }

        listOfTypes.map { it.first }.forEach { bufferedWriter ->
            bufferedWriter.close()
        }


    }

    val satDirectories = File("$path\\sat").walkTopDown().drop(1).filter { it.isDirectory }.toList()

    val unsatDirectories = File("$path\\unsat").walkTopDown().drop(1).filter { it.isDirectory }.toList()
    val sortedList = (satDirectories + unsatDirectories).sortedBy { Regex("\\d+").find(it.toString())!!.value.toInt() }
    for (i in sortedList) {
        generate(i.toString())
    }

}
