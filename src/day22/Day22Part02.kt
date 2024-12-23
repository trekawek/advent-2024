package day22

const val CHANGES = 2000

fun main() {
  val secrets = generateSequence(::readlnOrNull).map(String::toInt).toList()
  val changeSeqToPrice: MutableMap<List<Int>, Int> = mutableMapOf()
  for (s in secrets) {
    val prices = getPrices(s, CHANGES + 1)
    val priceChanges = prices.zipWithNext().map { it.second - it.first }
    val changeSeqForSecret: MutableSet<List<Int>> = mutableSetOf()
    for (i in (0..priceChanges.size - 4)) {
      val changeSeq = priceChanges.subList(i, i + 4)
      if (!changeSeqForSecret.add(changeSeq)) {
        continue
      }
      changeSeqToPrice[changeSeq] = (changeSeqToPrice[changeSeq] ?: 0) + prices[i + 4]
    }
  }
  println(changeSeqToPrice.toList().maxByOrNull { it.second }!!)
}

fun getPrices(seed: Int, priceCount: Int): List<Int> {
  return generateSecrets(seed.toLong()).map { it.mod(10) }.take(priceCount).toList()
}
