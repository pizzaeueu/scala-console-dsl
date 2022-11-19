package com.consolebuilder

import cats.data.StateT
import cats.Applicative

trait Dsl[F[_]]() {
  
  def writeMessage(input: String): StateT[F, List[String], Unit]
}

object Dsl {
    def of[F[_]: Applicative]() = new Dsl[F]{

      override def writeMessage(input: String): StateT[F, List[String], Unit] = 
        StateT.modify[F, List[String]](state => state :+ input)
        
    }
}