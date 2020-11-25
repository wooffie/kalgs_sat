package sat.wooftown

import org.kohsuke.args4j.Argument
import org.kohsuke.args4j.CmdLineException
import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.Option
import sat.wooftown.io.Parser
import sat.wooftown.solver.SolverType
import sat.wooftown.solver.cdcl.CDCLSolver
import sat.wooftown.solver.dpll.NaiveSolver
import sat.wooftown.util.Model


fun main(args: Array<String>) {
    Main().launch(args)
}

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
            println("java -jar tail.jar [-c |-d ] file0 file1 file2")
            parser.printUsage(System.out)
            return
        }

        if (inputFilesNames.isEmpty()) {
            println("no input files")
        }

        // reformat TODO()
        if (!cdcl && !dpll) {
            cdcl = true
            dpll = true
        }

        for (files in inputFilesNames) {
            println(files)
            val parserCNF = Parser(files)


            val formula = parserCNF.parse()
            if (cdcl) {
                val x = System.currentTimeMillis()
                print("CDCL solver: ")
                val solution = CDCLSolver(formula).solve()
                println("${System.currentTimeMillis() - x} ms")
                if (solution == null) {
                    println("UNSAT")
                } else {
                    println("SAT")
                    if (printSolution) {
                        print(solution)
                    }
                }
            }
            System.gc()
            if (dpll) {
                val x = System.currentTimeMillis()
                print("DPLL solver:")
                val solution = NaiveSolver(formula).solve()
                println("${System.currentTimeMillis() - x}")
                if (solution == null) {
                    println("UNSAT")
                } else {
                    println("SAT")
                    if (printSolution) {
                        print(solution)
                    }
                }
            }
        }
    }



}

