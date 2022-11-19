package com.consolebuilder

import cats.Monad
import scala.collection.immutable.List
import cats.kernel.Semigroup
import cats.syntax.all._
import cats.implicits._

trait MessageB


case class InteractiveInput[A <: MessageB](a: A, state: List[A] = Nil) {

    def addState(newState: A) = state :+ newState
}


//accamulate state during map/flatMap

//val t =  new Monad[InteractiveInput[MessageB]] {
 
