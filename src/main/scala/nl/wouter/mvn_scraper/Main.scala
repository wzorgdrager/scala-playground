package nl.wouter.mvn_scraper

object Main {

  def main(args: Array[String]): Unit = {
    new MavenScraper().getReleasesByPage(1)
  }

}
