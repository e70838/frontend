package model

import common.Jobs
import feed.{MostPopularExpandableAgent, MostPopularAgent}
import play.api.{ Application => PlayApp, Play, GlobalSettings }
import play.api.Play.current
import common.editions.Uk
import scala.concurrent.duration._
import scala.concurrent.Await


trait MostPopularLifecycle extends GlobalSettings {
  override def onStart(app: PlayApp) {
    super.onStart(app)

    Jobs.deschedule("MostPopularAgentRefreshJob")

    // fire every min
    Jobs.schedule("MostPopularAgentRefreshJob",  "0 * * * * ?") {
      MostPopularAgent.refresh()
      MostPopularExpandableAgent.refresh()
    }

    if (Play.isTest) {
      Await.result(MostPopularAgent.refresh(Uk), 5.seconds)
    }
  }

  override def onStop(app: PlayApp) {
    Jobs.deschedule("MostPopularAgentRefreshJob")
    super.onStop(app)
  }
}
