package day20

fun main() {
  val field = Field.readField()
  val start = field.find('S')
  val end = field.find('E')
  println(countCheats(field, start, end, 20, 100))
}
