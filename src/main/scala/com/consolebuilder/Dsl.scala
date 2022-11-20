package com.consolebuilder

import cats.data.StateT
import cats.Applicative
import com.consolebuilder.domain.*
import com.consolebuilder.domain.UserResponse

trait Dsl[F[_]]() {
  
  def writeMessage(input: TextMessage): StateT[F, List[InteractiveMessage], Unit]
  
  def readMessage(): StateT[F, List[InteractiveMessage], String]
}

object Dsl {
    def of[F[_]: Applicative]() = new Dsl[F]{


      override def readMessage(): StateT[F, List[InteractiveMessage], String] =
        val representation = scala.util.Random.nextString(7)
        StateT.modify[F, List[InteractiveMessage]](state => 
            state :+ UserResponse(representation)
          )
          .map(_ => representation)

      override def writeMessage(input: TextMessage): StateT[F, List[InteractiveMessage], Unit] = 
        StateT.modify[F, List[InteractiveMessage]](state => state :+ input)
        
    }
}