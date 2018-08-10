object CosineSimilarity {

  def cosineSimilarity(x: Array[Double], y: Array[Double]): Double = {
    require(x.size == y.size)
    dotProduct(x, y)/(magnitude(x) * magnitude(y))
  }

  def dotProduct(x: Array[Double], y: Array[Double]): Double = {
    (for((a, b) <- x zip y) yield a * b) sum
  }

  def magnitude(x: Array[Double]): Double = {
    math.sqrt(x map(i => i*i) sum)
  }
}