package nl.wouter.mvn_scraper

import java.text.SimpleDateFormat
import java.util.concurrent.{ScheduledThreadPoolExecutor, TimeUnit}

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
    var releases: List[MavenRelease] = List()
    var newReleases: List[MavenRelease] = List()
    val ex = new ScheduledThreadPoolExecutor(1)
    val task = new Runnable {
      def run() = {
        newReleases = scraper.getUntilLatestRelease(latestRelease)
        val diff = newReleases.filterNot(releases.toSet)

        println(s"Diff in releases: ${diff.size}.")
        diff.foreach(println)

        releases = newReleases
      }
    }
    val f = ex.scheduleAtFixedRate(task, 0, 10, TimeUnit.MINUTES)

  }

}
