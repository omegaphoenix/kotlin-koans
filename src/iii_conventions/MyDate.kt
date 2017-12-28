package iii_conventions


data class MyDate(val year: Int, val month: Int, val dayOfMonth: Int) : Comparable<MyDate> {
    private val daysInWeek = 7
    override fun compareTo(other: MyDate) = when
    {
        year != other.year -> year - other.year
        month != other.month -> month - other.month
        else -> dayOfMonth - other.dayOfMonth
    }

    operator fun plus(interval: TimeInterval) = when (interval) {
        TimeInterval.DAY -> this.nextDay()
        TimeInterval.WEEK -> this.nextDayHelper(daysInWeek)
        else -> MyDate(year + 1, month, dayOfMonth)
    }

    private fun nextDayHelper(n: Int): MyDate = when {
        n <= 0 -> this
        else -> this.nextDay().nextDayHelper(n - 1)
    }

    operator fun plus(interval: RepeatedTimeInterval) = when (interval.ti) {
        TimeInterval.DAY -> this.nextDayHelper(interval.n)
        TimeInterval.WEEK -> this.nextDayHelper(daysInWeek * interval.n)
        else -> MyDate(year + interval.n, month, dayOfMonth)
    }
}

operator fun MyDate.rangeTo(other: MyDate): DateRange = DateRange(this, other)

enum class TimeInterval {
    DAY,
    WEEK,
    YEAR
}
    operator fun TimeInterval.times(n: Int)
        = RepeatedTimeInterval(this, n)

class DateRange(val start: MyDate, val endInclusive: MyDate) : Iterable<MyDate> {
    operator fun contains(d: MyDate) =
            start <= d && endInclusive >= d
    override operator fun iterator() : Iterator<MyDate> {
        return DateIterator(start, endInclusive)
    }
}

class DateIterator(var date: MyDate, val end: MyDate) : Iterator<MyDate> {
    override operator fun next(): MyDate {
        val prev = date
        date = date.nextDay()
        return prev
    }
    override operator fun hasNext() = date <= end
}

class RepeatedTimeInterval(val ti: TimeInterval, val n: Int)
