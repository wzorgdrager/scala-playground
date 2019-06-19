package nl.wouter.mvn_scraper

import java.text.SimpleDateFormat

object Main {

  def main(args: Array[String]): Unit = {
    val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val latestRelease = ("", format.parse("2019-06-13 00:14:09"))

    val scraper = new MavenScraper

    /* val releases =
      (1 to 20)
        .map(scraper.getReleasesByPage(_))
        .flatten
        .sortBy(_.date.getTime)
        .toList
     */
    val releases = scraper.getUntilLatestRelease(latestRelease)

    scraper.printSummary(releases)
    println(releases.reverse.head)
    println(releases.head)
  }

}
