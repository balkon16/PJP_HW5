/**
 *
 * @author Lonca Paweł S23452
 *
 */

package zad1;

static isCorrectDate(dateStr) {
    def daysInMonth = [
            "01": [31, 31],
            "02": [28, 29],
            "03": [31, 31],
            "04": [30, 30],
            "05": [31, 31],
            "06": [30, 30],
            "07": [31, 31],
            "08": [31, 31],
            "09": [30, 30],
            "10": [31, 31],
            "11": [30, 30],
            "12": [31, 31]
    ]

    def (yearStr, monthStr, dayStr) = dateStr.split("-")
    def yearInt = yearStr.toInteger()
    def isLeapInt = ((yearInt % 4 == 0 & yearInt % 100 != 0) | (yearInt % 400 == 0)) ? 1 : 0
    def maxDays = daysInMonth.getOrDefault(monthStr, null)
    if (maxDays.is(null)) return false
    if (dayStr.toInteger() > maxDays[isLeapInt] | dayStr.toInteger() == 0) return false
    return true
}

static getFormattedBigDecimal(bigDecimal) {
    return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP)
}


static parseRow(row, sep = ";") {
    def elems = row.split(sep)
    return ["date": elems[0], "price": new BigDecimal(elems[4]), "volume": new Long(elems[5])]
}

static getReturnRate(parsedRows) {
    return (parsedRows.last().get("price") / parsedRows.first().get("price") - 1) * 100
}

static getEffectiveDates(parsedRows) {
    return new Tuple2(parsedRows.first().get("date"), parsedRows.last().get("date"))
}

static getMedian(values) {
    def sortedValues = values.toSorted()
    def middleIndex = values.size().intdiv(2)
    return values.size() % 2 ? sortedValues[middleIndex] : (sortedValues[middleIndex - 1] + sortedValues[middleIndex]) / 2
}

static getPriceStats(parsedRows) {
    def prices = parsedRows.collect { it.get("price") }

    return new Tuple4<>(prices.min(), prices.average(), getMedian(prices), prices.max())
}

static getVolumeStats(parsedRows) {
    def volumeInCurrencyList = parsedRows.collect { it.get("price") * it.get("volume") }
    return volumeInCurrencyList.average()
}

assert args.length == 4, "You must provide at least four arguments"

def ticker = args[0]

def startDate = args[1]
assert startDate.length() == 10, "Date must be specified in the 'YYYY-MM-DD' format."
assert isCorrectDate(startDate), "The date $startDate does not exist."

def endDate = args[2]
assert endDate.length() == 10, "Date must be specified in the 'YYYY-MM-DD' format."
assert isCorrectDate(endDate), "The date $endDate does not exist."

assert Date.parse("yyyy-MM-dd", endDate) > Date.parse("yyyy-MM-dd", startDate),
        "The `endDate` must be at least the next day after the `startDate`: $endDate vs $startDate"


def allowedIntervals = ['y', 'q', 'm', 'w', 'd'] as Set
def interval = args[3]
assert allowedIntervals.contains(interval), "Interval must be one of $allowedIntervals"

def url = new URL("https://stooq.pl/q/d/l/?c=1&s=$ticker&d1=${startDate.replace("-", "")}&d2=${endDate.replace("-", "")}&i=$interval")

def output = url.getText()

if (output.trim() == "Brak danych") {
    println("No data found for the parameters:\n\t> ticker: $ticker\n\t> start date: $startDate\n\t> end date: $endDate")
    return
}

def rows = output.split("(\r)?\n").tail()
        .findAll { it.split(";").size() == 6 }
        .collect { parseRow(it) }

def effectiveDates = getEffectiveDates(rows)
println("Start date: ${effectiveDates[0]}, end date: ${effectiveDates[1]}")
println("The return rate is ${getFormattedBigDecimal(getReturnRate(rows))} %")
def pricesStats = getPriceStats(rows)
println("Price statistics:\n\t" +
        "low: ${getFormattedBigDecimal(pricesStats[0])}\n\t" +
        "average: ${getFormattedBigDecimal(pricesStats[1])}\n\t" +
        "median: ${getFormattedBigDecimal(pricesStats[2])}\n\t" +
        "high: ${getFormattedBigDecimal(pricesStats[3])}")
println("The average volume was ${getFormattedBigDecimal(getVolumeStats(rows))}")