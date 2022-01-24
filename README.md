# Introduction

This is the fifth homework in Programming Languages Practice course which is dedicated to learning Groovy. Other repositories you may be interested in:
* [Homework no. 1](https://github.com/balkon16/PJP_HW1)
* [Homework no. 2](https://github.com/balkon16/PJP_HW2)
* [Homework no. 3](https://github.com/balkon16/PJP_HW3)
* [Homework no. 4](https://github.com/balkon16/PJP_HW4)

# Exercise no. 1

The script *Main.groovy* is a tool for computing statistics for exchange-traded financial instruments. The script accepts four parameters:
1. Ticker, e.g. KGH, ^SPX, IBCI.DE, 241.HK. Consult the list of tickers on the [stooq.pl](https://stooq.pl/) website.
2. Start date formatted as `YYYY-MM-DD`.
3. End date formatted as `YYYY-MM-DD`.
4. Interval. Available options are:
   * y - yearly
   * q - quarterly
   * m - monthly
   * w - weekly
   * d - daily

```shell
groovy src/zad1/Main.groovy IBCI.DE 2015-01-11 2022-01-19 d
```