import com.sun.javaws.exceptions.InvalidArgumentException
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql
import org.apache.spark.sql.{AnalysisException, SparkSession}

object SparkTask02 {

  //Necessary fields
  val spark: SparkSession = SparkSession.builder() //create spark session
    .appName("Spark Task 02")
    .master("local[*]")
    .getOrCreate()
  val sc = spark.sparkContext // create spark context
  val rootLogger = Logger.getRootLogger() //get root logger and set level off
  rootLogger.setLevel(Level.ERROR)

//main method
  def main(args: Array[String]): Unit = {

    try {

      val csvTasks = new CSVProcessor(spark, sc, "src/main/resources/athlete_events.csv")
      csvTasks.readFile()

      println("Total Number of Athletes " + csvTasks.totalNumberOfAthletes())
      csvTasks.highestMedalsCountryYear("Gold")
      csvTasks.findAverge("Height","M")
      csvTasks.findAverge("Weight","F")
      csvTasks.genderRatio()
      csvTasks.medalDistributionByCountry()
      csvTasks.mostPopularSport()

    }catch {
      case e:IllegalArgumentException=> rootLogger.error("Null or Invalid argument entered")
      case e:AnalysisException=> rootLogger.error("Invalid value entered")
      case e:NullPointerException=> rootLogger.error("Null value entered")

    }
  }
}