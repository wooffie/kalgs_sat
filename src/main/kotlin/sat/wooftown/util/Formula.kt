package sat.wooftown.util

/*
Все наши дизъюнкьтвы вместе
операторы для удобства работы
 */

/**
 * Конюънкта дизъюнкт
 * @param clauses - наши наборы дизъюнки
 */
// Хотел сделать Collection<Clause>
class Formula(
    vararg clauses: Clause,
) {

    // набор дизъюнкт
    val clauses = mutableSetOf<Clause>()

    init {
        this.clauses.addAll(clauses)
    }

    /**
     * Добавить к набору ещё один clause
     */
    operator fun times(other: Clause): Formula {
        clauses.add(other)
        return this
    }

    override fun equals(other: Any?): Boolean = other is Formula && clauses == other.clauses

    override fun hashCode(): Int {
        return clauses.hashCode()
    }

}