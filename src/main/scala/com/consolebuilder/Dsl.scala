package com.consolebuilder

import cats.data.StateT
import cats.Applicative
import com.consolebuilder.domain.*

trait Dsl[F[_]]() {
  
  def writeMessage(input: TextMessage): StateT[F, List[InteractiveMessage], Unit]
}

object Dsl {
    def of[F[_]: Applicative]() = new Dsl[F]{

      override def writeMessage(input: TextMessage): StateT[F, List[InteractiveMessage], Unit] = 
        StateT.modify[F, List[InteractiveMessage]](state => state :+ input)
        
    }
}