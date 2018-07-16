package ca.usherbrooke.degel.helpers.criterias

interface Criteria<T> {
    infix fun apply(input: List<T>) : List<T>

    infix fun and(other: Criteria<T>) = AndCriteria<T>(this, other)

    infix fun or(other: Criteria<T>) = OrCriteria(this, other)

    infix fun not(other: Criteria<T>) = NotCriteria(other)
}

class AndCriteria<T>(private val leftCriteria: Criteria<T>,
                     private val rightCriteria: Criteria<T>) : Criteria<T> {
    override fun apply(input: List<T>): List<T> {
        val leftCriteriaOut = leftCriteria.apply(input)
        return rightCriteria.apply(leftCriteriaOut)
    }
}

class OrCriteria<T>(private val leftCriteria: Criteria<T>,
                    private val rightCriteria: Criteria<T>) : Criteria<T> {
    override fun apply(input: List<T>): List<T> {
        val leftCriteriaOut = leftCriteria.apply(input).toMutableList()
        val rightCriteriaOut = rightCriteria.apply(input)

        rightCriteriaOut.forEach {
            if(it !in leftCriteriaOut)
                leftCriteriaOut.add(it)
        }

        return leftCriteriaOut
    }
}

class NotCriteria<T>(private val criteria: Criteria<T>) : Criteria<T> {
    override fun apply(input: List<T>): List<T> {
        val mutableInput = input.toMutableList()

        val criteriaOut = criteria.apply(input)
        mutableInput.removeAll(criteriaOut)

        return mutableInput
    }
}