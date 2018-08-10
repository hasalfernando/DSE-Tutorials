import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._

import scala.collection.immutable.ListMap

class ContentBasedRecommendation(usr_id:Int , dataPath:String, sc:SparkSession) extends RecommendationEngine(usr_id, dataPath, sc) {

  override var data: DataFrame = _

  private var arrangedData : DataFrame= _

  import sc.implicits._


  override def predict(movieId: Int, nOfPred : Int): Map[Double,(Int,String)] = {

    val temp1 = arrangedData.filter("movieId == "+movieId).drop("movieId").collect.map(_.toSeq).map(_.toArray)
    val sMovieFea = temp1(0).map(x=> FeatureScaling.extractDouble(x))

    val temp2 = arrangedData.filter("movieId != "+movieId).drop("movieId").collect.map(_.toSeq).map(_.toArray)
    val oMovieFea = temp2.map(row=> row.map(x=> FeatureScaling.extractDouble(x)))

    val temp3 = arrangedData.filter("movieId != "+movieId).select("movieId").collect().map(_.toSeq).map(_.toArray).map(row => row(0).asInstanceOf[Int])
    val temp4 = data.filter("movieId != "+movieId).select("title").collect().map(_.toSeq).map(_.toArray).map(row => row(0).toString)

    var index = 1

    var similarities  = Map[Double,(Int,String)]()

    for(index <- 1 to temp3.size){

      similarities += ( CosineSimilarity.cosineSimilarity(oMovieFea(index-1),sMovieFea) -> ( temp3(index-1) , temp4(index-1)))
    }

    ListMap(similarities.toSeq.sortWith(_._1 > _._1):_*).take(nOfPred)

  }


  override def run(): Unit = {

    loadData

    val year = data.select("movieId","title").map(t => (t.getInt(0),FeatureScaling.extractYear(t.getString(1))))

    val averageYear = year.toDF().filter("_2 != '"+0.0+"'").agg(avg("_2")).head().getDouble(0)

    val filteredYears = year.toDF().map(r=> (r.getInt(0),{
      if (r.getDouble(1) == 0.0) averageYear else r.getDouble(1)
    })).toDF()


    val yearDiff = FeatureScaling.GetMaxMinValues(filteredYears,"_2")

    val scaledYears = filteredYears.map(row => (row.getInt(0),FeatureScaling.ScaleValue(row.getDouble(1),yearDiff._1,yearDiff._2)))
      .toDF().withColumnRenamed("_1","movieId").withColumnRenamed("_2","Year")


    var genresFiltered = data.map(row=> FeatureScaling.booleanGenres(row.getString(2))).toDF()

    data = data.withColumn("rowId1", monotonically_increasing_id())
    genresFiltered = genresFiltered.withColumn("rowId2", monotonically_increasing_id())

    arrangedData = data.as("df1").join(genresFiltered.as("df2"), data("rowId1") === genresFiltered("rowId2"), "inner")
      .drop("rowId1","rowId2","genres").join(scaledYears,"movieId").drop("title")

  }

  override def loadData: Unit = {
    data  = sc.read
          .format("csv")
          .option("header", "true")
          .option("inferSchema", "true")
          .load(dataPath+"movies.csv")
  }


}
