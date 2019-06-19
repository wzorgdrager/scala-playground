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

  def getUntilLatestRelease(
      latestRelease: (String, Date)): List[MavenRelease] = {
    println(s"Now retrieving releases until ${latestRelease._2}.")

    /** TODO: Turn this into pretty fp code **/
    var found = false
    var currentPage = 1
    var releasesList: List[MavenRelease] = Nil

    while (!found) {
      val releases = getReleasesByPage(currentPage)
      val filteredReleases = releases.filter(_.date.after(latestRelease._2))

      releasesList = filteredReleases ::: releasesList

      if (releases.size != filteredReleases.size) {
        found = true
      } else {
        currentPage = currentPage + 1
      }
    }

    println(
      releasesList
        .sortBy(_.date.getTime)
        .reverse
        .head
        .date)
    val diffHours = (releasesList
      .sortBy(_.date.getTime)
      .reverse
      .head
      .date
      .getTime - latestRelease._2.getTime) / 60 / 60 / 1000

    val stat = releasesList.size / diffHours
    println(s"Sorted: ${releasesList == releasesList.sortBy(_.date.getTime)}")
    println(s"New releases (since ${latestRelease._2}): ${releasesList.size}")
    println(
      s"The difference is $diffHours, which means on average $stat releases/h.")

    releasesList
  }

  def getReleasesByPage(pageId: Int): List[MavenRelease] = {
    val url = new URL(staticURL.replace(pageIdentifier, pageId.toString))
    val cleaner = new HtmlCleaner
    val props = cleaner.getProperties
    val root = cleaner.clean(url)

    val releases = root.getElementListByName("tr", true)

    println(s"Retrieving releases from page $pageId.")
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
