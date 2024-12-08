package day08

typealias Point = Pair<Int, Int>

typealias Vector = Pair<Int, Int>

operator fun Point.plus(v: Vector) = Point(first + v.first, second + v.second)
operator fun Point.minus(p: Point) = Vector(first - p.first, second - p.second)
operator fun Vector.times(i: Int) = Vector(first * i, second * i)
operator fun Vector.unaryMinus() = Vector(-first, -second)