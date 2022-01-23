/**
 *
 *  @author Lonca Paweł S23452
 *
 */

package zad1;

// https://stooq.pl/q/d/l/?c=1&s=xdwd.uk&d1=20150325&d2=20220121&i=y
// https://stooq.pl/q/d/l/?c=1&s=<ticker>&d1=<yyyymmdd>&d2=<yyyymmdd>&i=<interval>
// intervals:
//  * y - yearly
//  * q - quarterly
//  * m - monthly
//  * w - weekly
//  * d - daily

//TODO: 2. Wyliczenie następujących statystyk dla pobranych danych:
//    - wydruk daty początkowej i końcowej - może być inna niż podana przez użytkownika
//    a. Stopa zwrotu w okresie
//    b. Średnie dzienne odchylenie stóp zwrotu
//    c. Średni dzienny obrót
//    d. cena minimalna w okresie
//    e. cena maksymalna w okresie
//    f. średnia cena w okresie
//    g. mediana cen w okresie
// TODO: możliwość zapisania wyników do pliku

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

static parseRow(row, sep=";") {
    def elems = row.split(sep)
    return ["date": elems[0], "price": elems[4], "volume": elems[5]]
}

assert args.length == 4, "You must provide four arguments"

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
println(url)
def output = url.getText()
println(output)

if (output.trim() == "Brak danych") {
    println("No data found for the parameters:\n\t> ticker: $ticker\n\t> start date: $startDate\n\t> end date: $endDate")
    return
}

output.split("\n").tail()
    .each {println(parseRow(it))}

