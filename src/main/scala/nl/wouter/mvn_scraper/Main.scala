package nl.wouter.mvn_scraper

import java.text.SimpleDateFormat

object Main {

  def main(args: Array[String]): Unit = {
    val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val latestRelease = ("", format.parse("2019-06-13 00:14:09"))
    new MavenScraper()
      .getUntilLatestRelease(latestRelease)
  }

}
