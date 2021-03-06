package controllers.commercial

import play.api.mvc._
import model.commercial.travel.OffersAgent
import common.{JsonNotFound, JsonComponent}
import model.Cached

object TravelOffers extends Controller {

  def listOffers = Action {
    implicit request =>
      OffersAgent.adsTargetedAt(segment) match {
        case Nil => JsonNotFound.apply()
        case offers => Cached(60)(JsonComponent(views.html.travelOffers(offers take 4)))
      }
  }

}
