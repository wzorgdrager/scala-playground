package nl.wouter.mvn_scraper

import org.htmlcleaner.HtmlCleaner
import java.util.Date
import java.net.URL
import java.text.SimpleDateFormat

import collection.JavaConverters._

case class MavenRelease(groupId: String,
                        artifactId: String,
                        version: String,
                        date: Date)

class MavenScraper {

  val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  val pageIdentifier = "$PAGE_ID"
  val staticURL = "http://maven-repository.com/artifact/latest?page=$PAGE_ID"
  val latestRelease: (String, Date) = ("", new Date)

  def getUntilLatestRelease(): List[MavenRelease] = {
    List()

  }

  def getReleasesByPage(pageId: Int): List[MavenRelease] = {
    val url = new URL(staticURL.replace(pageIdentifier, pageId.toString))
    val cleaner = new HtmlCleaner
    val props = cleaner.getProperties
    val root = cleaner.clean(url)

    val releases = root.getElementListByName("tr", true)

    releases.asScala
      .drop(1)
      .map { x =>
        val releaseDetails = x.getChildTagList.asScala

        val group = releaseDetails(0).getText.toString
        val artifact = releaseDetails(1).getText.toString
        val version = releaseDetails(2).getText.toString
        val date = releaseDetails(3).getText.toString

        MavenRelease(group, artifact, version, dateFormat.parse(date))
      }
      .sortBy(_.date.getTime)
      .toList
  }

}
