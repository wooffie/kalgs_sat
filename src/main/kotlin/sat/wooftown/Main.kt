package sat.wooftown

import org.kohsuke.args4j.Argument
import org.kohsuke.args4j.CmdLineException
import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.Option
import sat.wooftown.solver.SolverType
import java.io.File

/**
 * Entry point of program
 */
fun main(args: Array<String>) {
    Main().launch(args)
}

/**
 * Main class for jar file
 */
class Main {
    @Option(name = "-c", metaVar = "CDCL", required = false, usage = "Use CDCL solver")
    private var cdcl: Boolean = false

    @Option(name = "-d", metaVar = "DPLL", required = false, usage = "Use DPLL solver")
    private var dpll: Boolean = false

    @Option(name = "-s", metaVar = "Solution", required = false, usage = "Print solution")
    private var printSolution: Boolean = false

    @Argument(required = false, metaVar = "InputFiles", usage = "Input files names")
    private var inputFilesNames = mutableListOf<String>()


    fun launch(args: Array<String>) {
        val parser = CmdLineParser(this)

        try {
            parser.parseArgument(*args)
        } catch (e: CmdLineException) {
            println(e.message)
            println("java -jar kalgs_sat.jar [-c |-d ] file0 file1 file2")
            parser.printUsage(System.out)
            return
        }

        if (inputFilesNames.isEmpty()) {
            println("no input files")
        }

        val solversType = mutableListOf<SolverType>()


        // switch бы сюда без break в конец каждого блока
        if (dpll) {
            solversType.add(SolverType.DPLL)
        }
        if (cdcl) {
            solversType.add(SolverType.CDCL)
        }
        if (solversType.isEmpty()){
            println("Choose type of solver")
            parser.printUsage(System.out)
            return
        }


        for (file in inputFilesNames) {
            println("Solving $file")
            for (solver in solversType){
                println("$solver:")
                val result = solver.solve(File(file))
                if (result != null){
                    println("SAT")
                    if (printSolution){
                        println(result)
                    }
                } else {
                    println("UNSAT")
                }
            }
        }

    }


}

